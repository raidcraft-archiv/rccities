package de.raidcraft.rccities.api.settings;

import de.raidcraft.rccities.api.plot.Plot;

/**
 * @author Philip Urban
 */
public interface PlotSetting extends Setting {

    public Plot getPlot();

    public void setPlot(Plot plot);
}
