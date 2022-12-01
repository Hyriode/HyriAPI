package fr.hyriode.api.impl.proxy.receiver;

import fr.hyriode.api.HyriAPI;
import fr.hyriode.api.proxy.IHyriProxy;
import fr.hyriode.hyggdrasil.api.protocol.packet.HyggPacket;
import fr.hyriode.hyggdrasil.api.protocol.packet.HyggPacketHeader;
import fr.hyriode.hyggdrasil.api.protocol.receiver.IHyggPacketReceiver;
import fr.hyriode.hyggdrasil.api.protocol.response.HyggResponse;
import fr.hyriode.hyggdrasil.api.proxy.HyggProxy;
import fr.hyriode.hyggdrasil.api.proxy.packet.HyggStopProxyPacket;

/**
 * Project: HyriAPI
 * Created by AstFaster
 * on 16/02/2022 at 22:13
 */
public class StopReceiver implements IHyggPacketReceiver {

    @Override
    public HyggResponse receive(String channel, HyggPacketHeader header, HyggPacket packet) {
        if (packet instanceof HyggStopProxyPacket) {
            final String proxyName = ((HyggStopProxyPacket) packet).getProxyName();
            final IHyriProxy proxy = HyriAPI.get().getProxy();

            if (proxy.getName().equals(proxyName)) {
                proxy.setState(HyggProxy.State.SHUTDOWN);

                return HyggResponse.Type.SUCCESS.toResponse();
            }
        }
        return HyggResponse.Type.NONE.toResponse();
    }

}
