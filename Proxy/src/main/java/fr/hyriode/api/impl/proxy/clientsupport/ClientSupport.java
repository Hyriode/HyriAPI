package fr.hyriode.api.impl.proxy.clientsupport;

/**
 * Project: HyriAPI
 * Created by AstFaster
 * on 11/05/2022 at 14:13
 */
public abstract class ClientSupport {

    private final String clientName;

    public ClientSupport(String clientName) {
        this.clientName = clientName;
    }

    public String getClientName() {
        return this.clientName;
    }

}
