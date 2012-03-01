package de.strasse36.rccities.commands;

import com.silthus.raidcraft.util.RCMessaging;
import de.strasse36.rccities.City;
import de.strasse36.rccities.Resident;
import de.strasse36.rccities.util.TableHandler;
import de.strasse36.rccities.util.TownMessaging;
import org.bukkit.command.CommandSender;

/**
 * Author: Philip Urban
 * Date: 01.03.12 - 14:53
 * Description:
 */
public class NonResidentCommands {

    public static void acceptTownInvite(CommandSender sender)
    {
        if(!CityStaffCommands.invites.containsKey(sender.getName()))
        {
            RCMessaging.warn(sender, "Du hast keine offenen Einladungen!");
            return;
        }
        City city = CityStaffCommands.invites.get(sender.getName());
        Resident resident = new Resident(sender.getName(), city, "resident");
        TownMessaging.sendTownResidents(city, sender.getName() + " ist nun Einwohner von " + city.getName() + "!");
        TableHandler.get().getResidentTable().updateResident(resident);
        RCMessaging.send(sender, "Du bist nun Einwohner von " + city.getName() + "!");

    }
}
