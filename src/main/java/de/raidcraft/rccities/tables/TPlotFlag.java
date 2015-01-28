package de.raidcraft.rccities.tables;

import de.raidcraft.RaidCraft;
import de.raidcraft.rccities.RCCitiesPlugin;
import de.raidcraft.rccities.api.plot.Plot;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 * @author Philip Urban
 */
@Getter
@Setter
@Entity
@Table(name = "rccities_plot_flags")
public class TPlotFlag {

    @Id
    private int id;
    @ManyToOne
    private TPlot plot;
    private String name;
    private String value;

    public void setPlot(Plot plot) {
        TPlot tPlot = RaidCraft.getDatabase(RCCitiesPlugin.class).find(TPlot.class, plot.getId());
        this.plot = tPlot;
    }
}
