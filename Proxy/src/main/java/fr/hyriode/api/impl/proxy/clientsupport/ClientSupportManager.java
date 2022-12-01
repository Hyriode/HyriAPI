package fr.hyriode.api.impl.proxy.clientsupport;

import fr.hyriode.api.HyriAPI;

import java.util.ArrayList;
import java.util.List;

/**
 * Project: HyriAPI
 * Created by AstFaster
 * on 11/05/2022 at 14:24
 */
public class ClientSupportManager {

    private final List<ClientSupport> supports = new ArrayList<>();

    public void registerSupport(ClientSupport support) {
        final String supportName = support.getClientName();

        if (this.getSupport(supportName) != null) {
            throw new IllegalStateException("'" + supportName + "' client support already exists and can't be registered twice!");
        }

        this.supports.add(support);

        HyriAPI.get().log("Registered '" + supportName + "' client support.");
    }

    public boolean unregisterSupport(String name) {
        final ClientSupport support = this.getSupport(name);

        if (support != null) {
            this.supports.remove(support);
            return true;
        }
        return false;
    }

    private <T extends ClientSupport> T getSupport(Class<T> clazz) {
        for (ClientSupport support : this.supports) {
            if (support.getClass() == clazz) {
                return clazz.cast(support);
            }
        }
        return null;
    }

    private ClientSupport getSupport(String name) {
        for (ClientSupport support : this.supports) {
            if (support.getClientName().equals(name)) {
                return support;
            }
        }
        return null;
    }

}
