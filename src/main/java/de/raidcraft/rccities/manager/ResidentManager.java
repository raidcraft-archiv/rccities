package de.raidcraft.rccities.manager;

import de.raidcraft.RaidCraft;
import de.raidcraft.api.RaidCraftException;
import de.raidcraft.rccities.DatabaseResident;
import de.raidcraft.rccities.RCCitiesPlugin;
import de.raidcraft.rccities.api.city.City;
import de.raidcraft.rccities.api.resident.Resident;
import de.raidcraft.rccities.api.resident.Role;
import de.raidcraft.rccities.api.resident.RolePermission;
import de.raidcraft.rccities.tables.TResident;
import de.raidcraft.util.CaseInsensitiveMap;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author Philip Urban
 */
public class ResidentManager {

    private RCCitiesPlugin plugin;
    private Map<String, List<Resident>> cachedResidents = new CaseInsensitiveMap<>();

    public ResidentManager(RCCitiesPlugin plugin) {

        this.plugin = plugin;
    }

    public void broadcastCityMessage(City city, String message) {

        for(Resident resident : getResidents(city)) {

            Player player = resident.getPlayer();
            if(player != null) {
                player.sendMessage(ChatColor.GOLD + message);
            }
        }
    }

    public Resident addResident(City city, Player player) throws RaidCraftException {

        Resident resident = getResident(player.getName(), city);
        if(resident != null) {
            throw new RaidCraftException(player.getName() + " ist bereits Einwohner von '" + city.getFriendlyName() + "'!");
        }

        return new DatabaseResident(player.getName(), Role.RESIDENT, city);
    }

    public void removeFromCache(Resident resident) {

        cachedResidents.remove(resident.getName());
    }

    public void printResidentInfo(String playerName, CommandSender sender) {

        List<Resident> citizenships = getCitizenships(playerName);
        if(citizenships == null || citizenships.size() == 0) {
            sender.sendMessage(ChatColor.RED + "Keine Einwohner Informationen zu '" + playerName + "' gefunden!");
        }

        sender.sendMessage("*********************************");
        sender.sendMessage(ChatColor.GOLD + "Einwohner Informationen zu '" + ChatColor.YELLOW + citizenships.get(0).getName() + ChatColor.GOLD + "'");
        if(citizenships.size() == 1) {
            sender.sendMessage(ChatColor.GOLD + "Bürgerschaft in " + ChatColor.YELLOW + citizenships.size() + ChatColor.GOLD + " Stadt:");
        }
        else {
            sender.sendMessage(ChatColor.GOLD + "Bürgerschaft in " + ChatColor.YELLOW + citizenships.size() + ChatColor.GOLD + " Städten:");
        }
        for(Resident resident : citizenships) {
            sender.sendMessage(ChatColor.GOLD + resident.getCity().getFriendlyName() + " : " + ChatColor.YELLOW + resident.getRole().getFriendlyName());
        }
        sender.sendMessage("*********************************");
    }

    public List<Resident> getCitizenships(String name) {

        List<Resident> residents = cachedResidents.get(name);

        if(residents == null || residents.size() == 0) {
            List<TResident> tResidents = RaidCraft.getDatabase(RCCitiesPlugin.class).find(TResident.class).where().ieq("name", name).findList();
            if(tResidents != null && tResidents.size() > 0) {
                cachedResidents.put(name, new ArrayList<Resident>());
                for(TResident tResident : tResidents) {
                    Resident resident = new DatabaseResident(tResident);
                    cachedResidents.get(name).add(resident);
                }
            }
        }
        return cachedResidents.get(name);
    }

    public List<Resident> getCitizenships(String name, RolePermission permission) {

        List<Resident> residents = getCitizenships(name);
        List<Resident> citizenships = new ArrayList<>();

        if(residents == null) return null;

        for(Resident resident : residents) {
            if(resident.getRole().hasPermission(permission)) {
                citizenships.add(resident);
            }
        }

        if(citizenships.size() == 0) return null;

        return citizenships;
    }

    public Resident getResident(String name, City city) {

        List<Resident> residents = getCitizenships(name);

        if(residents != null) {
            for(Resident resident : residents) {
                if(resident.getCity().equals(city)) {
                    return resident;
                }
            }
        }

        return null;
    }

    public List<Resident> getResidents(City city) {

        List<Resident> residents = new ArrayList<>();
        List<TResident> tResidents = RaidCraft.getDatabase(RCCitiesPlugin.class).find(TResident.class).where().eq("city_id", city.getId()).findList();
        for(TResident tResident : tResidents) {
            if(!cachedResidents.containsKey(tResident.getName())) {
                cachedResidents.put(tResident.getName(), new ArrayList<Resident>());
            }
            boolean exist = false;
            for(Resident resident : cachedResidents.get(tResident.getName())) {
                if(resident.getCity().equals(city)) {
                    residents.add(resident);
                    exist = true;
                    break;
                }
            }
            if(!exist) {
                Resident resident = new DatabaseResident(tResident);
                cachedResidents.get(resident.getName()).add(resident);
                residents.add(resident);
            }
        }

        return residents;
    }

    public void clearCache() {

        cachedResidents.clear();
    }
}
