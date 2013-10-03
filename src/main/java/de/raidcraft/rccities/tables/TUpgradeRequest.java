package de.raidcraft.rccities.tables;

import de.raidcraft.RaidCraft;
import de.raidcraft.rccities.RCCitiesPlugin;
import de.raidcraft.rccities.api.city.City;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 * @author Philip Urban
 */
@Entity
@Table(name = "rccities_upgrade_requests")
public class TUpgradeRequest {

    @Id
    private int id;
    @ManyToOne
    private TCity city;
    private String level_identifier;
    private String info;

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

    public String getLevel_identifier() {

        return level_identifier;
    }

    public void setLevel_identifier(String level_identifier) {

        this.level_identifier = level_identifier;
    }

    public String getInfo() {

        return info;
    }

    public void setInfo(String info) {

        this.info = info;
    }
}
