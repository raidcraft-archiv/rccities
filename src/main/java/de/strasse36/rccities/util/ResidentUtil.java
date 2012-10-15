package de.strasse36.rccities.util;

import com.silthus.raidcraft.util.RCMessaging;
import de.strasse36.rccities.City;
import de.strasse36.rccities.Resident;
import de.strasse36.rccities.database.TableHandler;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;

/**
 * Author: Philip Urban
 * Date: 01.03.12 - 15:37
 * Description:
 */
public class ResidentUtil {

    public static Map<String, City> invites = new HashMap<String, City>();

    public static Resident isResident(String name, City city)
    {


        Resident resident = TableHandler.get().getResidentTable().getResident(name);
        if(resident == null || resident.getCity() == null)
            return null;
        if(resident.getCity().getName().equalsIgnoreCase(city.getName()))
        {
            return resident;
        }
        return null;
    }

    public static void sentInvitation(Player invited, City city) {
        invites.put(invited.getName(), city);
        RCMessaging.send(invited, "----------------------------------------------", false);
        RCMessaging.send(invited, RCMessaging.blue("Du wurdest in die Stadt " + city.getName() + " eingeladen!"), false);
        RCMessaging.send(invited, RCMessaging.blue("Bestätige die Einladung mit /town accept"), false);
        RCMessaging.send(invited, "----------------------------------------------", false);
    }
}
