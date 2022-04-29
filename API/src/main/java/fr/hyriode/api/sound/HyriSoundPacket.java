package fr.hyriode.api.sound;

import fr.hyriode.api.HyriAPI;
import fr.hyriode.api.packet.HyriPacket;

import java.util.UUID;

/**
 * Project: HyriAPI
 * Created by AstFaster
 * on 29/04/2022 at 18:15
 */
public class HyriSoundPacket extends HyriPacket {

    public static final String CHANNEL = "sounds";

    private final UUID target;
    private final HyriSound sound;
    private final float volume;
    private final float pitch;

    public HyriSoundPacket(UUID target, HyriSound sound, float volume, float pitch) {
        this.target = target;
        this.sound = sound;
        this.volume = volume;
        this.pitch = pitch;
    }

    public UUID getTarget() {
        return this.target;
    }

    public HyriSound getSound() {
        return this.sound;
    }

    public float getVolume() {
        return this.volume;
    }

    public float getPitch() {
        return this.pitch;
    }

    public static void send(UUID target, HyriSound sound, float volume, float pitch) {
        HyriAPI.get().getPubSub().send(CHANNEL, new HyriSoundPacket(target, sound, volume, pitch));
    }

}
