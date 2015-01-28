package de.raidcraft.rccities.tables;

import de.raidcraft.RaidCraft;
import de.raidcraft.rccities.RCCitiesPlugin;
import de.raidcraft.rccities.api.city.City;
import de.raidcraft.rcupgrades.api.level.UpgradeLevel;
import de.raidcraft.rcupgrades.api.upgrade.Upgrade;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.sql.Timestamp;

/**
 * @author Philip Urban
 */
@Getter
@Setter
@Entity
@Table(name = "rccities_upgrade_requests")
public class TUpgradeRequest {

    @Id
    private int id;
    @ManyToOne
    private TCity city;
    private String levelIdentifier;
    private String info;
    private boolean rejected;
    private boolean accepted;
    private String rejectReason;
    private Timestamp rejectDate;


    public City getRCCity() {
        return RaidCraft.getComponent(RCCitiesPlugin.class).getCityManager().getCity(city.getName());
    }

    public void setCity(City city) {
        TCity tCity = RaidCraft.getDatabase(RCCitiesPlugin.class).find(TCity.class, city.getId());
        this.city = tCity;
    }

    public UpgradeLevel<City> getUpgradeLevel() {
        for (Upgrade upgrade : getRCCity().getUpgrades().getUpgrades()) {
            for (UpgradeLevel upgradeLevel : upgrade.getLevels()) {
                if (upgradeLevel.getId().equalsIgnoreCase(getLevelIdentifier())) {
                    return upgradeLevel;
                }
            }
        }
        return null;
    }
}
