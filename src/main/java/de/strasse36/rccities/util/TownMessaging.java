package de.strasse36.rccities.util;

import com.silthus.raidcraft.util.RCMessaging;
import de.strasse36.rccities.City;
import de.strasse36.rccities.Resident;
import de.strasse36.rccities.database.TableHandler;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.Iterator;
import java.util.List;

/**
 * Author: Philip Urban
 * Date: 01.03.12 - 15:05
 * Description:
 */
public class TownMessaging {
    
    public static void sendTownStaff(City city, String msg)
    {
        List<Resident> residentlist = TableHandler.get().getResidentTable().getResidents(city);
        Iterator<Resident> iterator = residentlist.iterator();
        while (iterator.hasNext()) {
            if(iterator.next().isStaff())
            {
                Player player = Bukkit.getPlayer(iterator.next().getName());
                if(player != null)
                    RCMessaging.send(player, RCMessaging.blue(msg), false);
            }
        }
    }
    
    public static void sendTownResidents(City city, String msg)
    {
        List<Resident> residentlist = TableHandler.get().getResidentTable().getResidents(city);
        Iterator<Resident> iterator = residentlist.iterator();
        while (iterator.hasNext()) {
                Player player = Bukkit.getPlayer(iterator.next().getName());
                if(player != null)
                    RCMessaging.send(player, RCMessaging.blue(msg), false);
        }
    }
    
    public static void broadcast(String msg) {
        RCMessaging.broadcast(RCMessaging.blue(msg), false);
    }
}
