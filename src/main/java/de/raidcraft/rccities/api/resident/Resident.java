package de.raidcraft.rccities.api.resident;

import de.raidcraft.rccities.api.plot.AbstractPlot;

import java.util.Set;

/**
 * @author Philip Urban
 */
public interface Resident {

    public String getName();

    public Set<AbstractPlot> getPlots();
}
