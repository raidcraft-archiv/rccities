package de.strasse36.rccities.commands;

import com.silthus.raidcraft.util.RCMessaging;
import com.sk89q.worldguard.protection.flags.DefaultFlag;
import com.sk89q.worldguard.protection.flags.Flag;
import com.sk89q.worldguard.protection.flags.StateFlag;
import de.strasse36.rccities.City;
import de.strasse36.rccities.Plot;
import de.strasse36.rccities.Resident;
import de.strasse36.rccities.database.TableHandler;
import de.strasse36.rccities.util.ChunkUtil;
import de.strasse36.rccities.util.TownMessaging;
import de.strasse36.rccities.util.WorldGuardManager;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;

import java.util.List;

/**
 * Author: Philip
 * Date: 26.08.12 - 20:59
 * Description:
 */
public class CommandUtility {
    public static Resident checkAndGetResidentLeader(CommandSender sender) {
        Resident resident = TableHandler.get().getResidentTable().getResident(sender.getName());
        //no resident
        if(resident == null || resident.getCity() == null)
        {
            TownCommandUtility.noResident(sender);
            return null;
        }

        //no leader
        if(!resident.isLeadership() && !sender.hasPermission("rccities.mod"))
        {
            TownCommandUtility.noLeadership(sender);
            return null;
        }
        return resident;
    }

    public static boolean toggleCityFlag(City city, Flag flag, String onOff) {
        List<Plot> plotList = TableHandler.get().getPlotTable().getPlots(city);

        if(onOff.equalsIgnoreCase("on"))
        {
            for(Plot plot : plotList)
            {
                WorldGuardManager.getRegion(plot.getRegionId()).setFlag(flag, StateFlag.State.ALLOW);
            }
            WorldGuardManager.save();
            TownMessaging.sendTownResidents(city, flag.getName().toUpperCase() + " ist nun in in der Stadt erlaubt!");
            //update chunk messages
            ChunkUtil.updateChunkMessages(city);
            return true;
        }

        if(onOff.equalsIgnoreCase("off"))
        {
            for(Plot plot : plotList)
            {
                if(flag != DefaultFlag.PVP || !plot.isPvp())
                    WorldGuardManager.getRegion(plot.getRegionId()).setFlag(flag, StateFlag.State.DENY);
            }
            WorldGuardManager.save();
            TownMessaging.sendTownResidents(city, flag.getName().toUpperCase() + " ist nun in in der Stadt verboten!");
            //update chunk messages
            ChunkUtil.updateChunkMessages(city);
            return true;
        }

        return false;
    }
    
    public static boolean togglePlotFlag(Resident resident, Plot plot, Flag flag, String onOff) {
        if(onOff.equalsIgnoreCase("on"))
        {
            WorldGuardManager.getRegion(plot.getRegionId()).setFlag(flag, StateFlag.State.ALLOW);
            WorldGuardManager.save();
            RCMessaging.send(Bukkit.getPlayer(resident.getName()), RCMessaging.blue(flag.getName().toUpperCase() + " ist nun in diesem Chunk erlaubt!"), false);
            return true;
        }

        if(onOff.equalsIgnoreCase("off"))
        {
            WorldGuardManager.getRegion(plot.getRegionId()).setFlag(flag, StateFlag.State.DENY);
            WorldGuardManager.save();
            RCMessaging.send(Bukkit.getPlayer(resident.getName()), RCMessaging.blue(flag.getName().toUpperCase() + " ist nun in diesem Chunk verboten!"), false);
            return true;
        }
        return false;
    }
}
