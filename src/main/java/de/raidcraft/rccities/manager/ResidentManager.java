package de.raidcraft.rccities.manager;

import de.raidcraft.RaidCraft;
import de.raidcraft.api.RaidCraftException;
import de.raidcraft.api.player.UnknownPlayerException;
import de.raidcraft.rccities.DatabaseResident;
import de.raidcraft.rccities.RCCitiesPlugin;
import de.raidcraft.rccities.api.city.City;
import de.raidcraft.rccities.api.resident.Resident;
import de.raidcraft.rccities.api.resident.Role;
import de.raidcraft.rccities.api.resident.RolePermission;
import de.raidcraft.rccities.tables.TJoinRequest;
import de.raidcraft.rccities.tables.TResident;
import de.raidcraft.skills.SkillsPlugin;
import de.raidcraft.skills.api.exceptions.UnknownSkillException;
import de.raidcraft.skills.api.hero.Hero;
import de.raidcraft.skills.api.skill.Skill;
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

        String prefix = ChatColor.DARK_GRAY + "[" + ChatColor.YELLOW + city.getFriendlyName() + ChatColor.DARK_GRAY + "] ";

        for(Resident resident : getResidents(city)) {

            Player player = resident.getPlayer();
            if(player != null) {
                player.sendMessage(prefix + ChatColor.GOLD + message);
            }
        }
    }

    public void broadcastCityMessage(City city, String message, RolePermission receiverPermission) {

        String prefix = ChatColor.DARK_GRAY + "[" + ChatColor.YELLOW + city.getFriendlyName() + ChatColor.DARK_GRAY + "] ";

        for(Resident resident : getResidents(city)) {

            if(!resident.getRole().hasPermission(receiverPermission)) continue;
            Player player = resident.getPlayer();
            if(player != null) {
                player.sendMessage(prefix + ChatColor.GOLD + message);
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

    public Resident addResident(City city, String playerName) throws RaidCraftException {

        Resident resident = getResident(playerName, city);
        if(resident != null) {
            throw new RaidCraftException(playerName + " ist bereits Einwohner von '" + city.getFriendlyName() + "'!");
        }

        return new DatabaseResident(playerName, Role.RESIDENT, city);
    }

    public void removeFromCache(Resident resident) {

        cachedResidents.remove(resident.getName());
    }

    public void printResidentInfo(String playerName, CommandSender sender) {

        List<Resident> citizenships = getCitizenships(playerName);
        if(citizenships == null || citizenships.size() == 0) {
            sender.sendMessage(ChatColor.RED + "Keine Einwohner Informationen zu '" + playerName + "' gefunden!");
            return;
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

    public void deleteOtherJoinRequests(String playerName, City exceptedCity) {

        List<TJoinRequest> tJoinRequests = RaidCraft.getDatabase(RCCitiesPlugin.class)
                .find(TJoinRequest.class).where().ieq("player", playerName).ne("city_id", exceptedCity.getId()).findList();
        if(tJoinRequests == null || tJoinRequests.size() == 0) return;
        RaidCraft.getDatabase(RCCitiesPlugin.class).delete(tJoinRequests);
    }

    public void addPrefixSkill(Resident resident) {

        if(!resident.getRole().hasPermission(RolePermission.PREFIX_SKILL)) return;

        try {
            Hero hero = RaidCraft.getComponent(SkillsPlugin.class).getCharacterManager().getHero(resident.getName());
            Skill skill = RaidCraft.getComponent(SkillsPlugin.class).getSkillManager().getSkill(hero, hero.getVirtualProfession(), "prefix-" + resident.getCity().getName().toLowerCase());
            if (skill.isUnlocked()) {
                return;
            }
            hero.addSkill(skill);
        } catch (UnknownSkillException e) {
            RaidCraft.LOGGER.warning("[RCCities] No prefix skill found for city '" + resident.getCity().getFriendlyName() + "'!");
        }
        catch (UnknownPlayerException e) {
            RaidCraft.LOGGER.warning("[RCCities] No hero found for resident '" + resident.getName() + "'!");
        }
    }

    public void removePrefixSkill(Resident resident) {

        try {
            Hero hero = RaidCraft.getComponent(SkillsPlugin.class).getCharacterManager().getHero(resident.getName());
            Skill skill = RaidCraft.getComponent(SkillsPlugin.class).getSkillManager().getSkill(hero, hero.getVirtualProfession(), "prefix-" + resident.getCity().getName().toLowerCase());
            if (!skill.isUnlocked()) {
                return;
            }
            hero.removeSkill(skill);
        } catch (UnknownSkillException e) {
            RaidCraft.LOGGER.warning("[RCCities] No prefix skill found for city '" + resident.getCity().getFriendlyName() + "'!");
        }
        catch (UnknownPlayerException e) {
            RaidCraft.LOGGER.warning("[RCCities] No hero found for resident '" + resident.getName() + "'!");
        }
    }

    public List<Resident> getCitizenships(String name) {

        List<Resident> residents = cachedResidents.get(name);

        if(residents == null || residents.size() == 0) {
            List<TResident> tResidents = RaidCraft.getDatabase(RCCitiesPlugin.class).find(TResident.class).where().ieq("name", name).findList();
            if(tResidents != null && tResidents.size() > 0) {
                cachedResidents.put(name, new ArrayList<Resident>());
                for(TResident tResident : tResidents) {
                    Resident resident = new DatabaseResident(tResident);
                    if(resident.getCity() != null) {
                        cachedResidents.get(name).add(resident);
                    }
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
