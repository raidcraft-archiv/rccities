package de.strasse36.rccities.util;

import com.silthus.raidcraft.util.RCMessaging;
import com.sk89q.worldedit.BlockVector;
import com.sk89q.worldguard.domains.DefaultDomain;
import com.sk89q.worldguard.protection.flags.DefaultFlag;
import com.sk89q.worldguard.protection.regions.ProtectedCuboidRegion;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import de.strasse36.rccities.City;
import de.strasse36.rccities.Plot;
import de.strasse36.rccities.Resident;
import org.bukkit.Chunk;
import org.bukkit.Location;

import java.util.List;

/**
 * Author: Philip Urban
 * Date: 06.03.12 - 19:13
 * Description:
 */
public class ChunkUtil {

    public static BlockVector[] getBlockVectors(Location location)
    {
        Chunk chunk = location.getChunk();
        BlockVector blockVector1 = new BlockVector(
            chunk.getX()*16,
            0,
            chunk.getZ()*16
        );
        BlockVector blockVector2 = new BlockVector(
                (chunk.getX()*16)+15,
                location.getWorld().getMaxHeight(),
                (chunk.getZ()*16)+15
        );

        //RCMessaging.broadcast("X: " + blockVector1.getX() + " | Y: " + blockVector1.getY() + " | Z: " + blockVector1.getZ());
        //RCMessaging.broadcast("X: " + blockVector2.getX() + " | Y: " + blockVector2.getY() + " | Z: " + blockVector2.getZ());
        BlockVector[] blockVectors = {
                blockVector1,
                blockVector2
        };
        return blockVectors;
    }
    
    public static ProtectedRegion getProtectedCuboidRegion(String id, Location location)
    {
        BlockVector[] blockVectors = ChunkUtil.getBlockVectors(location);
        ProtectedRegion region = new ProtectedCuboidRegion(id, blockVectors[0], blockVectors[1]);
        return region;
    }
    
    public static void updatePlotOwner(City city)
    {
        DefaultDomain leaders = new DefaultDomain();
        List<Resident> residentList = TableHandler.get().getResidentTable().getResidents(city);
        for(Resident resident : residentList)
        {
            if(resident.isStaff())
            {
                leaders.addPlayer(resident.getName());
            }
        }
        
        List<Plot> plotList = TableHandler.get().getPlotTable().getPlots(city);
        for(Plot plot : plotList)
        {
            WorldGuardManager.getRegion(plot.getRegionId()).setOwners(leaders);
        }
        WorldGuardManager.save();
    }

    public static void updateChunkMessages(City city)
    {
        List<Plot> plotList = TableHandler.get().getPlotTable().getPlots(city);
        String greetingMessage;
        String member;
        for(Plot plot : plotList)
        {
            if(city.isGreetings())
            {
                member = RCMessaging.green(WorldGuardManager.getRegion(plot.getRegionId()).getMembers().toUserFriendlyString());
                greetingMessage = "";
                if(plot.isPvp())
                    greetingMessage = RCMessaging.red("~PVP~ ");
                if(plot.isOpen())
                    greetingMessage += RCMessaging.green("~öffentlich~");
                else
                    greetingMessage += member;

                if(WorldGuardManager.getRegion(plot.getRegionId()).getMembers().size() == 0)
                {
                    greetingMessage += RCMessaging.green("~kein Besitzer~");
                }
            }
            else
                greetingMessage = null;

            WorldGuardManager.getRegion(plot.getRegionId()).setFlag(DefaultFlag.GREET_MESSAGE, greetingMessage);
            WorldGuardManager.save();
        }
    }
    
    public static void setPublic(City city)
    {
        DefaultDomain residents = new DefaultDomain();
        List<Resident> residentList = TableHandler.get().getResidentTable().getResidents(city);
        for(Resident resident : residentList)
        {
            residents.addPlayer(resident.getName());
        }

        List<Plot> plotList = TableHandler.get().getPlotTable().getPlots(1);
        for(Plot plot : plotList)
        {
            WorldGuardManager.getRegion(plot.getRegionId()).setMembers(residents);
        }
        WorldGuardManager.save();

        //update chunk messages
        updateChunkMessages(city);
    }
}
