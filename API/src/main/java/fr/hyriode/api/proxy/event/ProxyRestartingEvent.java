package fr.hyriode.api.proxy.event;

import fr.hyriode.api.event.HyriEvent;

/**
 * Created by AstFaster
 * on 15/04/2023 at 11:27.<br>
 *
 * This event is triggered when a proxy will restart (maybe to deploy an update)
 */
public class ProxyRestartingEvent extends HyriEvent {

    /** The name of the proxy that will restart */
    private final String proxy;
    /** The count before the proxies restart */
    private final int count;

    public ProxyRestartingEvent(String proxy, int count) {
        this.proxy = proxy;
        this.count = count;
    }

    public String getProxy() {
        return this.proxy;
    }

    public int getCount() {
        return this.count;
    }

}
