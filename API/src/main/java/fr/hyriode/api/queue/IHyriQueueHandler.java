package fr.hyriode.api.queue;

import fr.hyriode.hyggdrasil.api.queue.packet.HyggQueueAddPacket;
import fr.hyriode.hyggdrasil.api.queue.packet.HyggQueueInfoPacket;
import fr.hyriode.hyggdrasil.api.queue.packet.HyggQueueRemovePacket;
import fr.hyriode.hyggdrasil.api.queue.packet.HyggQueueUpdateGroupPacket;

/**
 * Project: HyriAPI
 * Created by AstFaster
 * on 19/04/2022 at 11:19
 */
public interface IHyriQueueHandler {

    default void onQueueInfo(HyggQueueInfoPacket infoPacket) {}

    default void onPlayerAddResponse(HyggQueueAddPacket.Response response) {}

    default void onPlayerRemoveResponse(HyggQueueRemovePacket.Response response) {}

    default void onPartyAddResponse(HyggQueueAddPacket.Response response) {}

    default void onPartyRemoveResponse(HyggQueueRemovePacket.Response response) {}

    default void onPartyUpdateResponse(HyggQueueUpdateGroupPacket.Response response) {}

}
