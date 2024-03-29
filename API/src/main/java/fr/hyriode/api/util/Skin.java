package fr.hyriode.api.util;

public class Skin {

    /** Texture's data */
    private String textureData;
    /** Texture's signature */
    private String textureSignature;

    /**
     * Constructor of {@link Skin}
     *
     * @param textureData Texture's data
     * @param textureSignature Texture's signature
     */
    public Skin(String textureData, String textureSignature) {
        this.textureData = textureData;
        this.textureSignature = textureSignature;
    }

    /**
     * Get skin texture's data
     *
     * @return Texture's data
     */
    public String getTextureData() {
        return this.textureData;
    }

    /**
     * Set skin texture's data
     *
     * @param textureData New texture's data
     */
    public void setTextureData(String textureData) {
        this.textureData = textureData;
    }

    /**
     * Get skin texture's signature
     *
     * @return Texture's data
     */
    public String getTextureSignature() {
        return this.textureSignature;
    }

    /**
     * Set skin texture's data
     *
     * @param textureSignature New texture's data
     */
    public void setTextureSignature(String textureSignature) {
        this.textureSignature = textureSignature;
    }

}
