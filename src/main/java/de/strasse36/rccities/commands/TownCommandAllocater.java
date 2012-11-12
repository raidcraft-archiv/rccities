package de.strasse36.rccities.commands;

import com.silthus.raidcraft.util.RCMessaging;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import java.util.HashMap;
import java.util.Map;

/**
 * Author: Philip Urban
 * Date: 26.02.12 - 20:14
 * Description:
 */
public class TownCommandAllocater implements CommandExecutor {
    private final static int COMMAND_COOLDOWN = 5;
    private Map<String, Long> cooldowns = new HashMap<String, Long>();
    
    public boolean onCommand(CommandSender sender, Command cmd, String s, String[] args) {
        String cmdStr;

        /////////////////////////////RESIDENTS//////////////////////////////////////////
        //mark town info
        if(args.length == 0 || (args.length > 1 && args[0].equals("info")))
        {
            ResidentCommands.showTownInfo(sender, args);
            return true;
        }

        if(args.length > 0)
        {
            //leave town
            if(args[0].equals("leave"))
            {
                ResidentCommands.leaveTown(sender);
                return true;
            }

            //teleport player to townspawn
            if(args[0].equals("spawn"))
            {
                ResidentCommands.townspawn(sender, args);
                return true;
            }

            //deposit into townbank
            if(args[0].equals("deposit"))
            {
                ResidentCommands.deposit(sender, args);
                return true;
            }

            /////////////////////////////CITY-STAFF//////////////////////////////////////////
            //mayor set townspawn
            if(args[0].equals("setspawn"))
            {
                CityStaffCommands.setspawn(sender);
                return true;
            }

            //promote residents
            if(args[0].equals("promote"))
            {
                CityStaffCommands.promote(sender, args);
                return true;
            }

            //kick player from town
            if(args[0].equals("kick"))
            {
                CityStaffCommands.kick(sender, args);
                return true;
            }

            //mayor & assistants invite player
            if(args[0].equals("invite"))
            {
                CityStaffCommands.invite(sender, args);
                return true;
            }

            //change city description
            ///town set desc <description>
            if(args[0].equals("setdesc"))
            {
                CityStaffCommands.setdesc(sender, args);
                return true;
            }

            //mark region farewell & greetings
            if(args[0].equals("greetings"))
            {
                if(checkCooldown(sender, "weCmd")) {
                    return true;
                }
                CityStaffCommands.greetings(sender, args);
                return true;
            }

            //pvp
            if(args[0].equals("pvp"))
            {
                if(checkCooldown(sender, "weCmd")) {
                    return true;
                }
                CityStaffCommands.pvp(sender, args);
                return true;
            }

            //tnt
            if(args[0].equals("tnt"))
            {
                if(checkCooldown(sender, "weCmd")) {
                    return true;
                }
                CityStaffCommands.tnt(sender, args);
                return true;
            }

            //tnt
            if(args[0].equals("mobs"))
            {
                if(checkCooldown(sender, "weCmd")) {
                    return true;
                }
                CityStaffCommands.mobspawn(sender, args);
                return true;
            }

            //withdraw from townbank
            if(args[0].equals("withdraw"))
            {
                CityStaffCommands.withdraw(sender, args);
                return true;
            }

            //flow
            if(args[0].equals("flow"))
            {
                CityStaffCommands.townflow(sender, args);
                return true;
            }

            /////////////////////////////MODERATOR//////////////////////////////////////////

            //relaod config and shit
            if(args[0].equals("reload"))
            {
                ModCommands.reload(sender, args);
                return true;
            }

            //create city and nominate mayor
            if(args[0].equals("create"))
            {
                ModCommands.createCity(sender, args);
                return true;
            }

            //promote player as mayor
            if(args[0].equals("mayor"))
            {
                ModCommands.setMayor(sender, args);
                return true;
            }

            //change city name
            ///town set name <city> <name>
            if(args[0].equals("setname"))
            {
                ModCommands.setCityName(sender, args);
                return true;
            }

            //demolish city
            if(args[0].equals("demolish"))
            {
                ModCommands.demolishCity(sender, args);
                return true;
            }

            //confirm demolish city
            if(args[0].equals("confirm"))
            {
                ModCommands.confirmDemolish(sender, args);
                return true;
            }

            /////////////////////////////NON-RESIDENTS//////////////////////////////////////////
            //mayor & assistants invite player
            if(args[0].equals("accept"))
            {
                NonResidentCommands.acceptTownInvite(sender);
                return true;
            }

            //list all towns
            if(args[0].equals("list"))
            {
                NonResidentCommands.listTowns(sender);
                return true;
            }

            //resident info
            if(args[0].equals("resident"))
            {
                NonResidentCommands.resident(sender, args);
                return true;
            }

            /////////////////////////////OTHERS//////////////////////////////////////////
            //help
            if(args[0].equals("help"))
            {
                TownCommandUtility.help(sender);
                return true;
            }
        }

        RCMessaging.warn(sender, "Der eigengebene Befehl konnte nicht zugeordnet werden!");
        RCMessaging.warn(sender, "'/town help' zeigt alle verfügbaren Befehle an!");
        return true;
    }

    private boolean checkCooldown(CommandSender sender, String type) {
        if(cooldowns.containsKey(type) && cooldowns.get(type) + COMMAND_COOLDOWN > System.currentTimeMillis() / 1000) {
            RCMessaging.warn(sender, "Du musst kurz warten bis du diesen Befehl ausführen kannst!");
            return true;
        }
        else {
            cooldowns.put(type, System.currentTimeMillis() / 1000);
        }
        return false;
    }

}
