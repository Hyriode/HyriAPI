package fr.hyriode.api.chat;

public enum HyriChatChannel {

    GLOBAL,
    PARTY,
    STAFF,
    PARTNER;

    private final String channel;

    HyriChatChannel() {
        this.channel = name().toLowerCase();
    }

    public String getChannel() {
        return this.channel;
    }

    public static HyriChatChannel getByChannel(String channel) {
        for (HyriChatChannel chat : HyriChatChannel.values()) {
            if (chat.getChannel().equals(channel.toLowerCase())) {
                return chat;
            }
        }
        return null;
    }
}
