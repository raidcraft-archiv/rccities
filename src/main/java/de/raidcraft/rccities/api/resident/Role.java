package de.raidcraft.rccities.api.resident;

import java.util.HashSet;
import java.util.Set;

/**
 * @author Philip Urban
 */
public enum Role {

    SLAVE("Sklave", true),

    RESIDENT("Einwohner", false,
            RolePermission.LEAVE
    ),

    ADMIN("Administrator", true,
            RolePermission.KICK,
            RolePermission.INVITE,
            RolePermission.BUILD_EVERYWHERE,
            RolePermission.PLOT_DISTRIBUTION,
            RolePermission.SET_DESCRIPTION,
            RolePermission.SET_SPAWN,
            RolePermission.CITY_FLAG_MODIFICATION,
            RolePermission.PROMOTE,
            RolePermission.SPAWN_TELEPORT,
            RolePermission.PLOT_BUY
    ),

    MAYOR("Bürgermeister", true,
            RolePermission.KICK,
            RolePermission.INVITE,
            RolePermission.BUILD_EVERYWHERE,
            RolePermission.PLOT_DISTRIBUTION,
            RolePermission.SET_DESCRIPTION,
            RolePermission.SET_SPAWN,
            RolePermission.PROMOTE,
            RolePermission.CITY_FLAG_MODIFICATION,
            RolePermission.PLOT_FLAG_MODIFICATION,
            RolePermission.PLOT_BUY
    ),

    VICE_MAYOR("Vize Bürgermeister", false,
            RolePermission.KICK,
            RolePermission.INVITE,
            RolePermission.BUILD_EVERYWHERE,
            RolePermission.PLOT_DISTRIBUTION,
            RolePermission.CITY_FLAG_MODIFICATION,
            RolePermission.PLOT_FLAG_MODIFICATION,
            RolePermission.PROMOTE,
            RolePermission.PLOT_BUY
    ),

    ASSISTANT("Stadtassistent", false,
            RolePermission.INVITE,
            RolePermission.BUILD_EVERYWHERE,
            RolePermission.PLOT_DISTRIBUTION,
            RolePermission.PLOT_FLAG_MODIFICATION
    );

    private Set<RolePermission> permissions = new HashSet<>();
    private String friendlyName;
    private boolean adminOnly;

    private Role(String friendlyName, boolean adminOnly, RolePermission... permissions) {

        this.friendlyName = friendlyName;
        this.adminOnly = adminOnly;
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

    public boolean isAdminOnly() {

        return adminOnly;
    }
}
