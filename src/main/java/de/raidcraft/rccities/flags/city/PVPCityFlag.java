package de.raidcraft.rccities.flags.city;

import com.sk89q.worldguard.protection.flags.DefaultFlag;
import com.sk89q.worldguard.protection.flags.StateFlag;
import de.raidcraft.rccities.api.city.City;
import de.raidcraft.rccities.api.flags.FlagInformation;
import de.raidcraft.rccities.api.flags.FlagRefreshType;
import de.raidcraft.rccities.api.flags.FlagType;
import de.raidcraft.rccities.api.plot.Plot;

/**
 * @author Philip Urban
 */
@FlagInformation(
        name = "PVP",
        type = FlagType.BOOLEAN,
        refreshType = FlagRefreshType.ON_CHANGE,
        refreshInterval = 0,
        cooldown = 60
)
public class PVPCityFlag extends AbstractBooleanPlotwiseCityFlag {

    public PVPCityFlag(City city) {

        super(city);
    }

    @Override
    public void allow(Plot plot) {

        plot.getRegion().setFlag(DefaultFlag.PVP, StateFlag.State.ALLOW);
    }

    @Override
    public void deny(Plot plot) {

        plot.getRegion().setFlag(DefaultFlag.PVP, StateFlag.State.DENY);
    }
}
