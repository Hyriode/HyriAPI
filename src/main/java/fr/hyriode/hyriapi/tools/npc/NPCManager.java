package fr.hyriode.hyriapi.tools.npc;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import fr.hyriode.hyriapi.HyriAPI;
import fr.hyriode.hyriapi.tools.hologram.Hologram;
import fr.hyriode.hyriapi.tools.util.PacketUtil;
import net.minecraft.server.v1_8_R3.Packet;
import net.minecraft.server.v1_8_R3.PacketPlayOutPlayerInfo;
import net.minecraft.server.v1_8_R3.World;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_8_R3.CraftWorld;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scoreboard.NameTagVisibility;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.function.Consumer;

public class NPCManager {

    private final JavaPlugin plugin;

    private final Map<NPC, Hologram> allNpc;

    public NPCManager(JavaPlugin plugin) {
        this.plugin = plugin;
        this.allNpc = new HashMap<>();
    }

    public NPC createNPC(Location location, NPCSkin skin, String[] hologramLines) {
        final GameProfile gameProfile = new GameProfile(UUID.randomUUID(), "[NPC] " + allNpc.size());

        gameProfile.getProperties().put("textures", new Property("textures", skin.getTextureData(), skin.getTextureSignature()));

        return this.createNPC(location, gameProfile, hologramLines);
    }

    public NPC createNPC(Location location, String skinOwner, String[] hologramLines) {
        final GameProfile gameProfile = new NPCProfileLoader("[NPC] " + allNpc.size(), skinOwner).loadProfile(HyriAPI.get().getJedisResource());

        return this.createNPC(location, gameProfile, hologramLines);
    }

    private NPC createNPC(Location location, GameProfile gameProfile, String[] hologramLines) {
        final World world = ((CraftWorld) location.getWorld()).getHandle();
        final NPC npc = new NPC(this.plugin, location, world, gameProfile);
        final Scoreboard scoreboard = Bukkit.getServer().getScoreboardManager().getMainScoreboard();

        Team npcTeam = null;
        for (Team team : scoreboard.getTeams()) {
            if (team.getName().equals("NPC")) {
                npcTeam = team;
            }
        }

        if (npcTeam == null) {
            npcTeam = scoreboard.registerNewTeam("NPC");
        }

        npcTeam.setNameTagVisibility(NameTagVisibility.NEVER);
        npcTeam.addEntry(npc.getName());

        npc.getDataWatcher().watch(10, (byte) 127);

        this.sendMetadataNPC(npc);

        if (hologramLines != null) {
            final Hologram hologram = new Hologram(this.plugin, hologramLines);

            hologram.setLocation(npc.getLocation().clone().add(0.0D, 1.8D, 0.0D));
            hologram.generateLines();

            npc.setHologram(hologram);
        }

        if (!existsNPC(npc)) {
            this.allNpc.put(npc, npc.getHologram());
        }

        return npc;
    }

    public void sendNPC(Player player, NPC npc) {
        final Consumer<Player> sendConsumer = p -> {
            for (Packet<?> packet : npc.getSpawnPackets()) {
                PacketUtil.sendPacket(player, packet);
            }

            npc.addPlayer(p);

            new BukkitRunnable() {
                @Override
                public void run() {
                    PacketUtil.sendPacket(player, new PacketPlayOutPlayerInfo(PacketPlayOutPlayerInfo.EnumPlayerInfoAction.REMOVE_PLAYER, npc));
                }
            }.runTaskLater(this.plugin, 20L);
        };

        if (npc.isShowingToAll()) {
            sendConsumer.accept(player);
        } else {
            if (npc.getPlayers().contains(player)) {
                sendConsumer.accept(player);
            }
        }
    }

    public void sendNPC(NPC npc) {
        for (Player player : Bukkit.getOnlinePlayers()) {
            this.sendNPC(player, npc);
        }
    }

    public void removeNPC(Player player, NPC npc) {
        for (Packet<?> packet : npc.getDestroyPackets()) {
            PacketUtil.sendPacket(player, packet);
        }
    }

    public void removeNPC(NPC npc) {
        for (Player player : Bukkit.getOnlinePlayers()) {
            this.removeNPC(player, npc);
        }
    }

    public void sendMetadataNPC(Player player, NPC npc) {
        PacketUtil.sendPacket(player, npc.getMetadataPacket());
    }

    public void sendMetadataNPC(NPC npc) {
        for (Player player : Bukkit.getOnlinePlayers()) {
            this.sendMetadataNPC(player, npc);
        }
    }

    public void setSkinNPC(NPC npc, String owner) {
        this.removeNPC(npc);

        final GameProfile profile = new NPCProfileLoader(npc.getName(), owner).loadProfile(HyriAPI.get().getJedisResource());

        npc.getProfile().getProperties().putAll(profile.getProperties());

        npc.getDataWatcher().watch(10, (byte) 127);

        this.sendMetadataNPC(npc);
        this.sendNPC(npc);
    }

    public NPC getNPC(String name) {
        for (NPC npc : this.allNpc.keySet()) {
            if (npc.getName().equals(name)) {
                return npc;
            }
        }
        return null;
    }

    public boolean hasNPCWithName(String name) {
        for (NPC npc : this.allNpc.keySet()) {
            if (npc.getName().equals(name)) {
                return true;
            }
        }
        return false;
    }

    public boolean existsNPC(NPC npc) {
        for (NPC n : this.allNpc.keySet()) {
            if (n.getLocation().equals(npc.getLocation()) && n.getHologram().getLines().equals(npc.getHologram().getLines()) && Arrays.equals(n.getEquipment(), npc.getEquipment())) {
                return true;
            }
        }
        return false;
    }

    public Map<NPC, Hologram> getAllNpc() {
        return this.allNpc;
    }
}
