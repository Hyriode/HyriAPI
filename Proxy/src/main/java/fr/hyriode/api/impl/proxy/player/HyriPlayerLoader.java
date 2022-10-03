package fr.hyriode.api.impl.proxy.player;

import com.google.gson.JsonObject;
import fr.hyriode.api.HyriAPI;
import fr.hyriode.api.impl.proxy.HyriAPIPlugin;
import fr.hyriode.api.party.HyriPartyDisbandReason;
import fr.hyriode.api.party.IHyriParty;
import fr.hyriode.api.player.IHyriPlayer;
import fr.hyriode.api.player.IHyriPlayerManager;
import fr.hyriode.api.player.event.PlayerQuitNetworkEvent;
import fr.hyriode.api.player.nickname.IHyriNickname;
import net.md_5.bungee.Util;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.UUID;

/**
 * Project: HyriAPI
 * Created by AstFaster
 * on 13/04/2022 at 19:13
 */
public class HyriPlayerLoader {

    private static final String PROFILES_CACHE_KEY = "proxy-mojang-profiles:";

    private final HyriAPIPlugin plugin;

    public HyriPlayerLoader(HyriAPIPlugin plugin) {
        this.plugin = plugin;
    }

    public MojangProfile fetchMojangProfile(String playerName) {
        final String key = PROFILES_CACHE_KEY + playerName;
        final MojangProfile cachedProfile = HyriAPI.get().getRedisProcessor().get(jedis -> {
            final String json = jedis.get(key);

            return json == null ? null : HyriAPI.GSON.fromJson(json, MojangProfile.class);
        });

        if (cachedProfile != null) {
            return cachedProfile;
        }

        try (final CloseableHttpClient client = HttpClients.createDefault()) {
            final HttpGet request = new HttpGet("https://api.mojang.com/users/profiles/minecraft/" + playerName);
            final HttpResponse response = client.execute(request);
            final int statusCode = response.getStatusLine().getStatusCode();

            if (statusCode == 200 || statusCode == 204) {
                final boolean premium = statusCode == 200;

                MojangProfile profile;
                if (premium) {
                    final JsonObject body = HyriAPI.GSON.fromJson(EntityUtils.toString(response.getEntity()), JsonObject.class);

                    profile = new MojangProfile(Util.getUUID(body.get("id").getAsString()), true);
                } else {
                    profile = new MojangProfile(UUID.nameUUIDFromBytes(("OfflinePlayer:" + playerName).getBytes(StandardCharsets.UTF_8)), false);
                }

                HyriAPI.get().getRedisProcessor().process(jedis -> {
                    jedis.set(key, HyriAPI.GSON.toJson(profile));
                    // Save the fetched profile for 24 hours
                    jedis.expire(key, 24 * 60 * 60L);
                });

                return profile;
            } else {
                throw new RuntimeException("An error occurred while requesting to Mojang!");
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void loadPlayerAccount(IHyriPlayer account, String name) {
        final IHyriPlayerManager playerManager = HyriAPI.get().getPlayerManager();

        account.setName(name);
        account.setLastLoginDate(new Date(System.currentTimeMillis()));
        account.setCurrentProxy(HyriAPI.get().getProxy().getName());
        account.setParty(null);
        account.update();

        playerManager.setPlayerId(name, account.getUniqueId());
    }

    public void handleDisconnection(ProxiedPlayer player) {
        final UUID playerId = player.getUniqueId();

        this.unloadPlayerAccount(playerId);

        HyriAPI.get().getNetworkManager().getEventBus().publishAsync(new PlayerQuitNetworkEvent(playerId));

        this.plugin.getAPI().getProxy().removePlayer(playerId);
        this.plugin.getAPI().getHyggdrasilManager().sendData();
    }

    private void unloadPlayerAccount(UUID uuid) {
        final IHyriPlayer account = IHyriPlayer.get(uuid);
        final IHyriPlayerManager pm = HyriAPI.get().getPlayerManager();

        if (account != null) {
            final UUID partyId = account.getParty();

            if (partyId != null) {
                final IHyriParty party = HyriAPI.get().getPartyManager().getParty(partyId);

                if (party != null) {
                    final UUID playerId = account.getUniqueId();

                    if (party.isLeader(playerId)) {
                        party.disband(HyriPartyDisbandReason.LEADER_LEAVE);
                    } else {
                        party.removeMember(account.getUniqueId());
                    }
                }
            }

            HyriAPI.get().getQueueManager().removePlayerFromQueue(uuid);
            HyriAPI.get().getQueueManager().removePlayerQueue(uuid);

            account.setParty(null);
            account.setLastPrivateMessagePlayer(null);
            account.setPlayTime(account.getPlayTime() + (System.currentTimeMillis() - account.getLastLoginDate().getTime()));
            account.setCurrentServer(null);
            account.setLastServer(null);
            account.setInVanishMode(false);
            account.setInModerationMode(false);
            account.setOnline(false);
            account.setCurrentProxy(null);

            final IHyriNickname nickname = account.getNickname();

            if (nickname != null) {
                pm.getNicknameManager().removeUsedNickname(nickname.getName());

                account.setNickname(null);
            }

            account.update();
        }
    }

    public static class MojangProfile {

        private final UUID playerId;
        private final boolean premium;

        public MojangProfile(UUID playerId, boolean premium) {
            this.playerId = playerId;
            this.premium = premium;
        }

        public UUID getPlayerId() {
            return this.playerId;
        }

        public boolean isPremium() {
            return this.premium;
        }

    }

}
