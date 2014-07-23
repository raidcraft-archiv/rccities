package de.raidcraft.rccities.tables;

import de.raidcraft.RaidCraft;
import de.raidcraft.rccities.RCCitiesPlugin;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.sql.Timestamp;
import java.util.Set;

/**
 * @author Philip Urban
 */
@Entity
@Table(name = "rccities_cities")
public class TCity {

    @Id
    private int id;
    private String name;
    private String creator;
    private Timestamp creationDate;
    @Lob
    private String description;
    private String world;
    private int x;
    private int y;
    private int z;
    private int pitch;
    private int yaw;
    private int plotCredit;
    private int maxRadius;
    private int exp;
    private int upgradeId;

    @OneToMany(cascade = CascadeType.REMOVE)
    @JoinColumn(name = "city_id")
    private Set<TPlot> plots;

    @OneToMany(cascade = CascadeType.REMOVE)
    @JoinColumn(name = "city_id")
    private Set<TResident> residents;

    @OneToMany(cascade = CascadeType.REMOVE)
    @JoinColumn(name = "city_id")
    private Set<TCityFlag> settings;

    @OneToMany(cascade = CascadeType.REMOVE)
    @JoinColumn(name = "city_id")
    private Set<TJoinRequest> requests;

    public void loadChildren() {

        plots = RaidCraft.getDatabase(RCCitiesPlugin.class).find(TPlot.class).where().eq("city_id", id).findSet();
        residents = RaidCraft.getDatabase(RCCitiesPlugin.class).find(TResident.class).where().eq("city_id", id).findSet();
        settings = RaidCraft.getDatabase(RCCitiesPlugin.class).find(TCityFlag.class).where().eq("city_id", id).findSet();
        requests = RaidCraft.getDatabase(RCCitiesPlugin.class).find(TJoinRequest.class).where().eq("city_id", id).findSet();
    }

    public int getId() {

        return id;
    }

    public void setId(int id) {

        this.id = id;
    }

    public String getName() {

        return name;
    }

    public void setName(String name) {

        this.name = name;
    }

    public String getCreator() {

        return creator;
    }

    public void setCreator(String creator) {

        this.creator = creator;
    }

    public Timestamp getCreationDate() {

        return creationDate;
    }

    public void setCreationDate(Timestamp creationDate) {

        this.creationDate = creationDate;
    }

    public String getDescription() {

        return description;
    }

    public void setDescription(String description) {

        this.description = description;
    }

    public String getWorld() {

        return world;
    }

    public void setWorld(String world) {

        this.world = world;
    }

    public int getX() {

        return x;
    }

    public void setX(int x) {

        this.x = x;
    }

    public int getY() {

        return y;
    }

    public void setY(int y) {

        this.y = y;
    }

    public int getZ() {

        return z;
    }

    public void setZ(int z) {

        this.z = z;
    }

    public int getPitch() {

        return pitch;
    }

    public void setPitch(int pitch) {

        this.pitch = pitch;
    }

    public int getYaw() {

        return yaw;
    }

    public void setYaw(int yaw) {

        this.yaw = yaw;
    }

    public int getPlotCredit() {

        return plotCredit;
    }

    public void setPlotCredit(int plotCredit) {

        this.plotCredit = plotCredit;
    }

    public int getMaxRadius() {

        return maxRadius;
    }

    public void setMaxRadius(int maxRadius) {

        this.maxRadius = maxRadius;
    }

    public int getExp() {

        return exp;
    }

    public void setExp(int exp) {

        this.exp = exp;
    }

    public int getUpgradeId() {

        return upgradeId;
    }

    public void setUpgradeId(int upgradeId) {

        this.upgradeId = upgradeId;
    }

    public Set<TPlot> getPlots() {

        return plots;
    }

    public void setPlots(Set<TPlot> plots) {

        this.plots = plots;
    }

    public Set<TResident> getResidents() {

        return residents;
    }

    public void setResidents(Set<TResident> residents) {

        this.residents = residents;
    }

    public Set<TCityFlag> getSettings() {

        return settings;
    }

    public void setSettings(Set<TCityFlag> settings) {

        this.settings = settings;
    }

    public Set<TJoinRequest> getRequests() {

        return requests;
    }

    public void setRequests(Set<TJoinRequest> requests) {

        this.requests = requests;
    }
}
