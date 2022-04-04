package fr.hyriode.api.rank;

import java.util.List;

/**
 * Project: HyriAPI
 * Created by AstFaster
 * on 24/08/2021 at 10:57
 */
public abstract class HyriRank {

    /** Rank's name */
    private final String name;
    /** Rank's prefix */
    private final String prefix;
    /** Permissions associated to the ranks */
    private final List<HyriPermission> permissions;

    /**
     * Constructor of {@link HyriRank}
     *
     * @param name Rank name
     * @param prefix Rank display name
     * @param permissions Rank permissions
     */
    public HyriRank(String name, String prefix, List<HyriPermission> permissions) {
        this.name = name;
        this.prefix = prefix;
        this.permissions = permissions;
    }

    public String getName() {
        return this.name;
    }

    public String getPrefix() {
        return this.prefix;
    }

    public List<HyriPermission> getPermissions() {
        return this.permissions;
    }

    public void addPermission(HyriPermission permission) {
        this.permissions.add(permission);
    }

    public void removePermission(HyriPermission permission) {
        this.permissions.remove(permission);
    }

    public boolean hasPermission(HyriPermission permission) {
        return this.permissions.contains(permission);
    }

    public EHyriRank getType() {
        return EHyriRank.getByName(this.name);
    }
}
