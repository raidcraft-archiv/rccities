package de.strasse36.rccities.util;

import com.silthus.raidcraft.util.RCMessaging;
import com.silthus.raidcraft.util.Task;
import com.sk89q.worldedit.BlockVector;
import com.sk89q.worldguard.domains.DefaultDomain;
import com.sk89q.worldguard.protection.flags.DefaultFlag;
import com.sk89q.worldguard.protection.flags.StateFlag;
import com.sk89q.worldguard.protection.regions.ProtectedCuboidRegion;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import de.strasse36.rccities.Assignment;
import de.strasse36.rccities.City;
import de.strasse36.rccities.Plot;
import de.strasse36.rccities.Resident;
import de.strasse36.rccities.bukkit.RCCitiesPlugin;
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
        return new BlockVector[]{
                blockVector1,
                blockVector2
        };
    }
    
    public static ProtectedRegion getProtectedCuboidRegion(String id, Location location)
    {
        BlockVector[] blockVectors = ChunkUtil.getBlockVectors(location);
        return new ProtectedCuboidRegion(id, blockVectors[0], blockVectors[1]);
    }
    
    public static void updatePlotOwner(City city)
    {
        Task task = new Task(RCCitiesPlugin.get(), city)
        {
            @Override
            public void run()
            {
                final City city = (City)getArg(0);
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
                if(plotList == null)
                    return;
                for(Plot plot : plotList)
                {
                    ProtectedRegion protectedRegion = WorldGuardManager.getRegion(plot.getRegionId());
                    if(protectedRegion == null)
                    {
                        protectedRegion = restoreCityPlot(plot);
                    }
                    protectedRegion.setOwners(leaders);
                }
            }
        };
        task.start(true);

        //save worldguard yaml after few seconds
        saveWorldGuardDelayed();
    }

    public static void updateChunkMessages(City city)
    {
        //update messages in async task
        Task task = new Task(RCCitiesPlugin.get(), city)
        {
            @Override
            public void run()
            {
                final City city = (City)getArg(0);
                List<Plot> plotList = TableHandler.get().getPlotTable().getPlots(city);
                String greetingMessage;
                String member;
                for(Plot plot : plotList)
                {
                    if(city.isGreetings())
                    {
                        ProtectedRegion protectedRegion = WorldGuardManager.getRegion(plot.getRegionId());
                        if(protectedRegion == null)
                        {
                            protectedRegion = restoreCityPlot(plot);
                        }
                        member = RCMessaging.green(protectedRegion.getMembers().toUserFriendlyString());
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
                }
            }
        };
        task.start(true);

        //save worldguard yaml after few seconds
        saveWorldGuardDelayed();
    }
    
    public static void setPublic(City city)
    {
        Task task = new Task(RCCitiesPlugin.get(), city)
        {
            @Override
            public void run()
            {
                final City city = (City)getArg(0);
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
            }
        };
        task.start(true);

        //update chunk messages
        updateChunkMessages(city);
    }

    public static void saveWorldGuardDelayed()
    {
        Task task = new Task(RCCitiesPlugin.get())
        {
            @Override
            public void run()
            {
                WorldGuardManager.save();
            }
        };
        task.startDelayed(7*20);
    }

    public static ProtectedRegion restoreCityPlot(Plot plot)
    {
        //check if plot already exist
        ProtectedRegion region = WorldGuardManager.getRegion(plot.getRegionId());
        if(region != null)
            return region;
        
        //claim plot chunk
        Location chunkLocation = new Location(plot.getCity().getSpawn().getWorld(), plot.getX()*16, 0, plot.getZ()*16);
        ProtectedRegion newRegion = getProtectedCuboidRegion(plot.getRegionId(), chunkLocation);
        WorldGuardManager.getWorldGuard().getGlobalRegionManager().get(plot.getCity().getSpawn().getWorld()).addRegion(newRegion);
        WorldGuardManager.setTownFlags(plot.getRegionId());

        DefaultDomain members = new DefaultDomain();
        //set public
        if(plot.isOpen())
        {

            List<Resident> residentList = TableHandler.get().getResidentTable().getResidents(plot.getCity());
            for(Resident resident : residentList)
            {
                members.addPlayer(resident.getName());
            }
        }
        //set member
        else
        {
            List<Assignment> assignmentList = TableHandler.get().getAssignmentsTable().getAssignments(plot);
            for(Assignment assignment : assignmentList)
            {
                members.addPlayer(TableHandler.get().getResidentTable().getResident(assignment.getResident_id()).getName());
            }
        }
        WorldGuardManager.getRegion(plot.getRegionId()).setMembers(members);

        //set pvp
        if(plot.isPvp())
            WorldGuardManager.getRegion(plot.getRegionId()).setFlag(DefaultFlag.PVP, StateFlag.State.ALLOW);

        //set owner
        updatePlotOwner(plot.getCity());

        return newRegion;
    }
}