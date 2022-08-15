package fr.hyriode.api.impl.common.game;

import com.google.gson.reflect.TypeToken;
import fr.hyriode.api.HyriAPI;
import fr.hyriode.api.game.IHyriGameInfo;
import fr.hyriode.api.game.IHyriGameManager;
import fr.hyriode.api.game.rotating.IHyriRotatingGameManager;
import fr.hyriode.api.impl.common.game.rotating.HyriRotatingGameManager;
import fr.hyriode.hystia.api.world.IWorldManager;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.function.BiFunction;
import java.util.function.Function;

/**
 * Project: HyriAPI
 * Created by AstFaster
 * on 17/04/2022 at 21:02
 */
public class HyriGameManager implements IHyriGameManager {

    private static final String MAPS_KEY = "games-maps:";
    private static final BiFunction<String, String, String> MAPS_KEY_FORMATTER = (game, gameType) -> MAPS_KEY + game + ":" + gameType;

    private static final String KEY = "games:";
    private static final Function<String, String> KEY_FORMATTER = name -> KEY + name;

    private final IHyriRotatingGameManager rotatingGameManager;
    private final IWorldManager worldManager;

    public HyriGameManager() {
        this.rotatingGameManager = new HyriRotatingGameManager();
        this.worldManager = HyriAPI.get().getHystiaAPI().getWorldManager();
    }

    @Override
    public IHyriGameInfo createGameInfo(String name, String displayName) {
        return new HyriGameInfo(name, displayName);
    }

    @Override
    public IHyriGameInfo getGameInfo(String name) {
        return HyriAPI.get().getRedisProcessor().get(jedis -> HyriAPI.GSON.fromJson(jedis.get(KEY_FORMATTER.apply(name)), HyriGameInfo.class));
    }

    @Override
    public void saveGameInfo(IHyriGameInfo game) {
        HyriAPI.get().getRedisProcessor().process(jedis -> jedis.set(KEY_FORMATTER.apply(game.getName()), HyriAPI.GSON.toJson(game)));
    }

    @Override
    public void deleteGameInfo(String gameName) {
        HyriAPI.get().getRedisProcessor().process(jedis -> jedis.del(KEY_FORMATTER.apply(gameName)));
    }

    @Override
    public List<IHyriGameInfo> getGamesInfo() {
        final Set<String> keys = HyriAPI.get().getRedisProcessor().get(jedis -> jedis.keys(KEY + "*"));

        if (keys != null && keys.size() > 0) {
            return HyriAPI.get().getRedisProcessor().get(jedis -> {
                final List<IHyriGameInfo> gamesInfo = new ArrayList<>();

                for (String key : keys) {
                    final String json = jedis.get(key);

                    if (json != null) {
                        final IHyriGameInfo gameInfo = HyriAPI.GSON.fromJson(json, HyriGameInfo.class);

                        gamesInfo.add(gameInfo);
                    }
                }
                return gamesInfo;
            });
        }
        return null;
    }

    @Override
    public List<String> getMaps(String game, String gameType) {
        final String key = MAPS_KEY_FORMATTER.apply(game, gameType);
        final String json = HyriAPI.get().getRedisProcessor().get(jedis -> jedis.get(key));

        if (json != null) {
            return HyriAPI.GSON.fromJson(json, new TypeToken<List<String>>() {}.getType());
        }

        final List<String> maps = this.worldManager.getWorlds(game, gameType);

        HyriAPI.get().getRedisProcessor().process(jedis -> {
            jedis.set(key, HyriAPI.GSON.toJson(maps));
            jedis.expire(key, 120);
        });

        return maps;
    }

    @Override
    public IHyriRotatingGameManager getRotatingGameManager() {
        return this.rotatingGameManager;
    }

}
