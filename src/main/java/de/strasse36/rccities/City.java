package de.strasse36.rccities;

import org.bukkit.Location;

/**
 * Author: Philip Urban
 * Date: 28.02.12 - 20:49
 * Description:
 */
public class City {
    private int id;
    private String name;
    private String description;
    private long size;
    private Location spawn;

    public City(){};

    public City(String name)
    {
        this.setName(name);
        this.setDescription("Noch keine Beschreibung verfügbar");
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Location getSpawn() {
        return spawn;
    }

    public void setSpawn(Location spawn) {
        this.spawn = spawn;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public long getSize() {
        return size;
    }

    public void setSize(long size) {
        this.size = size;
    }
}
