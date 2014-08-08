package de.raidcraft.rccities.flags.plot;

import com.sk89q.worldguard.protection.flags.DefaultFlag;
import com.sk89q.worldguard.protection.flags.StateFlag;
import de.raidcraft.api.RaidCraftException;
import de.raidcraft.rccities.api.flags.AbstractPlotFlag;
import de.raidcraft.rccities.api.flags.FlagInformation;
import de.raidcraft.rccities.api.flags.FlagType;
import de.raidcraft.rccities.api.plot.Plot;

/**
 * @author Philip Urban
 */
@FlagInformation(
        name = "MOB_SPAWNING",
        friendlyName = "Mob-Spawning (An/Aus)",
        type = FlagType.BOOLEAN,
        cooldown = 60,
        adminOnly = true
)
public class MobSpawnPlotFlag extends AbstractPlotFlag {

    public MobSpawnPlotFlag(Plot plot) {

        super(plot);
    }

    @Override
    public void refresh() throws RaidCraftException {

        if (getPlot() == null) return;

        boolean currentValue = getType().convertToBoolean(getValue());

        if (currentValue) {
            getPlot().getRegion().setFlag(DefaultFlag.MOB_SPAWNING, StateFlag.State.ALLOW);
        } else {
            getPlot().getRegion().setFlag(DefaultFlag.MOB_SPAWNING, StateFlag.State.DENY);
        }
    }
}
