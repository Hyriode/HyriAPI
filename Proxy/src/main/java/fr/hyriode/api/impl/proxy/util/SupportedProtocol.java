package fr.hyriode.api.impl.proxy.util;

/**
 * Project: HyriAPI
 * Created by AstFaster
 * on 22/03/2022 at 13:48
 */
public enum SupportedProtocol {

    VERSION_1_8(47)
    ;

    private final int protocolNumber;

    SupportedProtocol(int protocolNumber) {
        this.protocolNumber = protocolNumber;
    }

    public int getProtocolNumber() {
        return this.protocolNumber;
    }

    public static boolean isSupported(int protocolNumber) {
        for (SupportedProtocol protocol : values()) {
            if (protocol.getProtocolNumber() == protocolNumber) {
                return true;
            }
        }
        return false;
    }

    public static SupportedProtocol getDefault() {
        return VERSION_1_8;
    }

}
