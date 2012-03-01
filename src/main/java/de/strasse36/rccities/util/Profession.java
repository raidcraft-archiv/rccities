package de.strasse36.rccities.util;

import com.silthus.raidcraft.database.UnknownTableException;
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
        try {
            TableGetter.getResidentTable().updateResident(resident);
        } catch (UnknownTableException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
    }
}
