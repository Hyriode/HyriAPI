package fr.hyriode.api.leaderboard;

import java.util.Calendar;
import java.util.TimeZone;
import java.util.function.Supplier;

/**
 * Created by AstFaster
 * on 06/06/2022 at 09:49
 */
public enum HyriLeaderboardScope {

    /** A leaderboard that never resets and counts the total of scores  */
    TOTAL("total", () -> -1L),
    /** A leaderboard resetting each month and counting monthly scores  */
    MONTHLY("monthly", () -> {
        final Calendar calendar = Calendar.getInstance();

        calendar.setTimeZone(TimeZone.getTimeZone("Europe/Paris"));
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        calendar.set(Calendar.MONTH, calendar.get(Calendar.MONTH) + 1);

        return calendar.getTimeInMillis() - System.currentTimeMillis();
    }),
    /** A leaderboard resetting each week and counting weekly scores */
    WEEKLY("weekly", () -> {
        final Calendar calendar = Calendar.getInstance();

        calendar.setTimeZone(TimeZone.getTimeZone("Europe/Paris"));
        calendar.set(Calendar.WEEK_OF_MONTH, calendar.get(Calendar.WEEK_OF_MONTH) + 1);
        calendar.set(Calendar.DAY_OF_WEEK, 2);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);

        return calendar.getTimeInMillis() - System.currentTimeMillis();
    }),
    /** A leaderboard resetting each day and counting daily scores */
    DAILY("daily", () -> {
        final Calendar calendar = Calendar.getInstance();

        calendar.setTimeZone(TimeZone.getTimeZone("Europe/Paris"));
        calendar.set(Calendar.DAY_OF_MONTH, calendar.get(Calendar.DAY_OF_MONTH) + 1);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);

        return calendar.getTimeInMillis() - System.currentTimeMillis();
    });

    /** The id of the scope */
    private final String id;
    private final Supplier<Long> nextReset;

    /**
     * The default constructor of a {@link HyriLeaderboardScope}
     *
     * @param id The id of the scope
     * @param nextReset The next reset supplier of the scope
     */
    HyriLeaderboardScope(String id, Supplier<Long> nextReset) {
        this.id = id;
        this.nextReset = nextReset;
    }

    /**
     * Get the identifier of the scope
     *
     * @return An identifier
     */
    public String getId() {
        return this.id;
    }

    /**
     * Get the time before the next reset of the scope
     *
     * @return A time in milliseconds
     */
    public long nextReset() {
        return this.nextReset.get();
    }

}
