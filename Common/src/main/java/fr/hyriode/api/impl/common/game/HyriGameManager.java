package fr.hyriode.api.impl.common.game;

import com.google.gson.JsonElement;
import com.google.gson.reflect.TypeToken;
import fr.hyriode.api.HyriAPI;
import fr.hyriode.api.game.IHyriGameInfo;
import fr.hyriode.api.game.IHyriGameManager;
import fr.hyriode.api.impl.common.hydrion.HydrionManager;
import fr.hyriode.hydrion.client.module.ResourcesModule;
import fr.hyriode.hystia.api.IHystiaAPI;
import fr.hyriode.hystia.api.world.IWorldManager;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ExecutionException;
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

    private final HydrionManager hydrionManager;
    private ResourcesModule resourcesModule;
    private IWorldManager worldManager;

    public HyriGameManager(HydrionManager hydrionManager) {
        this.hydrionManager = hydrionManager;

        if (this.hydrionManager.isEnabled()) {
            this.resourcesModule = this.hydrionManager.getClient().getResourcesModule();

            final IHystiaAPI hystiaAPI = HyriAPI.get().getHystiaAPI();

            if (hystiaAPI != null) {
                this.worldManager = hystiaAPI.getWorldManager();
            }
        }
    }

    @Override
    public IHyriGameInfo createGameInfo(String name, String displayName) {
        return new HyriGameInfo(name, displayName);
    }

    @Override
    public IHyriGameInfo getGameInfo(String name) {
        final String json = HyriAPI.get().getRedisProcessor().get(jedis -> jedis.get(KEY_FORMATTER.apply(name)));

        if (json != null) {
            return HyriAPI.GSON.fromJson(json, HyriGameInfo.class);
        }

        if (this.hydrionManager.isEnabled()) {
            try {
                final IHyriGameInfo gameInfo = this.resourcesModule.getGame(name)
                        .thenApply(response -> {
                            final JsonElement content = response.getContent();

                            if (!content.isJsonNull()) {
                                return HyriAPI.GSON.fromJson(content.toString(), HyriGameInfo.class);
                            }
                            return null;
                        }).get();

                HyriAPI.get().getRedisProcessor().process(jedis -> jedis.set(KEY_FORMATTER.apply(name), HyriAPI.GSON.toJson(gameInfo)));

                return gameInfo;
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    @Override
    public void saveGameInfo(IHyriGameInfo game) {
        final String gameName = game.getName();
        final String serialized = HyriAPI.GSON.toJson(game);

        HyriAPI.get().getRedisProcessor().process(jedis -> jedis.set(KEY_FORMATTER.apply(gameName), serialized));

        if (this.hydrionManager.isEnabled()) {
            this.resourcesModule.addGame(gameName, serialized);
        }
    }

    @Override
    public void deleteGameInfo(String gameName) {
        HyriAPI.get().getRedisProcessor().process(jedis -> jedis.del(KEY_FORMATTER.apply(gameName)));

        if (this.hydrionManager.isEnabled()) {
            this.resourcesModule.removeGame(gameName);
        }
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

        if (this.hydrionManager.isEnabled()) {
            try {
                final List<IHyriGameInfo> gamesInfo = this.resourcesModule.getGames().thenApply(response -> {
                    final JsonElement content = response.getContent();

                    if (!content.isJsonNull()) {
                        final List<IHyriGameInfo> all = HyriAPI.GSON.fromJson(content.getAsJsonArray(), new TypeToken<List<HyriGameInfo>>(){}.getType());

                        HyriAPI.get().getRedisProcessor().process(jedis -> {
                            for (IHyriGameInfo info : all) {
                                jedis.set(KEY_FORMATTER.apply(info.getName()), HyriAPI.GSON.toJson(info));
                            }
                        });
                        return all;
                    }
                    return new ArrayList<IHyriGameInfo>();
                }).get();

                HyriAPI.get().getRedisProcessor().processAsync(jedis -> {
                    for (IHyriGameInfo gameInfo : gamesInfo) {
                        jedis.set(KEY_FORMATTER.apply(gameInfo.getName()), HyriAPI.GSON.toJson(gameInfo));
                    }
                });
                return gamesInfo;
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    @Override
    public List<String> getMaps(String game, String gameType) {
        final String json = HyriAPI.get().getRedisProcessor().get(jedis -> jedis.get(MAPS_KEY_FORMATTER.apply(game, gameType)));

        if (json != null) {
            return HyriAPI.GSON.fromJson(json, new TypeToken<List<String >>(){}.getType());
        }

        if (this.hydrionManager.isEnabled()) {
            try {
                final List<String> maps = this.worldManager.getWorlds(game, gameType).get();

                HyriAPI.get().getRedisProcessor().process(jedis -> jedis.set(MAPS_KEY_FORMATTER.apply(game, gameType), HyriAPI.GSON.toJson(maps)));

                return maps;
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
            }
        }
        return new ArrayList<>();
    }

}
