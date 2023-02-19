package fr.hyriode.api.impl.common.game;

import fr.hyriode.api.HyriAPI;
import fr.hyriode.api.game.IHyriGameInfo;
import fr.hyriode.api.game.IHyriGameManager;
import fr.hyriode.api.game.rotating.IHyriRotatingGameManager;
import fr.hyriode.api.impl.common.game.rotating.HyriRotatingGameManager;
import fr.hyriode.hystia.api.world.IWorldManager;
import redis.clients.jedis.Pipeline;
import redis.clients.jedis.Response;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Project: HyriAPI
 * Created by AstFaster
 * on 17/04/2022 at 21:02
 */
public class HyriGameManager implements IHyriGameManager {

    private static final String KEY = "games:";

    private final IHyriRotatingGameManager rotatingGameManager;
    private final IWorldManager worldManager;

    public HyriGameManager() {
        this.rotatingGameManager = new HyriRotatingGameManager();
//        this.worldManager = HyriAPI.get().getHystiaAPI().getWorldManager(); TODO
        this.worldManager = null;
    }

    @Override
    public IHyriGameInfo createGameInfo(String name, String displayName) {
        return new HyriGameInfo(name, displayName);
    }

    @Override
    public IHyriGameInfo getGameInfo(String name) {
        return HyriAPI.get().getRedisProcessor().get(jedis -> {
            final byte[] bytes =  jedis.get((KEY + name).getBytes(StandardCharsets.UTF_8));

            return bytes == null ? null : HyriAPI.get().getDataSerializer().deserialize(new HyriGameInfo(), bytes);
        });
    }

    @Override
    public void saveGameInfo(IHyriGameInfo game) {
        HyriAPI.get().getRedisProcessor().process(jedis -> jedis.set((KEY + game.getName()).getBytes(StandardCharsets.UTF_8), HyriAPI.get().getDataSerializer().serialize((HyriGameInfo) game)));
    }

    @Override
    public void deleteGameInfo(String name) {
        HyriAPI.get().getRedisProcessor().process(jedis -> jedis.del(KEY + name));
    }

    @Override
    public List<IHyriGameInfo> getGamesInfo() {
        return HyriAPI.get().getRedisProcessor().get(jedis -> {
            final Set<String> keys = jedis.keys(KEY + "*");
            final List<Response<byte[]>> responses = new ArrayList<>();

            final Pipeline pipeline = jedis.pipelined();

            for (String key : keys) {
                responses.add(pipeline.get(key.getBytes(StandardCharsets.UTF_8)));
            }

            pipeline.sync();

            return responses.stream().map(response -> HyriAPI.get().getDataSerializer().deserialize(new HyriGameInfo(), response.get())).collect(Collectors.toList());
        });
    }

    @Override
    public IHyriRotatingGameManager getRotatingGameManager() {
        return this.rotatingGameManager;
    }

}
