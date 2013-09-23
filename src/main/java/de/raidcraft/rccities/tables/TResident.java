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
@Table(name = "rccities_residents")
public class TResident {

    @Id
    @Version
    private int id;
    @ManyToOne
    private TCity city;
    private String name;
    private String profession;

    @OneToMany(cascade = CascadeType.REMOVE)
    @JoinColumn(name = "resident_id")
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

    public void setCity(City city) {

        TCity tCity = RaidCraft.getDatabase(RCCitiesPlugin.class).find(TCity.class, city.getId());
        this.city = tCity;
    }

    public void setCity(TCity city) {

        this.city = city;
    }

    public String getName() {

        return name;
    }

    public void setName(String name) {

        this.name = name;
    }

    public String getProfession() {

        return profession;
    }

    public void setProfession(String profession) {

        this.profession = profession;
    }

    public Set<TAssignment> getAssignment() {

        return assignment;
    }

    public void setAssignment(Set<TAssignment> assignment) {

        this.assignment = assignment;
    }
}
