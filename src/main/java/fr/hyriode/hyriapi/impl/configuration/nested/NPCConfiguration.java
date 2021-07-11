package fr.hyriode.hyriapi.impl.configuration.nested;

public class NPCConfiguration {

    private String world;
    private double x;
    private double y;
    private double z;
    private float yaw;
    private float pitch;
    private String textureData;
    private String textureSignature;

    public NPCConfiguration(String world, double x, double y, double z, float yaw, float pitch, String textureData, String textureSignature) {
        this.world = world;
        this.x = x;
        this.y = y;
        this.z = z;
        this.yaw = yaw;
        this.pitch = pitch;
        this.textureData = textureData;
        this.textureSignature = textureSignature;
    }

    public String getWorld() {
        return this.world;
    }

    public void setWorld(String world) {
        this.world = world;
    }

    public double getX() {
        return this.x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getY() {
        return this.y;
    }

    public void setY(double y) {
        this.y = y;
    }

    public double getZ() {
        return this.z;
    }

    public void setZ(double z) {
        this.z = z;
    }

    public float getYaw() {
        return this.yaw;
    }

    public void setYaw(float yaw) {
        this.yaw = yaw;
    }

    public float getPitch() {
        return this.pitch;
    }

    public void setPitch(float pitch) {
        this.pitch = pitch;
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
