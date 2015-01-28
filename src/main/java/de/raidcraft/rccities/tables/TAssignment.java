package de.raidcraft.rccities.tables;

import de.raidcraft.RaidCraft;
import de.raidcraft.rccities.RCCitiesPlugin;
import de.raidcraft.rccities.api.plot.Plot;
import de.raidcraft.rccities.api.resident.Resident;
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
@Table(name = "rccities_assignments")
public class TAssignment {

    @Id
    private int id;
    @ManyToOne
    private TPlot plot;
    @ManyToOne
    private TResident resident;


    public void setPlot(Plot plot) {
        TPlot tPlot = RaidCraft.getDatabase(RCCitiesPlugin.class).find(TPlot.class, plot.getId());
        this.plot = tPlot;
    }


    public void setResident(Resident resident) {
        TResident tResident = RaidCraft.getDatabase(RCCitiesPlugin.class).find(TResident.class, resident.getId());
        this.resident = tResident;
    }
}
