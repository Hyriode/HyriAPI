package fr.hyriode.api.impl.server.receiver;

import fr.hyriode.api.packet.HyriPacket;
import fr.hyriode.api.packet.IHyriPacketReceiver;
import fr.hyriode.api.sound.HyriSoundPacket;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

/**
 * Project: HyriAPI
 * Created by AstFaster
 * on 29/04/2022 at 18:18
 */
public class SoundReceiver implements IHyriPacketReceiver {

    @Override
    public void receive(String channel, HyriPacket packet) {
        if (packet instanceof HyriSoundPacket) {
            final HyriSoundPacket soundPacket = (HyriSoundPacket) packet;
            final Player player = Bukkit.getPlayer(soundPacket.getTarget());

            if (player != null) {
                player.playSound(player.getLocation(), Sound.valueOf(soundPacket.getSound().name()), soundPacket.getVolume(), soundPacket.getPitch());
            }
        }
    }

}
