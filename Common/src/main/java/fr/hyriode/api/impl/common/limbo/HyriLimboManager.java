package fr.hyriode.api.impl.common.limbo;

import fr.hyriode.api.HyriAPI;
import fr.hyriode.api.hyggdrasil.IHyggdrasilManager;
import fr.hyriode.api.limbo.IHyriLimboManager;
import fr.hyriode.api.packet.HyriChannel;
import fr.hyriode.api.packet.model.PlayerLimboSendPacket;
import fr.hyriode.hyggdrasil.api.limbo.HyggLimbo;
import fr.hyriode.hyggdrasil.api.protocol.data.HyggData;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.function.Consumer;
import java.util.stream.Collectors;

/**
 * Created by AstFaster
 * on 02/01/2023 at 18:08
 */
public class HyriLimboManager implements IHyriLimboManager {

    private final Map<String, HyggLimbo> limbos;

    private final IHyggdrasilManager hyggdrasilManager;

    public HyriLimboManager() {
        this.hyggdrasilManager = HyriAPI.get().getHyggdrasilManager();
        this.limbos = new HashMap<>();

        if (this.hyggdrasilManager.withHyggdrasil()) {
            for (HyggLimbo limbo : this.hyggdrasilManager.getHyggdrasilAPI().getLimbosRequester().fetchLimbos()) {
                this.limbos.put(limbo.getName(), limbo);
            }
        }
    }

    public void addLimbo(HyggLimbo limbo) {
        this.limbos.put(limbo.getName(), limbo);
    }

    public void removeLimbo(String limboName) {
        this.limbos.remove(limboName);
    }

    @Override
    public @NotNull Set<HyggLimbo> getLimbos() {
        return Collections.unmodifiableSet(new HashSet<>(this.limbos.values()));
    }

    @Override
    public @NotNull Set<HyggLimbo> getLimbos(HyggLimbo.Type type) {
        return Collections.unmodifiableSet(this.limbos.values().stream().filter(limbo -> limbo.getType() == type).collect(Collectors.toSet()));
    }

    @Override
    public @Nullable HyggLimbo getLimbo(@NotNull String name) {
        return this.limbos.get(name);
    }

    @Override
    public @Nullable HyggLimbo getBestLimbo(HyggLimbo.Type type) {
        return this.limbos.values().stream().filter(limbo -> limbo.getType() == type).min(Comparator.comparingInt(limbo -> limbo.getPlayers().size())).orElse(null); // Returns the limbo with the lowest amount of players
    }

    @Override
    public void sendPlayerToLimbo(@NotNull UUID playerId, @NotNull String limboName) {
        HyriAPI.get().getPubSub().send(HyriChannel.PROXIES, new PlayerLimboSendPacket(playerId, limboName));
    }

    @Override
    public void createLimbo(@NotNull HyggLimbo.Type type, @NotNull HyggData data, @NotNull Consumer<HyggLimbo> onCreated) {
        this.hyggdrasilManager.getHyggdrasilAPI().getLimbosRequester().createLimbo(type, data, onCreated);
    }

    @Override
    public void removeLimbo(@NotNull String limboName, Runnable onRemoved) {
        this.hyggdrasilManager.getHyggdrasilAPI().getLimbosRequester().removeLimbo(limboName, onRemoved);
    }

}
