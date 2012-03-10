package de.strasse36.rccities;

import de.strasse36.rccities.config.MainConfig;
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
    private boolean greetings;

    public City()
    {
        this.setName("-");
        this.setDescription("-");
        this.setSize(MainConfig.getChunksPerPlayer());
        this.setSpawn(null);
    };

    public City(String name, Location spawn)
    {
        this.setName(name);
        this.setDescription("Noch keine Beschreibung verfügbar");
        this.setSize(MainConfig.getChunksPerPlayer());
        this.setSpawn(spawn);
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

    public boolean isGreetings() {
        return greetings;
    }

    public void setGreetings(boolean greetings) {
        this.greetings = greetings;
    }
    
    public int getGreetings() {
        if(isGreetings())
            return 1;
        else
            return 0;
    }

    public String getBankAccount() {
        return "rccities_" + getName().toLowerCase();
    }
}
