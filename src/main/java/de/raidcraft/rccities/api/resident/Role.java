package de.raidcraft.rccities.api.resident;

import java.util.HashSet;
import java.util.Set;

/**
 * @author Philip Urban
 */
public enum Role {

    RESIDENT(),
    MAYOR(RolePermission.KICK, RolePermission.INVITE, RolePermission.BUILD_EVERYWHERE,
            RolePermission.PLOT_DISTRIBUTION, RolePermission.SET_DESCRIPTION, RolePermission.SET_SPAWN),
    VICE_MAYOR(RolePermission.KICK, RolePermission.INVITE, RolePermission.BUILD_EVERYWHERE,
            RolePermission.PLOT_DISTRIBUTION),
    ASSISTANT(RolePermission.INVITE, RolePermission.BUILD_EVERYWHERE,
            RolePermission.PLOT_DISTRIBUTION);

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
