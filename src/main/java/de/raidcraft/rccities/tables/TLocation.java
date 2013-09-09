package de.raidcraft.rccities.tables;

import com.sk89q.worldedit.Vector;
import org.bukkit.Bukkit;
import org.bukkit.Location;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 * @author Philip Urban
 */
@Entity
@Table(name = "rcfarms_locations")
public class TLocation {

    @Id
    private int id;
    @ManyToOne
    private TCity city;
    private String world;
    private int x;
    private int y;
    private int z;

    public TLocation() {
        // required
    }

    public TLocation(Location location, TCity city) {

        this.city = city;
        this.world = location.getWorld().getName();
        this.x = location.getBlockX();
        this.y = location.getBlockY();
        this.z = location.getBlockZ();
    }

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

    public Location getLocation() {

        return new Location(Bukkit.getWorld(world), x, y, z);
    }

    public Vector getSk89qVector() {

        return new Vector(x, y, z);
    }
}
