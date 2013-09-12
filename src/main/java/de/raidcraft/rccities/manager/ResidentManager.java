package de.raidcraft.rccities.manager;

import de.raidcraft.rccities.RCCitiesPlugin;
import de.raidcraft.rccities.api.resident.Resident;
import de.raidcraft.util.CaseInsensitiveMap;

import java.util.Map;

/**
 * @author Philip Urban
 */
public class ResidentManager {

    private RCCitiesPlugin plugin;
    private Map<String, Resident> cachedResidents = new CaseInsensitiveMap<>();

    public ResidentManager(RCCitiesPlugin plugin) {

        this.plugin = plugin;
    }

}
