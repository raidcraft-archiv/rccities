package de.strasse36.rccities.util;

import com.silthus.raidcraft.util.RCMessaging;
import de.strasse36.rccities.City;
import de.strasse36.rccities.Resident;
import de.strasse36.rccities.database.TableHandler;
import de.strasse36.rccities.exceptions.UnknownProfessionException;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

/**
 * Author: Philip Urban
 * Date: 29.02.12 - 00:03
 * Description:
 */
public class Profession {
    
    public enum Professions {
        
        RESIDENT("resident", "Bürger"),
        ASSISTANT("assistant", "Stadtassistent"),
        VICEMAYOR("vicemayor", "Vize-Bürgermeister"),
        MAYOR("mayor", "Bürgermeister");
        
        private String dBname;
        private String name;
        
        Professions(String dBname, String name) {
            this.dBname = dBname;
            this.name = name;
        }
        
        public String getDbName() {
            return dBname;
        }
        
        public String getName() {
            return name;
        }
    } 

    public static void setMayor(Player player, City city) {
        setMayor(player.getName(), city);
    }

    public static void setMayor(String player, City city)
    {
        Resident resident = new Resident(player);
        resident.setProfession(city, player);
        TableHandler.get().getResidentTable().updateResident(resident);
        Player bukkitPlayer = Bukkit.getPlayer(player);
        if(bukkitPlayer != null && bukkitPlayer.isOnline())
            RCMessaging.send(bukkitPlayer, RCMessaging.blue("Du bist nun der Bürgermeister von " + city.getName() + "!"), false);
    }
}
