package fr.hyriode.api.chat;

public enum HyriDefaultChatChannel {

    PLUGIN,
    PRIVATE,
    GLOBAL,
    STAFF,
    PARTNER;

    private final String channel;

    HyriDefaultChatChannel() {
        this.channel = name().toLowerCase();
    }

    public String getChannel() {
        return this.channel;
    }

    public static HyriDefaultChatChannel getByChannel(String channel) {
        for (HyriDefaultChatChannel chat : HyriDefaultChatChannel.values()) {
            if (chat.getChannel().equals(channel.toLowerCase())) {
                return chat;
            }
        }
        return null;
    }
}
