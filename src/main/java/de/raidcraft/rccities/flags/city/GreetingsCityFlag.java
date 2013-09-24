package de.raidcraft.rccities.flags.city;

import com.sk89q.worldguard.protection.flags.DefaultFlag;
import de.raidcraft.rccities.api.city.City;
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
        refreshInterval = 0,
        cooldown = 60
)
public class GreetingsCityFlag extends AbstractBooleanPlotwiseCityFlag {

    public GreetingsCityFlag(City city) {

        super(city);
    }

    @Override
    public void allow(Plot plot) {

        String residentList = "";
        for(Resident resident : plot.getAssignedResidents()) {
            if(!residentList.isEmpty()) residentList += ChatColor.GRAY + ", ";
            residentList += ChatColor.GREEN + resident.getName();
        }
        plot.getRegion().setFlag(DefaultFlag.GREET_MESSAGE, ChatColor.GREEN + "~ " + plot.getId() + ": " + residentList + " ~");
    }

    @Override
    public void deny(Plot plot) {

        plot.getRegion().setFlag(DefaultFlag.GREET_MESSAGE, null);
    }
}