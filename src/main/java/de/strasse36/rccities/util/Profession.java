package de.strasse36.rccities.util;

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
    }
}
