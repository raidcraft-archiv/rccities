package de.raidcraft.rccities.tables;

import de.raidcraft.RaidCraft;
import de.raidcraft.rccities.RCCitiesPlugin;
import de.raidcraft.rccities.api.city.City;
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
@Table(name = "rccities_city_flags")
public class TCityFlag {

    @Id
    private int id;
    @ManyToOne
    private TCity city;
    private String name;
    private String value;

    public void setCity(City city) {
        TCity tCity = RaidCraft.getDatabase(RCCitiesPlugin.class).find(TCity.class, city.getId());
        this.city = tCity;
    }
}
