package de.raidcraft.rccities.tables;

import de.raidcraft.RaidCraft;
import de.raidcraft.rccities.RCCitiesPlugin;

import javax.persistence.*;
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
    private double x;
    private double y;
    private double z;
    private float pitch;
    private float yaw;
    private int plotCredit;

    @OneToMany(cascade = CascadeType.REMOVE)
    @JoinColumn(name = "city_id")
    private Set<TPlot> plots;

    @OneToMany(cascade = CascadeType.REMOVE)
    @JoinColumn(name = "city_id")
    private Set<TResident> residents;

    @OneToMany(cascade = CascadeType.REMOVE)
    @JoinColumn(name = "city_id")
    private Set<TSetting> settings;

    public void loadChildren() {

        plots = RaidCraft.getDatabase(RCCitiesPlugin.class).find(TPlot.class).where().eq("city_id", id).findSet();
        residents = RaidCraft.getDatabase(RCCitiesPlugin.class).find(TResident.class).where().eq("city_id", id).findSet();
        settings = RaidCraft.getDatabase(RCCitiesPlugin.class).find(TSetting.class).where().eq("city_id", id).findSet();
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

    public double getX() {

        return x;
    }

    public void setX(double x) {

        this.x = x;
    }

    public double getY() {

        return y;
    }

    public void setY(double y) {

        this.y = y;
    }

    public double getZ() {

        return z;
    }

    public void setZ(double z) {

        this.z = z;
    }

    public float getPitch() {

        return pitch;
    }

    public void setPitch(float pitch) {

        this.pitch = pitch;
    }

    public float getYaw() {

        return yaw;
    }

    public void setYaw(float yaw) {

        this.yaw = yaw;
    }

    public int getPlotCredit() {

        return plotCredit;
    }

    public void setPlotCredit(int plotCredit) {

        this.plotCredit = plotCredit;
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

    public Set<TSetting> getSettings() {

        return settings;
    }

    public void setSettings(Set<TSetting> settings) {

        this.settings = settings;
    }
}
