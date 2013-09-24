package de.raidcraft.rccities.flags.city;

import com.sk89q.worldguard.protection.flags.DefaultFlag;
import de.raidcraft.RaidCraft;
import de.raidcraft.rccities.RCCitiesPlugin;
import de.raidcraft.rccities.api.city.City;
import de.raidcraft.rccities.api.flags.AbstractCityFlag;
import de.raidcraft.rccities.api.flags.FlagInformation;
import de.raidcraft.rccities.api.flags.FlagRefreshType;
import de.raidcraft.rccities.api.flags.FlagType;
import de.raidcraft.rccities.api.plot.Plot;
import de.raidcraft.rccities.api.resident.Resident;
import org.bukkit.ChatColor;

/**
 * @author Philip Urban
 */
@FlagInformation(
        name = "GREETINGS",
        type = FlagType.BOOLEAN,
        refreshType = FlagRefreshType.ON_CHANGE,
        refreshInterval = 0
)
public class GreetingsCityFlag extends AbstractCityFlag {

    public GreetingsCityFlag(City city) {

        super(city);
    }

    @Override
    public void refresh() {

        if(getCity() == null) return;

        boolean currentValue = getType().convertToBoolean(getValue());

        if(currentValue) {
            for(Plot plot : RaidCraft.getComponent(RCCitiesPlugin.class).getPlotManager().getPlots(getCity())) {
                String residentList = "";
                for(Resident resident : plot.getAssignedResidents()) {
                    if(!residentList.isEmpty()) residentList += ChatColor.GRAY + ", ";
                    residentList += ChatColor.GREEN + resident.getName();
                }
                plot.getRegion().setFlag(DefaultFlag.GREET_MESSAGE, ChatColor.GREEN + "~ " + plot.getId() + ": " + residentList + " ~");
            }
        }
        else {
            for(Plot plot : RaidCraft.getComponent(RCCitiesPlugin.class).getPlotManager().getPlots(getCity())) {
                plot.getRegion().setFlag(DefaultFlag.GREET_MESSAGE, null);
            }
        }
    }
}
