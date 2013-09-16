package de.raidcraft.rccities.api.resident;

import java.util.HashSet;
import java.util.Set;

/**
 * @author Philip Urban
 */
public enum Role {

    SLAVE("Sklave"),

    RESIDENT("Einwohner",
            RolePermission.LEAVE),

    MAYOR("Bürgermeister",
            RolePermission.KICK,
            RolePermission.INVITE,
            RolePermission.BUILD_EVERYWHERE,
            RolePermission.PLOT_DISTRIBUTION,
            RolePermission.SET_DESCRIPTION,
            RolePermission.SET_SPAWN,
            RolePermission.CITY_FLAG_MODIFICATION),

    VICE_MAYOR("Vize Bürgermeister",
            RolePermission.KICK,
            RolePermission.INVITE,
            RolePermission.BUILD_EVERYWHERE,
            RolePermission.PLOT_DISTRIBUTION,
            RolePermission.CITY_FLAG_MODIFICATION,
            RolePermission.PLOT_FLAG_MODIFICATION),

    ASSISTANT("Stadtassistent",
            RolePermission.INVITE,
            RolePermission.BUILD_EVERYWHERE,
            RolePermission.PLOT_DISTRIBUTION,
            RolePermission.PLOT_FLAG_MODIFICATION);

    private Set<RolePermission> permissions = new HashSet<>();
    private String friendlyName;

    private Role(String friendlyName, RolePermission... permissions) {

        this.friendlyName = friendlyName;
        for(RolePermission permission : permissions) {
            this.permissions.add(permission);
        }
    }

    public boolean hasPermission(RolePermission permission) {

        return permissions.contains(permission);
    }

    public String getFriendlyName() {

        return friendlyName;
    }
}
