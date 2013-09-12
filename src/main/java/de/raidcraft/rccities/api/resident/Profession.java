package de.raidcraft.rccities.api.resident;

import java.util.HashSet;
import java.util.Set;

/**
 * @author Philip Urban
 */
public enum Profession {

    RESIDENT(),
    MAYOR(ProfessionPermission.KICK, ProfessionPermission.INVITE, ProfessionPermission.BUILD_EVERYWHERE,
            ProfessionPermission.PLOT_DISTRIBUTION, ProfessionPermission.SET_DESCRIPTION, ProfessionPermission.SET_SPAWN),
    VICE_MAYOR(ProfessionPermission.KICK, ProfessionPermission.INVITE, ProfessionPermission.BUILD_EVERYWHERE,
            ProfessionPermission.PLOT_DISTRIBUTION),
    ASSISTANT(ProfessionPermission.INVITE, ProfessionPermission.BUILD_EVERYWHERE,
            ProfessionPermission.PLOT_DISTRIBUTION);

    private Set<ProfessionPermission> permissions = new HashSet<>();

    private Profession(ProfessionPermission... permissions) {

        for(ProfessionPermission permission : permissions) {
            this.permissions.add(permission);
        }
    }

    public boolean hasPermission(ProfessionPermission permission) {

        return permissions.contains(permission);
    }
}
