package de.raidcraft.rccities.flags.city;

import com.sk89q.worldguard.protection.flags.DefaultFlag;
import com.sk89q.worldguard.protection.flags.StateFlag;
import de.raidcraft.RaidCraft;
import de.raidcraft.rccities.RCCitiesPlugin;
import de.raidcraft.rccities.api.city.City;
import de.raidcraft.rccities.api.flags.FlagInformation;
import de.raidcraft.rccities.api.flags.FlagType;
import de.raidcraft.rccities.api.flags.PlotFlag;
import de.raidcraft.rccities.api.plot.Plot;
import de.raidcraft.rccities.flags.plot.PvpPlotFlag;

/**
 * @author Philip Urban
 */
@FlagInformation(
        name = "LEAF_DECAY",
        friendlyName = "Laub-Zerfall (An/Aus)",
        type = FlagType.BOOLEAN
)
public class LeafDecayCityFlag extends AbstractBooleanPlotwiseCityFlag {

    public LeafDecayCityFlag(City city) {

        super(city);
    }

    @Override
    public void announce(boolean state) {
    }

    @Override
    public void allow(Plot plot) {

        // check if plot has its own pvp setting -> skip
        PlotFlag existingFlag = RaidCraft.getComponent(RCCitiesPlugin.class).getFlagManager().getPlotFlag(plot, PvpPlotFlag.class);
        if(existingFlag != null && !existingFlag.getType().convertToBoolean(existingFlag.getValue())) return;

        plot.getRegion().setFlag(DefaultFlag.LEAF_DECAY, StateFlag.State.ALLOW);
    }

    @Override
    public void deny(Plot plot) {

        // check if plot has its own pvp setting -> skip
        PlotFlag existingFlag = RaidCraft.getComponent(RCCitiesPlugin.class).getFlagManager().getPlotFlag(plot, PvpPlotFlag.class);
        if(existingFlag != null && existingFlag.getType().convertToBoolean(existingFlag.getValue())) return;

        plot.getRegion().setFlag(DefaultFlag.LEAF_DECAY, StateFlag.State.DENY);
    }
}
