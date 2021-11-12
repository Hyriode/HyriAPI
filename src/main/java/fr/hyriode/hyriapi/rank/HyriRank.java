package fr.hyriode.hyriapi.rank;

import java.util.List;

/**
 * Project: HyriAPI
 * Created by AstFaster
 * on 24/08/2021 at 10:57
 */
public abstract class HyriRank {

    /**
     * Fields
     */

    private String name;
    private String displayName;
    private String description;
    private List<HyriPermission> permissions;

    /**
     * Default constructor of {@link HyriRank}
     *
     * @param name - Rank name
     * @param displayName - Rank display name
     * @param description - Rank description
     * @param permissions - Rank permissions
     */
    public HyriRank(String name, String displayName, String description, List<HyriPermission> permissions) {
        this.name = name;
        this.displayName = displayName;
        this.description = description;
        this.permissions = permissions;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDisplayName() {
        return this.displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getDescription() {
        return this.description;
    }

    public void setDescription(String description) {
        this.description = description;
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

}
