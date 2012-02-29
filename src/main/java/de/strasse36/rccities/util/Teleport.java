package de.strasse36.rccities.util;

import de.strasse36.rccities.City;
import org.bukkit.entity.Player;

/**
 * Author: Philip Urban
 * Date: 29.02.12 - 00:44
 * Description:
 */
public class Teleport {
    
    public static void teleportPlayer(Player player, City city)
    {
        player.teleport(city.getSpawn());
    }
}
