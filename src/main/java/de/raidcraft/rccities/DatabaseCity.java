package de.raidcraft.rccities;

import de.raidcraft.RaidCraft;
import de.raidcraft.rccities.api.city.AbstractCity;
import de.raidcraft.rccities.tables.TCity;
import org.bukkit.Location;

import java.sql.Timestamp;

/**
 * @author Philip Urban
 */
public class DatabaseCity extends AbstractCity {

    public DatabaseCity(String name, Location spawn, String creator) {

        super(name, spawn, creator);
    }

    public DatabaseCity(int cityId) {


    }

    @Override
    public void save() {

        // save new city
        if(getId() == 0) {
            TCity tCity = new TCity();
            tCity.setCreationDate(new Timestamp(System.currentTimeMillis()));
            tCity.setCreator(getCreator());
            tCity.setName(getName());
            tCity.setWorld(getSpawn().getWorld().getName());
            tCity.setX(getSpawn().getX());
            tCity.setY(getSpawn().getY());
            tCity.setZ(getSpawn().getZ());
            tCity.setPitch(getSpawn().getPitch());
            tCity.setYaw(getSpawn().getYaw());
            RaidCraft.getDatabase(RCCitiesPlugin.class).save(tCity);
            setId(tCity.getId());
        }
        // update existing city
        else {
            TCity tCity = RaidCraft.getDatabase(RCCitiesPlugin.class).find(TCity.class, getId());
            tCity.setWorld(getSpawn().getWorld().getName());
            tCity.setX(getSpawn().getX());
            tCity.setY(getSpawn().getY());
            tCity.setZ(getSpawn().getZ());
            tCity.setPitch(getSpawn().getPitch());
            tCity.setYaw(getSpawn().getYaw());
            tCity.setDescription(getDescription());
            RaidCraft.getDatabase(RCCitiesPlugin.class).update(tCity);
        }
    }
}
