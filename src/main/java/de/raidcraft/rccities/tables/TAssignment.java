package de.raidcraft.rccities.tables;

import javax.persistence.*;

/**
 * @author Philip Urban
 */
@Entity
@Table(name = "rccities_assignments")
public class TAssignment {

    @Id
    private int id;
    @ManyToOne
    private TPlot plot;
    @ManyToOne
    private TResident resident;

    public int getId() {

        return id;
    }

    public void setId(int id) {

        this.id = id;
    }

    public TPlot getPlot() {

        return plot;
    }

    public void setPlot(TPlot plot) {

        this.plot = plot;
    }

    public TResident getResident() {

        return resident;
    }

    public void setResident(TResident resident) {

        this.resident = resident;
    }
}
