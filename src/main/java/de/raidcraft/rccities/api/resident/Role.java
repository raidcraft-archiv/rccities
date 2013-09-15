package de.raidcraft.rccities.api.resident;

import java.util.HashSet;
import java.util.Set;

/**
 * @author Philip Urban
 */
public enum Role {

    RESIDENT(),
    MAYOR(RolePermission.KICK,
            RolePermission.INVITE,
            RolePermission.BUILD_EVERYWHERE,
            RolePermission.PLOT_DISTRIBUTION,
            RolePermission.SET_DESCRIPTION,
            RolePermission.SET_SPAWN,
            RolePermission.CITY_FLAG_MODIFICATION),

    VICE_MAYOR(RolePermission.KICK,
            RolePermission.INVITE,
            RolePermission.BUILD_EVERYWHERE,
            RolePermission.PLOT_DISTRIBUTION,
            RolePermission.CITY_FLAG_MODIFICATION,
            RolePermission.PLOT_FLAG_MODIFICATION),

    ASSISTANT(RolePermission.INVITE,
            RolePermission.BUILD_EVERYWHERE,
            RolePermission.PLOT_DISTRIBUTION,
            RolePermission.PLOT_FLAG_MODIFICATION);

    private Set<RolePermission> permissions = new HashSet<>();

    private Role(RolePermission... permissions) {

        for(RolePermission permission : permissions) {
            this.permissions.add(permission);
        }
    }

    public boolean hasPermission(RolePermission permission) {

        return permissions.contains(permission);
    }
}
