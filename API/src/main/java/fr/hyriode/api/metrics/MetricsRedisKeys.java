package fr.hyriode.api.metrics;

public enum MetricsRedisKeys {

    HYRIS("money:hyris"),
    HYODES("money:hyodes"),

    HYRI_PLUS("ranks:hyriplus"),

    REGISTRED_PLAYERS("players:registred");

    private final String key;

    MetricsRedisKeys(String key) {
        this.key = "hyreos:metrics:" + key + ":";
    }

    public String getKey() {
        return this.key;
    }

    @Override
    public String toString() {
        return this.key;
    }
}
