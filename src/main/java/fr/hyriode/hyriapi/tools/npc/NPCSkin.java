package fr.hyriode.hyriapi.tools.npc;

public class NPCSkin {

    private String textureData;
    private String textureSignature;

    public NPCSkin(String textureData, String textureSignature) {
        this.textureData = textureData;
        this.textureSignature = textureSignature;
    }

    public String getTextureData() {
        return this.textureData;
    }

    public void setTextureData(String textureData) {
        this.textureData = textureData;
    }

    public String getTextureSignature() {
        return this.textureSignature;
    }

    public void setTextureSignature(String textureSignature) {
        this.textureSignature = textureSignature;
    }

}
