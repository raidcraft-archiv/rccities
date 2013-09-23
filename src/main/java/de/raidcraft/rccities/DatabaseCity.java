package de.raidcraft.rccities;

import de.raidcraft.RaidCraft;
import de.raidcraft.api.RaidCraftException;
import de.raidcraft.rccities.api.city.AbstractCity;
import de.raidcraft.rccities.api.plot.Plot;
import de.raidcraft.rccities.api.resident.Resident;
import de.raidcraft.rccities.tables.TCity;
import org.bukkit.Bukkit;
import org.bukkit.Location;

import java.sql.Timestamp;
import java.util.List;

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
    public void setFlag(String flagName, String flagValue) throws RaidCraftException {

        RaidCraft.getComponent(RCCitiesPlugin.class).getFlagManager().setCityFlag(this, flagName, flagValue);
    }

    @Override
    public void removeFlag(String flagName) {

        RaidCraft.getComponent(RCCitiesPlugin.class).getFlagManager().removeCityFlag(this, flagName);
    }

    @Override
    public void refreshFlags() {

        RaidCraft.getComponent(RCCitiesPlugin.class).getFlagManager().refreshCityFlags(this);
    }

    @Override
    public List<Resident> getResidents() {

        return RaidCraft.getComponent(RCCitiesPlugin.class).getResidentManager().getResidents(this);
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
            tCity.setMaxRadius(RaidCraft.getComponent(RCCitiesPlugin.class).getConfig().defaultMaxRadius);
            tCity.setPlotCredit(RaidCraft.getComponent(RCCitiesPlugin.class).getConfig().initialPlotCredit);
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
            tCity.setMaxRadius(getMaxRadius());
            tCity.setExp(getExp());
            RaidCraft.getDatabase(RCCitiesPlugin.class).update(tCity);
        }
    }

    @Override
    public void delete() {

        RCCitiesPlugin plugin = RaidCraft.getComponent(RCCitiesPlugin.class);
        for(Plot plot : plugin.getPlotManager().getPlots(this)) {
            plot.delete();
        }
        for(Resident resident : plugin.getResidentManager().getResidents(this)) {
            resident.delete();
        }

        plugin.getCityManager().removeFromCache(this);
        RaidCraft.getDatabase(RCCitiesPlugin.class).delete(TCity.class, getId());
    }
}
