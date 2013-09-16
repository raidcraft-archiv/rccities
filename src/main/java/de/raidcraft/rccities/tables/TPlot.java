package de.raidcraft.rccities.tables;

import de.raidcraft.RaidCraft;
import de.raidcraft.rccities.RCCitiesPlugin;
import de.raidcraft.rccities.api.city.City;

import javax.persistence.*;
import java.util.Set;

/**
 * @author Philip Urban
 */
@Entity
@Table(name = "rccities_plots")
public class TPlot {

    @Id
    private int id;
    @ManyToOne
    private TCity city;
    private int x;
    private int z;

    @OneToMany(cascade = CascadeType.REMOVE)
    @JoinColumn(name = "plot_id")
    private Set<TAssignment> assignment;

    public int getId() {

        return id;
    }

    public void setId(int id) {

        this.id = id;
    }

    public TCity getCity() {

        return city;
    }

    public void setCity(TCity city) {

        this.city = city;
    }

    public void setCity(City city) {

        TCity tCity = RaidCraft.getDatabase(RCCitiesPlugin.class).find(TCity.class, city.getId());
        this.city = tCity;
    }

    public int getX() {

        return x;
    }

    public void setX(int x) {

        this.x = x;
    }

    public int getZ() {

        return z;
    }

    public void setZ(int z) {

        this.z = z;
    }

    public Set<TAssignment> getAssignment() {

        return assignment;
    }

    public void setAssignment(Set<TAssignment> assignment) {

        this.assignment = assignment;
    }
}
