package de.raidcraft.rccities.rewards;

import de.raidcraft.RaidCraft;
import de.raidcraft.api.reward.AbstractReward;
import de.raidcraft.api.reward.RewardInformation;
import de.raidcraft.rccities.RCCitiesPlugin;
import de.raidcraft.rccities.api.city.City;
import org.bukkit.configuration.ConfigurationSection;

/**
 * @author Philip Urban
 */
@RewardInformation("CITY_RADIUS")
public class CityRadiusReward extends AbstractReward<City> {

    int blocks;
    boolean broadcast;

    public CityRadiusReward(ConfigurationSection config) {

        super(config);
    }

    @Override
    public void load(ConfigurationSection config) {

        blocks = config.getInt("blocks");
        broadcast = config.getBoolean("broadcast", true);
    }

    @Override
    public void reward(City city) {

        city.setMaxRadius(city.getMaxRadius() + blocks);

        if (broadcast) {
            RaidCraft.getComponent(RCCitiesPlugin.class).getResidentManager().broadcastCityMessage(city, "Die Stadt hat ihren Radius um " + blocks + " Blöcke vergrößert!");
        }
    }

    @Override
    public String getDescription() {

        return "Stadtradius vergrößert um " + blocks + " Blöcke";
    }
}