package fr.hyriode.api.impl.proxy.player;

import com.google.gson.JsonObject;
import fr.hyriode.api.HyriAPI;
import fr.hyriode.api.impl.common.player.HyriPlayerSession;
import fr.hyriode.api.player.IHyriPlayer;
import fr.hyriode.api.player.IHyriPlayerManager;
import fr.hyriode.api.player.IHyriPlayerSession;
import fr.hyriode.api.player.event.PlayerQuitNetworkEvent;
import fr.hyriode.api.player.nickname.IHyriNickname;
import net.md_5.bungee.Util;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.UUID;
import java.util.regex.Pattern;

/**
 * Project: HyriAPI
 * Created by AstFaster
 * on 13/04/2022 at 19:13
 */
public class PlayerLoader {

    private static final Pattern NOTCHIAN_NAME_PATTERN = Pattern.compile("^[a-zA-Z0-9_]{3,16}$");
    private static final String PROFILES_CACHE_KEY = "proxy-mojang-profiles:";

    public boolean isNameValid(String name) {
        return NOTCHIAN_NAME_PATTERN.matcher(name).find();
    }

    public MojangProfile fetchMojangProfile(String playerName) {
        final String key = PROFILES_CACHE_KEY + playerName.toLowerCase();
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

            if (statusCode == 200 || statusCode == 204 || statusCode == 404) {
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

    public void loadPlayerAccount(UUID playerId, IHyriPlayer account, String name) {
        final long loginTime = System.currentTimeMillis();
        final IHyriPlayerManager playerManager = HyriAPI.get().getPlayerManager();
        final IHyriPlayerSession session = new HyriPlayerSession(playerId, loginTime);

        session.setProxy(HyriAPI.get().getProxy().getName());
        session.update();

        if (account != null) { // Might be null if the player is crack and doesn't have an account yet
            account.setName(name);
            account.setLastLoginDate(loginTime);
            account.update();
        }

        playerManager.setPlayerId(name, playerId);
    }

    public void handleDisconnection(ProxiedPlayer player, boolean login) {
        final UUID playerId = player.getUniqueId();
        final IHyriPlayer account = IHyriPlayer.get(playerId);
        final IHyriPlayerSession session = IHyriPlayerSession.get(playerId);
        final IHyriPlayerManager pm = HyriAPI.get().getPlayerManager();

        if (session != null) {
            final IHyriNickname nickname = session.getNickname();

            if (nickname != null) {
                pm.getNicknameManager().removeUsedNickname(nickname.getName());
            }

            pm.deleteSession(playerId);
        }

        if (!login) { // All this stuff doesn't have to be done if the player has been kicked while logging in
            if (account != null) {
                HyriAPI.get().getQueueManager().removePlayerFromQueue(playerId);

                account.setPlayTime(account.getPlayTime() + (System.currentTimeMillis() - account.getLastLoginDate()));
                account.update();
            }

            HyriAPI.get().getNetworkManager().getEventBus().publishAsync(new PlayerQuitNetworkEvent(playerId, session == null ? null : session.getParty()));
            HyriAPI.get().getProxy().removePlayer(playerId);
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
