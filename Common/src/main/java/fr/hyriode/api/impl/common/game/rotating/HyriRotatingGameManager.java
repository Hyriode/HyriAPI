package fr.hyriode.api.impl.common.game.rotating;

import fr.hyriode.api.HyriAPI;
import fr.hyriode.api.game.IHyriGameInfo;
import fr.hyriode.api.game.rotating.IHyriRotatingGame;
import fr.hyriode.api.game.rotating.IHyriRotatingGameManager;
import fr.hyriode.api.game.rotating.RotatingGameChangedEvent;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by AstFaster
 * on 27/07/2022 at 12:08
 */
public class HyriRotatingGameManager implements IHyriRotatingGameManager {

    private static final String CURRENT_GAME_KEY = "rotating-game";
    private static final String GAMES_KEY = "rotating-games";

    @Override
    public IHyriRotatingGame getRotatingGame() {
        return HyriAPI.get().getRedisProcessor().get(jedis -> {
            final byte[] bytes = jedis.get(CURRENT_GAME_KEY.getBytes(StandardCharsets.UTF_8));

            return bytes == null ? null : HyriAPI.get().getDataSerializer().deserialize(new HyriRotatingGame(), bytes);
        });
    }

    @Override
    public void addRotatingGame(int order, String game) {
        if (order == 0) {
            this.addRotatingGame(1, game);
            this.switchToNextRotatingGame();
            return;
        }

        HyriAPI.get().getRedisProcessor().process(jedis -> {
            final List<IHyriGameInfo> games = this.getRotatingGames();

            for (int i = 0; i < games.size(); i++) {
                if (i >= order) {
                    jedis.zadd(GAMES_KEY, Integer.MAX_VALUE - i, games.get(i).getName());
                }
            }

            jedis.zadd(GAMES_KEY, Integer.MAX_VALUE - order, game);
        });
    }

    @Override
    public void removeRotatingGame(String game) {
        HyriAPI.get().getRedisProcessor().process(jedis -> {
            final List<IHyriGameInfo> games = this.getRotatingGames();
            final IHyriRotatingGame currentGame = this.getRotatingGame();

            games.removeIf(gameInfo -> gameInfo.getName().equals(game));

            jedis.zrem(GAMES_KEY, game);
            jedis.del(CURRENT_GAME_KEY);

            for (int i = 0; i < games.size(); i++) {
                final String gameName = games.get(i).getName();

                jedis.zadd(GAMES_KEY, Integer.MAX_VALUE - i, gameName);

                if (i == 0 && (currentGame == null || !currentGame.getInfo().getName().equals(gameName))) {
                    jedis.set(CURRENT_GAME_KEY.getBytes(StandardCharsets.UTF_8), HyriAPI.get().getDataSerializer().serialize(new HyriRotatingGame(gameName, System.currentTimeMillis())));
                }
            }
        });
    }

    @Override
    public void switchToNextRotatingGame() {
        final List<IHyriGameInfo> games = this.getRotatingGames();
        final IHyriGameInfo game = games.size() > 1 ? games.get(1) : (games.size() == 1 ? games.get(0) : null);

        if (game == null) {
            return;
        }

        HyriAPI.get().getRedisProcessor().process(jedis -> {
            for (int i = 0; i < games.size(); i++) {
                final String gameName = games.get(i).getName();

                if (i == 0 && games.size() > 1) {
                    jedis.zadd(GAMES_KEY, Integer.MAX_VALUE - games.size(), gameName);
                    continue;
                } else if (i == 1 || games.size() == 1) {
                    jedis.set(CURRENT_GAME_KEY.getBytes(StandardCharsets.UTF_8), HyriAPI.get().getDataSerializer().serialize(new HyriRotatingGame(gameName, System.currentTimeMillis())));
                    jedis.zadd(GAMES_KEY, Integer.MAX_VALUE, gameName);
                    continue;
                }

                jedis.zadd(GAMES_KEY, Integer.MAX_VALUE - i, gameName);
            }
        });

        HyriAPI.get().getNetworkManager().getEventBus().publish(new RotatingGameChangedEvent(game.getName()));
    }

    @Override
    public int getOrder(String game) {
        return (int) (double) HyriAPI.get().getRedisProcessor().get(jedis -> jedis.zrevrank(GAMES_KEY, game));
    }

    @Override
    public List<IHyriGameInfo> getRotatingGames() {
        return HyriAPI.get().getRedisProcessor().get(jedis -> {
            final List<String> gameNames = jedis.zrange(GAMES_KEY, 0, -1);
            final List<IHyriGameInfo> games = new ArrayList<>();

            for (String gameName : gameNames) {
                games.add(HyriAPI.get().getGameManager().getGameInfo(gameName));
            }

            Collections.reverse(games);

            return games;
        });
    }

}
