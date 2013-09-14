package de.raidcraft.rccities.api.settings;

import de.raidcraft.rccities.api.plot.Plot;

/**
 * @author Philip Urban
 */
public abstract class AbstractPlotSetting extends AbstractSetting implements PlotSetting {

    private Plot plot;

    @Override
    public Plot getPlot() {

        return plot;
    }

    @Override
    public void setPlot(Plot plot) {

        this.plot = plot;
    }
}
