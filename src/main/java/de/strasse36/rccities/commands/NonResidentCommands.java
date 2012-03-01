package de.strasse36.rccities.commands;

import com.silthus.raidcraft.util.RCMessaging;
import de.strasse36.rccities.Resident;
import de.strasse36.rccities.util.TableHandler;
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
        Resident resident = new Resident(sender.getName(), CityStaffCommands.invites.get(sender.getName()), "resident");
        TableHandler.get().getResidentTable().updateResident(resident);
        RCMessaging.send(sender, "Du bist nun Einwohner von " + CityStaffCommands.invites.get(sender.getName()).getName() + "!");
    }
}
