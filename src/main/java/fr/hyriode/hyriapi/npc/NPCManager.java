package fr.hyriode.hyriapi.npc;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import fr.hyriode.hyriapi.HyriAPI;
import fr.hyriode.hyriapi.util.PacketUtil;
import net.minecraft.server.v1_8_R3.Packet;
import net.minecraft.server.v1_8_R3.PacketPlayOutPlayerInfo;
import net.minecraft.server.v1_8_R3.World;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_8_R3.CraftWorld;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.*;

public class NPCManager {

    private static final Set<NPC> allNpc = new HashSet<>();

    public static NPC createNPC(String name, Location location) {
        final World world = ((CraftWorld) location.getWorld()).getHandle();
        final GameProfile gameProfile = new GameProfile(UUID.randomUUID(), name);
        final NPC npc = new NPC(gameProfile.getName(), location, world, gameProfile);

        if (!existsNPC(npc)) {
            allNpc.add(npc);
        }

        return npc;
    }

    public static void sendNPC(Player player, NPC npc) {
        if (npc.getPlayers().contains(player.getUniqueId()) || npc.getPlayers().isEmpty()) {
            for (Packet<?> packet : npc.getSpawnPackets()) {
                PacketUtil.sendPacket(player, packet);
            }

            new BukkitRunnable() {
                @Override
                public void run() {
                    PacketUtil.sendPacket(player, new PacketPlayOutPlayerInfo(PacketPlayOutPlayerInfo.EnumPlayerInfoAction.REMOVE_PLAYER, npc));
                }
            }.runTaskLater(HyriAPI.get().getPlugin(), 20L);
        }
    }

    public static void sendNPC(NPC npc) {
        for (Player player : Bukkit.getOnlinePlayers()) {
            sendNPC(player, npc);
        }
    }

    public static void removeNPC(Player player, NPC npc) {
        for (Packet<?> packet : npc.getDestroyPackets()) {
            PacketUtil.sendPacket(player, packet);
        }
    }

    public static void removeNPC(NPC npc) {
        for (Player player : Bukkit.getOnlinePlayers()) {
            removeNPC(player, npc);
        }
    }

    public static void sendMetadataNPC(Player player, NPC npc) {
        PacketUtil.sendPacket(player, npc.getMetadataPacket());
    }

    public static void sendMetadataNPC(NPC npc) {
        for (Player player : Bukkit.getOnlinePlayers()) {
            sendMetadataNPC(player, npc);
        }
    }

    public static void setSkinNPC(NPC npc, String owner) {
        removeNPC(npc);

        try {
            final URL mojangProfileUrl = new URL("https://api.mojang.com/users/profiles/minecraft/" + owner);
            final InputStreamReader mojangProfileReader = new InputStreamReader(mojangProfileUrl.openStream());
            final String uuid = new JsonParser().parse(mojangProfileReader).getAsJsonObject().get("id").getAsString();
            final URL mojangSessionUrl = new URL("https://sessionserver.mojang.com/session/minecraft/profile/" + uuid + "?unsigned=false");
            final InputStreamReader mojangSessionReader = new InputStreamReader(mojangSessionUrl.openStream());
            final JsonObject property = new JsonParser().parse(mojangSessionReader).getAsJsonObject().get("properties").getAsJsonArray().get(0).getAsJsonObject();
            final String texture = property.get("value").getAsString();
            final String signature = property.get("signature").getAsString();
            final GameProfile profile = npc.getProfile();

            profile.getProperties().put("textures", new Property("textures", texture, signature));
        } catch (IOException e) {
            e.printStackTrace();
        }

        npc.getDataWatcher().watch(10, (byte) 127);

        sendMetadataNPC(npc);
        sendNPC(npc);
    }

    public static NPC getNPC(String name) {
        for (NPC npc : allNpc) {
            if (npc.getName().equals(name)) {
                return npc;
            }
        }
        return null;
    }

    public static boolean hasNPCWithName(String name) {
        for (NPC npc : allNpc) {
            if (npc.getName().equals(name)) {
                return true;
            }
        }
        return false;
    }

    public static boolean existsNPC(NPC npc) {
        for (NPC n : allNpc) {
            if (n.getLocation().equals(npc.getLocation()) && n.getName().equals(npc.getName()) && Arrays.equals(n.getEquipment(), npc.getEquipment())) {
                return true;
            }
        }
        return false;
    }

    public static Set<NPC> getAllNpc() {
        return allNpc;
    }
}
