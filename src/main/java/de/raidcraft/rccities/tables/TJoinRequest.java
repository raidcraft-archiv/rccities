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
@Table(name = "rccities_join_requests")
public class TJoinRequest {

    @Id
    private int id;
    @ManyToOne
    private TCity city;
    private String player;
    private boolean rejected;
    private String rejectReason;

    public int getId() {

        return id;
    }

    public void setCity(City city) {

        TCity tCity = RaidCraft.getDatabase(RCCitiesPlugin.class).find(TCity.class, city.getId());
        this.city = tCity;
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

    public String getPlayer() {

        return player;
    }

    public void setPlayer(String player) {

        this.player = player;
    }

    public boolean isRejected() {

        return rejected;
    }

    public void setRejected(boolean rejected) {

        this.rejected = rejected;
    }

    public String getRejectReason() {

        return rejectReason;
    }

    public void setRejectReason(String rejectReason) {

        this.rejectReason = rejectReason;
    }
}
