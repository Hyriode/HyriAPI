package fr.hyriode.api.impl.common.player.title;

import fr.hyriode.api.packet.HyriPacket;

/**
 * Project: HyriAPI
 * Created by AstFaster
 * on 21/04/2022 at 09:27
 */
public class TitlePacket extends HyriPacket {

    protected final String title;
    protected final String subtitle;
    protected final int fadeIn;
    protected final int stay;
    protected final int fadeOut;

    public TitlePacket(String title, String subtitle, int fadeIn, int stay, int fadeOut) {
        this.title = title;
        this.subtitle = subtitle;
        this.fadeIn = fadeIn;
        this.stay = stay;
        this.fadeOut = fadeOut;
    }

    public String getTitle() {
        return this.title;
    }

    public String getSubtitle() {
        return this.subtitle;
    }

    public int getFadeIn() {
        return this.fadeIn;
    }

    public int getStay() {
        return this.stay;
    }

    public int getFadeOut() {
        return this.fadeOut;
    }

}
