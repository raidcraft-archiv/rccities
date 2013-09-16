package de.raidcraft.rccities.manager;

import de.raidcraft.RaidCraft;
import de.raidcraft.api.RaidCraftException;
import de.raidcraft.rccities.DatabaseCity;
import de.raidcraft.rccities.RCCitiesPlugin;
import de.raidcraft.rccities.api.city.City;
import de.raidcraft.rccities.api.resident.Resident;
import de.raidcraft.rccities.api.resident.Role;
import de.raidcraft.rccities.tables.TCity;
import de.raidcraft.util.CaseInsensitiveMap;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;

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

        if(cityName.length() > 20) {
            throw new RaidCraftException("Der angegebene Stadtname ist zu lange!");
        }
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
            throw new RaidCraftException("Es wurde keine Stadt mit diesem namen gefunden!");
        }

        city.delete();
        cachedCities.remove(city.getName());
    }

    public void printCityInfo(String cityName, CommandSender sender) throws RaidCraftException {

        City city = getCity(cityName);
        if(city == null) {
            throw new RaidCraftException("Es wurde keine Stadt mit diesem namen gefunden!");
        }

        String mayorList = "";
        int mayorCount = 0;
        String residentList = "";
        int residentCount = 0;
        for(Resident resident : plugin.getResidentManager().getResidents(city)) {
            if(resident.getRole() == Role.MAYOR) {
                if(!mayorList.isEmpty()) mayorList += ChatColor.GRAY + ", ";
                mayorList += ChatColor.YELLOW + resident.getName();
                mayorCount++;
            }
            else {
                if(!residentList.isEmpty()) residentList += ChatColor.GRAY + ", ";
                residentList += ChatColor.YELLOW + resident.getName();
                residentCount++;
            }
        }

        sender.sendMessage("*********************************");
        sender.sendMessage(ChatColor.GOLD + "Informationen zur Stadt '" + ChatColor.YELLOW + city.getFriendlyName() + ChatColor.GOLD + "'");
        sender.sendMessage(ChatColor.GOLD + "Gründungsdatum: " + ChatColor.YELLOW + city.getCreationDate().toString());
        sender.sendMessage(ChatColor.GOLD + "Größe (Chunks): " + ChatColor.YELLOW + city.getSize());
        sender.sendMessage(ChatColor.GOLD + "Unclaimed Plots: " + ChatColor.YELLOW + city.getPlotCredit());
        sender.sendMessage(ChatColor.GOLD + "Bürgermeister (" + mayorCount + "): " + ChatColor.YELLOW + mayorList);
        sender.sendMessage(ChatColor.GOLD + "Einwohner (" + residentCount + "): " + ChatColor.YELLOW + residentList);
        sender.sendMessage("*********************************");
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
        // search for name parts
        if(city == null) {
            getCities();
            for(Map.Entry<String, City> entry : cachedCities.entrySet()) {
                if(entry.getKey().toLowerCase().startsWith(name.toLowerCase())) {
                    city = entry.getValue();
                    break;
                }
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
