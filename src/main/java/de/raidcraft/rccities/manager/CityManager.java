package de.raidcraft.rccities.manager;

import de.raidcraft.RaidCraft;
import de.raidcraft.api.RaidCraftException;
import de.raidcraft.rccities.RCCitiesPlugin;
import de.raidcraft.rccities.api.city.City;
import de.raidcraft.rccities.implementations.DatabaseCity;
import de.raidcraft.rccities.tables.TCity;
import de.raidcraft.util.CaseInsensitiveMap;
import org.bukkit.Location;

import java.util.Map;

/**
 * @author Philip Urban
 */
public class CityManager {

    private RCCitiesPlugin plugin;

    private Map<String, City> loadedCities = new CaseInsensitiveMap<>();

    public CityManager(RCCitiesPlugin plugin) {

        this.plugin = plugin;
    }

    public void createCity(String cityName, Location location, String creator) throws RaidCraftException {

        cityName = cityName.replace(' ', '_');
        City city = getCity(cityName);
        if(city != null) {
            throw new RaidCraftException("Es gibt bereits eine Stadt mit diesem Namen!");
        }
        city = new DatabaseCity(cityName, location, creator);
        loadedCities.put(cityName, city);
    }

    public void deleteCity(String cityName) throws RaidCraftException {

        City city = getCity(cityName);
        if(city == null) {
            throw new RaidCraftException("Es wurde keine Stadt mit dem namen gefunden!");
        }

        city.delete();
    }

    public void setSpawn(String cityName, Location newSpawn) throws RaidCraftException {

        City city = getCity(cityName);
        if(city == null) {
            throw new RaidCraftException("Es wurde keine Stadt mit dem namen gefunden!");
        }
        if(!newSpawn.getWorld().getName().equalsIgnoreCase(city.getSpawn().getWorld().getName())) {
            throw new RaidCraftException("Der neue Spawn muss sich auf der selben Welt befinden!");
        }

        city.setSpawn(newSpawn);
    }

    public City getCity(String name) {

        City city = loadedCities.get(name);

        if(city == null) {
            TCity tCity = RaidCraft.getDatabase(RCCitiesPlugin.class).find(TCity.class).where().ieq("name", name).findUnique();
            if(tCity != null) {
                city = new DatabaseCity(tCity);
            }
        }
        return city;
    }
}
