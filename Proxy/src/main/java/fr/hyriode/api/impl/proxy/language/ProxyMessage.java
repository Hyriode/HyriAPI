package fr.hyriode.api.impl.proxy.language;

import fr.hyriode.api.HyriConstants;
import fr.hyriode.api.language.HyriLanguage;
import fr.hyriode.api.language.HyriLanguageMessage;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.TextComponent;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by AstFaster
 * on 15/04/2023 at 10:21
 */
public enum ProxyMessage {

    SUPPORT("line.support"),
    STORE("line.store"),

    MAINTENANCE("message.maintenance"),
    SERVER_FULL("message.server-full"),
    NO_SERVER("message.no-server"),
    INVALID_NAME("message.invalid-name"),
    PROFILE_LOADING_ERROR("message.profile-loading-error"),
    PROFILE_TAKEN("message.profile-taken"),
    ALREADY_ONLINE("message.already-online"),
    IP_LIMIT("message.ip-limit")

    ;

    private static final String HYPHENS = "----------------------";

    private HyriLanguageMessage message;

    private final String key;

    ProxyMessage(String key) {
        this.key = key;
    }

    public String getKey() {
        return this.key;
    }

    public HyriLanguageMessage asLang() {
        return this.message == null ? this.message = HyriLanguageMessage.get(this.key) : this.message;
    }

    public <T> String asString(T object) {
        return this.asLang().getValue(object);
    }

    public <T> List<String> asList(T object) {
        return new ArrayList<>(Arrays.asList(this.asString(object).split("\n")));
    }

    public <T> BaseComponent[] asComponents(T object) {
        return TextComponent.fromLegacyText(this.asString(object));
    }

    public <T> BaseComponent[] asFramedComponents(T object, boolean separateSupport) {
        return new ComponentBuilder(HYPHENS).color(ChatColor.DARK_AQUA).strikethrough(true)
                .append(" Hyriode ").reset().color(ChatColor.AQUA)
                .append(HYPHENS).color(ChatColor.DARK_AQUA).strikethrough(true)
                .append("\n\n")
                .reset()
                .append(object == null ? this.asLang().getValue(HyriLanguage.FR) : this.asString(object))
                .append(separateSupport ? "\n\n" : "\n")
                .reset()
                .append((object == null ? ProxyMessage.SUPPORT.asLang().getValue(HyriLanguage.FR) : ProxyMessage.SUPPORT.asString(object)).replace("%support%", HyriConstants.DISCORD_URL))
                .append("\n\n")
                .append("----------------------------------------------------").color(ChatColor.DARK_AQUA).strikethrough(true)
                .create();
    }

}
