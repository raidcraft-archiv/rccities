package de.raidcraft.rccities.tables;

import de.raidcraft.RaidCraft;
import de.raidcraft.rccities.RCCitiesPlugin;
import de.raidcraft.rccities.api.city.City;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @author Philip Urban
 */
@Getter
@Setter
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
    private Set<TAssignment> assignment = new HashSet<>();
    @OneToMany(cascade = CascadeType.REMOVE)
    @JoinColumn(name = "plot_id")
    private List<TPlotFlag> flags = new ArrayList<>();

    public void setCity(City city) {
        TCity tCity = RaidCraft.getDatabase(RCCitiesPlugin.class).find(TCity.class, city.getId());
        this.city = tCity;
    }
}
