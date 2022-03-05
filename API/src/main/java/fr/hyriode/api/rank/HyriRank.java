package fr.hyriode.api.rank;

import fr.hyriode.api.settings.HyriLanguage;

import java.util.List;
import java.util.Map;

/**
 * Project: HyriAPI
 * Created by AstFaster
 * on 24/08/2021 at 10:57
 */
public abstract class HyriRank {

    /** Rank's name */
    private String name;
    /** Rank's display names */
    private Map<HyriLanguage, String> displayNames;
    /** Permissions associated to the ranks */
    private List<HyriPermission> permissions;

    /**
     * Constructor of {@link HyriRank}
     *
     * @param name Rank name
     * @param displayNames Rank display names
     * @param permissions Rank permissions
     */
    public HyriRank(String name, Map<HyriLanguage, String> displayNames, List<HyriPermission> permissions) {
        this.name = name;
        this.displayNames = displayNames;
        this.permissions = permissions;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Map<HyriLanguage, String> getDisplayNames() {
        return this.displayNames;
    }

    public void setDisplayNames(Map<HyriLanguage, String> displayNames) {
        this.displayNames = displayNames;
    }

    public List<HyriPermission> getPermissions() {
        return this.permissions;
    }

    public void setPermissions(List<HyriPermission> permissions) {
        this.permissions = permissions;
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
