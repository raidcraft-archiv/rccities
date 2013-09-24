package de.raidcraft.rccities.flags.city;

import com.sk89q.worldguard.protection.flags.DefaultFlag;
import com.sk89q.worldguard.protection.flags.StateFlag;
import de.raidcraft.RaidCraft;
import de.raidcraft.rccities.RCCitiesPlugin;
import de.raidcraft.rccities.api.city.City;
import de.raidcraft.rccities.api.flags.FlagInformation;
import de.raidcraft.rccities.api.flags.FlagRefreshType;
import de.raidcraft.rccities.api.flags.FlagType;
import de.raidcraft.rccities.api.flags.PlotFlag;
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
public class dummy extends AbstractBooleanPlotwiseCityFlag {

    public dummy(City city) {

        super(city);
    }

    @Override
    public void allow(Plot plot) {

        // check if plot has its own pvp setting -> skip
        PlotFlag existingFlag = RaidCraft.getComponent(RCCitiesPlugin.class).getFlagManager().getPlotFlag(plot, "pvp");
        if(existingFlag != null && !existingFlag.getType().convertToBoolean(existingFlag.getValue())) return;

        plot.getRegion().setFlag(DefaultFlag.PVP, StateFlag.State.ALLOW);
    }

    @Override
    public void deny(Plot plot) {

        // check if plot has its own pvp setting -> skip
        PlotFlag existingFlag = RaidCraft.getComponent(RCCitiesPlugin.class).getFlagManager().getPlotFlag(plot, "pvp");
        if(existingFlag != null && existingFlag.getType().convertToBoolean(existingFlag.getValue())) return;

        plot.getRegion().setFlag(DefaultFlag.PVP, StateFlag.State.DENY);
    }
}
