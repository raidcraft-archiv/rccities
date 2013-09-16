package de.raidcraft.rccities;

import de.raidcraft.RaidCraft;
import de.raidcraft.rccities.api.city.AbstractCity;
import de.raidcraft.rccities.tables.TCity;
import org.bukkit.Bukkit;
import org.bukkit.Location;

import java.sql.Timestamp;

/**
 * @author Philip Urban
 */
public class DatabaseCity extends AbstractCity {

    public DatabaseCity(String name, Location spawn, String creator) {

        super(name, spawn, creator);
    }

    public DatabaseCity(TCity tCity) {

        //XXX setter call order is important!!!
        id = tCity.getId();
        name = tCity.getName();
        creator = tCity.getCreator();
        creationDate = tCity.getCreationDate();
        description = tCity.getDescription();
        plotCredit = tCity.getPlotCredit();
        spawn = new Location(Bukkit.getWorld(tCity.getWorld()), tCity.getX(), tCity.getY(), tCity.getZ(), tCity.getYaw(), tCity.getPitch());
    }

    @Override
    public int getSize() {

        return RaidCraft.getComponent(RCCitiesPlugin.class).getPlotManager().getPlots(this).size();
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
            id = tCity.getId();
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
            tCity.setPlotCredit(getPlotCredit());
            RaidCraft.getDatabase(RCCitiesPlugin.class).update(tCity);
        }
    }

    @Override
    public void delete() {

        //TODO get all city plots and delete them

        RaidCraft.getDatabase(RCCitiesPlugin.class).delete(TCity.class, getId());
    }
}
