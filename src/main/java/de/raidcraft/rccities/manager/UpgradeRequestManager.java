package de.raidcraft.rccities.manager;

import de.raidcraft.rccities.RCCitiesPlugin;
import de.raidcraft.rccities.api.city.City;
import de.raidcraft.rccities.api.request.UpgradeRequest;
import de.raidcraft.rcupgrades.api.level.UpgradeLevel;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Philip Urban
 */
public class UpgradeRequestManager {

    private RCCitiesPlugin plugin;

    public UpgradeRequestManager(RCCitiesPlugin plugin) {

        this.plugin = plugin;
    }

    public UpgradeRequest getRequest(City city, UpgradeLevel upgradeLevel) {

        for(UpgradeRequest upgradeRequest : getRequests(city)) {
            if(upgradeRequest.getUpgradeLevel().equals(upgradeLevel)) {
                return upgradeRequest;
            }
        }
        return null;
    }

    public List<UpgradeRequest> getRequests(City city) {

        List<UpgradeRequest> requests = new ArrayList<>();

        //TODO
        return requests;
    }
}
