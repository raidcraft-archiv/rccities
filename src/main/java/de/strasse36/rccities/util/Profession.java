package de.strasse36.rccities.util;

import com.silthus.raidcraft.util.RCMessaging;
import de.strasse36.rccities.City;
import de.strasse36.rccities.Resident;
import org.bukkit.entity.Player;

/**
 * Author: Philip Urban
 * Date: 29.02.12 - 00:03
 * Description:
 */
public class Profession {
    
    public static void setMayor(Player player, City city)
    {
        Resident resident = new Resident(player.getName(), city, "mayor");
        TableHandler.get().getResidentTable().updateResident(resident);
        if(player.isOnline())
            RCMessaging.send(player, "Du bist nun der Bürgermeister von " + city.getName() + "!");
    }
}
