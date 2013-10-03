package de.raidcraft.rccities.requirements;

import de.raidcraft.RaidCraft;
import de.raidcraft.api.requirement.AbstractRequirement;
import de.raidcraft.api.requirement.RequirementInformation;
import de.raidcraft.api.requirement.RequirementResolver;
import de.raidcraft.rccities.RCCitiesPlugin;
import de.raidcraft.rccities.api.city.City;
import de.raidcraft.rccities.api.request.UpgradeRequest;
import de.raidcraft.rcupgrades.api.level.UpgradeLevel;
import org.bukkit.configuration.ConfigurationSection;

/**
 * @author Philip Urban
 */
@RequirementInformation("CITY_STAFF")
public class CityStaffRequirement extends AbstractRequirement<City> {

    private int amount;

    public CityStaffRequirement(RequirementResolver<City> resolver, ConfigurationSection config) {

        super(resolver, config);
    }

    @Override
    protected void load(ConfigurationSection data) {

        amount = data.getInt("exp");
    }

    @Override
    public boolean isMet(City city) {

        UpgradeLevel<City> upgradeLevel = (UpgradeLevel<City>)getResolver();
        RCCitiesPlugin plugin = RaidCraft.getComponent(RCCitiesPlugin.class);
        UpgradeRequest request = plugin.getCityManager().getUpgradeRequest(city, upgradeLevel);

        // new request
        if(request == null) {
            plugin.getCityManager().addUpgradeRequest(city, upgradeLevel);
            return false;
        }

        // check old request
        if(request.isAccepted()) {
            plugin.getCityManager().removeUpgradeRequest(request);
            return true;
        }
        else {
            return false;
        }
    }

    @Override
    public String getShortReason() {

        return "In Bearbeitung";
    }

    @Override
    public String getLongReason() {

        return "Ein Teammitglied wird sich in kürze um die Freischaltung kümmern!";
    }
}