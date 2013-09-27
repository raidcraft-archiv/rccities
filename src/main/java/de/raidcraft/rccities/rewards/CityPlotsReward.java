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
@RewardInformation("CITY_PLOTS")
public class CityPlotsReward extends AbstractReward<City> {

    int plotAmount;
    boolean broadcast;

    public CityPlotsReward(ConfigurationSection config) {

        super(config);
    }

    @Override
    public void load(ConfigurationSection config) {

        plotAmount = config.getInt("amount");
        broadcast = config.getBoolean("broadcast", false);
    }

    @Override
    public void reward(City city) {

        city.setPlotCredit(city.getPlotCredit() + plotAmount);

        if(broadcast) {
            RaidCraft.getComponent(RCCitiesPlugin.class).getResidentManager().broadcastCityMessage(city, "Die Stadt hat " + plotAmount + " neue Plots erhalten!");
        }
    }
}