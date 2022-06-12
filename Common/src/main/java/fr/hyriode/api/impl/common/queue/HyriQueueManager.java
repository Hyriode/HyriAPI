package fr.hyriode.api.impl.common.queue;

import fr.hyriode.api.HyriAPI;
import fr.hyriode.api.impl.common.hyggdrasil.HyggdrasilManager;
import fr.hyriode.api.party.IHyriParty;
import fr.hyriode.api.player.IHyriPlayer;
import fr.hyriode.api.player.IHyriPlayerManager;
import fr.hyriode.api.queue.IHyriQueueHandler;
import fr.hyriode.api.queue.IHyriQueueManager;
import fr.hyriode.hyggdrasil.api.protocol.HyggChannel;
import fr.hyriode.hyggdrasil.api.protocol.packet.HyggPacket;
import fr.hyriode.hyggdrasil.api.protocol.response.content.HyggResponseContent;
import fr.hyriode.hyggdrasil.api.queue.HyggQueueGroup;
import fr.hyriode.hyggdrasil.api.queue.HyggQueuePlayer;
import fr.hyriode.hyggdrasil.api.queue.packet.HyggQueueAddPacket;
import fr.hyriode.hyggdrasil.api.queue.packet.HyggQueueInfoPacket;
import fr.hyriode.hyggdrasil.api.queue.packet.HyggQueueRemovePacket;
import fr.hyriode.hyggdrasil.api.queue.packet.group.HyggQueueAddGroupPacket;
import fr.hyriode.hyggdrasil.api.queue.packet.group.HyggQueueRemoveGroupPacket;
import fr.hyriode.hyggdrasil.api.queue.packet.group.HyggQueueUpdateGroupPacket;
import fr.hyriode.hyggdrasil.api.queue.packet.player.HyggQueueAddPlayerPacket;
import fr.hyriode.hyggdrasil.api.queue.packet.player.HyggQueueRemovePlayerPacket;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.function.Consumer;

/**
 * Project: HyriAPI
 * Created by AstFaster
 * on 19/04/2022 at 11:18
 */
public class HyriQueueManager implements IHyriQueueManager {

    private final List<IHyriQueueHandler> handlers;

    private final HyggdrasilManager hyggdrasilManager;

    public HyriQueueManager(HyggdrasilManager hyggdrasilManager) {
        this.hyggdrasilManager = hyggdrasilManager;
        this.handlers = new ArrayList<>();
    }

    public void start() {
        if (this.hyggdrasilManager.withHyggdrasil()) {
            this.hyggdrasilManager.getHyggdrasilAPI().getPacketProcessor().registerReceiver(HyggChannel.QUEUE, new HyriQueueReceiver(this));
        }
    }

    void onQueueInfo(HyggQueueInfoPacket info) {
        for (IHyriQueueHandler handler : this.handlers) {
            handler.onQueueInfo(info);
        }
    }

    @Override
    public void addHandler(IHyriQueueHandler handler) {
        this.handlers.add(handler);
    }

    @Override
    public void removeHandler(IHyriQueueHandler handler) {
        this.handlers.remove(handler);
    }

    @Override
    public boolean addPlayerInQueueWithPartyCheck(UUID playerId, String game, String gameType) {
        final IHyriPlayer account = HyriAPI.get().getPlayerManager().getPlayer(playerId);

        if (account.hasParty()) {
            final IHyriParty party = HyriAPI.get().getPartyManager().getPlayerParty(playerId);

            if (party.isLeader(playerId)) {
                this.addPartyInQueue(party, game, gameType);
                return true;
            }
            return false;
        } else {
            this.addPlayerInQueue(playerId, game, gameType);
        }
        return true;
    }

    @Override
    public void addPlayerInQueue(UUID playerId, String game, String gameType) {
        this.addPlayerInQueue(playerId, game, gameType, null);
    }

    @Override
    public void addPlayerInQueue(UUID playerId, String game, String gameType, String map) {
        final IHyriPlayer player = HyriAPI.get().getPlayerManager().getPlayer(playerId);

        if (player != null) {
            this.sendQueuePacket(new HyggQueueAddPlayerPacket(new HyggQueuePlayer(playerId, player.getPriority()), game, gameType, map), content -> {
                if (content instanceof HyggQueueAddPacket.Response) {
                    for (IHyriQueueHandler handler : this.handlers) {
                        handler.onPlayerAddResponse((HyggQueueAddPacket.Response) content);
                    }
                }
            });
        }
    }

    @Override
    public void removePlayerFromQueue(UUID playerId) {
        this.sendQueuePacket(new HyggQueueRemovePlayerPacket(playerId), content -> {
            if (content instanceof HyggQueueRemovePacket.Response) {
                for (IHyriQueueHandler handler : this.handlers) {
                    handler.onPlayerRemoveResponse((HyggQueueRemovePacket.Response) content);
                }
            }
        });
    }

    @Override
    public void addPartyInQueue(IHyriParty party, String game, String gameType) {
        this.addPartyInQueue(party, game, gameType, null);
    }

    @Override
    public void addPartyInQueue(IHyriParty party, String game, String gameType, String map) {
        this.sendQueuePacket(new HyggQueueAddGroupPacket(this.createGroupFromParty(party), game, gameType, map), content -> {
            if (content instanceof HyggQueueAddPacket.Response) {
                for (IHyriQueueHandler handler : this.handlers) {
                    handler.onPartyAddResponse((HyggQueueAddPacket.Response) content);
                }
            }
        });
    }

    @Override
    public void removePartyFromQueue(UUID partyId) {
        this.sendQueuePacket(new HyggQueueRemoveGroupPacket(partyId), content -> {
            if (content instanceof HyggQueueRemovePacket.Response) {
                for (IHyriQueueHandler handler : this.handlers) {
                    handler.onPartyRemoveResponse((HyggQueueRemovePacket.Response) content);
                }
            }
        });
    }

    @Override
    public void updatePartyInQueue(IHyriParty party) {
        this.sendQueuePacket(new HyggQueueUpdateGroupPacket(this.createGroupFromParty(party)), content -> {
            if (content instanceof HyggQueueUpdateGroupPacket.Response) {
                for (IHyriQueueHandler handler : this.handlers) {
                    handler.onPartyUpdateResponse((HyggQueueUpdateGroupPacket.Response) content);
                }
            }
        });
    }

    private HyggQueueGroup createGroupFromParty(IHyriParty party) {
        final UUID leader = party.getLeader();
        final IHyriPlayerManager playerManager = HyriAPI.get().getPlayerManager();
        final HyggQueuePlayer queueLeader = new HyggQueuePlayer(leader, playerManager.getPlayer(leader).getPriority());
        final List<HyggQueuePlayer> players = new ArrayList<>();

        for (UUID member : party.getMembers().keySet()) {
            if (party.isLeader(member)) {
                continue;
            }

            players.add(new HyggQueuePlayer(member, playerManager.getPlayer(member).getPriority()));
        }

        return new HyggQueueGroup(party.getId(), queueLeader, players);
    }

    private void sendQueuePacket(HyggPacket packet, Consumer<HyggResponseContent> contentConsumer) {
        if (this.hyggdrasilManager.withHyggdrasil()) {
            this.hyggdrasilManager.getHyggdrasilAPI().getPacketProcessor().request(HyggChannel.QUEUE, packet)
                    .withResponseCallback(response -> {
                        final HyggResponseContent content = response.getContent();

                        if (content != null) {
                            contentConsumer.accept(content);
                        }
                    })
                    .exec();
        }
    }

}