package de.raidcraft.rccities.manager;

import de.raidcraft.RaidCraft;
import de.raidcraft.api.RaidCraftException;
import de.raidcraft.rccities.DatabaseCity;
import de.raidcraft.rccities.RCCitiesPlugin;
import de.raidcraft.rccities.api.city.City;
import de.raidcraft.rccities.tables.TCity;
import de.raidcraft.util.CaseInsensitiveMap;
import org.bukkit.Location;

import java.util.Collection;
import java.util.Map;

/**
 * @author Philip Urban
 */
public class CityManager {

    private RCCitiesPlugin plugin;
    private Map<String, City> cachedCities = new CaseInsensitiveMap<>();

    public CityManager(RCCitiesPlugin plugin) {

        this.plugin = plugin;
    }

    public City createCity(String cityName, Location location, String creator) throws RaidCraftException {

        cityName = cityName.replace(' ', '_');
        City city = getCity(cityName);
        if(city != null) {
            throw new RaidCraftException("Es gibt bereits eine Stadt mit diesem Namen!");
        }
        city = new DatabaseCity(cityName, location, creator);
        cachedCities.put(cityName, city);
        return city;
    }

    public void deleteCity(String cityName) throws RaidCraftException {

        City city = getCity(cityName);
        if(city == null) {
            throw new RaidCraftException("Es wurde keine Stadt mit dem namen gefunden!");
        }

        city.delete();
    }

    public City getCity(String name) {

        City city = cachedCities.get(name);

        if(city == null) {
            TCity tCity = RaidCraft.getDatabase(RCCitiesPlugin.class).find(TCity.class).where().ieq("name", name).findUnique();
            if(tCity != null) {
                city = new DatabaseCity(tCity);
                cachedCities.put(tCity.getName(), city);
            }
        }
        return city;
    }

    public Collection<City> getCities() {

        for(TCity tCity : RaidCraft.getDatabase(RCCitiesPlugin.class).find(TCity.class).findList()) {

            if(!cachedCities.containsKey(tCity.getName())) {
                cachedCities.put(tCity.getName(), new DatabaseCity(tCity));
            }
        }
        return cachedCities.values();
    }

    public void clearCache() {

        cachedCities.clear();
    }
}
