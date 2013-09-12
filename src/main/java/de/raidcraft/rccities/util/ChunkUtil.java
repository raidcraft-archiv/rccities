//package de.raidcraft.rccities.util;
//
//import com.sk89q.worldedit.BlockVector;
//import com.sk89q.worldguard.domains.DefaultDomain;
//import com.sk89q.worldguard.protection.ApplicableRegionSet;
//import com.sk89q.worldguard.protection.flags.DefaultFlag;
//import com.sk89q.worldguard.protection.flags.StateFlag;
//import com.sk89q.worldguard.protection.regions.ProtectedCuboidRegion;
//import com.sk89q.worldguard.protection.regions.ProtectedRegion;
//import de.raidcraft.rccities.RCCitiesPlugin;
//import de.raidcraft.rccities.api.city.City;
//import de.raidcraft.rccities.api.plot.Plot;
//import de.raidcraft.rccities.api.resident.Resident;
//import org.bukkit.Chunk;
//import org.bukkit.ChunkSnapshot;
//import org.bukkit.Location;
//import org.bukkit.Material;
//import org.bukkit.block.Block;
//import sun.org.mozilla.javascript.internal.ast.Assignment;
//
//import java.util.ArrayList;
//import java.util.List;
//
///**
// * Author: Philip Urban
// * Date: 06.03.12 - 19:13
// * Description:
// */
//public class ChunkUtil {
//
//    public static List<Block> markBackup = new ArrayList<Block>();
//
//    public static BlockVector[] getBlockVectors(Location location)
//    {
//        Chunk chunk = location.getChunk();
//        BlockVector blockVector1 = new BlockVector(
//                chunk.getX()*16,
//                0,
//                chunk.getZ()*16
//        );
//        BlockVector blockVector2 = new BlockVector(
//                (chunk.getX()*16)+15,
//                location.getWorld().getMaxHeight(),
//                (chunk.getZ()*16)+15
//        );
//
//        //RCMessaging.broadcast("X: " + blockVector1.getX() + " | Y: " + blockVector1.getY() + " | Z: " + blockVector1.getZ());
//        //RCMessaging.broadcast("X: " + blockVector2.getX() + " | Y: " + blockVector2.getY() + " | Z: " + blockVector2.getZ());
//        return new BlockVector[]{
//                blockVector1,
//                blockVector2
//        };
//    }
//
//    public static ProtectedRegion getProtectedCuboidRegion(String id, Location location)
//    {
//        BlockVector[] blockVectors = ChunkUtil.getBlockVectors(location);
//        return new ProtectedCuboidRegion(id, blockVectors[0], blockVectors[1]);
//    }
//
//    public static void updatePlotOwner(City city)
//    {
//        Task task = new Task(RCCitiesPlugin.get(), city)
//        {
//            @Override
//            public void run()
//            {
//                final City city = (City)getArg(0);
//                DefaultDomain leaders = new DefaultDomain();
//                List<Resident> residentList = TableHandler.get().getResidentTable().getResidents(city);
//                for(Resident resident : residentList)
//                {
//                    if(resident.isStaff())
//                    {
//                        leaders.addPlayer(resident.getName());
//                    }
//                }
//
//                List<Plot> plotList = TableHandler.get().getPlotTable().getPlots(city);
//                if(plotList == null)
//                    return;
//                for(Plot plot : plotList)
//                {
//                    ProtectedRegion protectedRegion = WorldGuardManager.getRegion(plot.getRegionId());
//                    if(protectedRegion == null)
//                    {
//                        protectedRegion = restoreCityPlot(plot);
//                    }
//                    protectedRegion.setOwners(leaders);
//                }
//            }
//        };
//        task.start(true);
//
//        //save worldguard yaml after few seconds
//        saveWorldGuardDelayed();
//    }
//
//    public static void updateChunkMessages(City city)
//    {
//        //update messages in async task
//        Task task = new Task(RCCitiesPlugin.get(), city)
//        {
//            @Override
//            public void run()
//            {
//                final City city = (City)getArg(0);
//                List<Plot> plotList = TableHandler.get().getPlotTable().getPlots(city);
//                String greetingMessage;
//                String member;
//                for(Plot plot : plotList)
//                {
//                    if(city.isGreetings())
//                    {
//                        ProtectedRegion protectedRegion = WorldGuardManager.getRegion(plot.getRegionId());
//                        if(protectedRegion == null)
//                        {
//                            protectedRegion = restoreCityPlot(plot);
//                        }
//                        member = RCMessaging.green(protectedRegion.getMembers().toUserFriendlyString());
//                        greetingMessage = "";
//                        if(plot.isPvp())
//                            greetingMessage += RCMessaging.red("~PVP~ ");
//                        if(protectedRegion.getFlag(DefaultFlag.MOB_SPAWNING) == StateFlag.State.ALLOW)
//                            greetingMessage += RCMessaging.red("~MOBS~ ");
//
//                        if(plot.isOpen())
//                            greetingMessage += RCMessaging.green("~öffentlich~");
//                        else
//                            greetingMessage += member;
//
//                        if(WorldGuardManager.getRegion(plot.getRegionId()).getMembers().size() == 0)
//                        {
//                            greetingMessage += RCMessaging.green("~kein Besitzer~");
//                        }
//                    }
//                    else
//                        greetingMessage = null;
//
//                    WorldGuardManager.getRegion(plot.getRegionId()).setFlag(DefaultFlag.GREET_MESSAGE, greetingMessage);
//                }
//            }
//        };
//        task.start(true);
//
//        //save worldguard yaml after few seconds
//        saveWorldGuardDelayed();
//    }
//
//    public static void setPublic(City city)
//    {
//        Task task = new Task(RCCitiesPlugin.get(), city)
//        {
//            @Override
//            public void run()
//            {
//                final City city = (City)getArg(0);
//                DefaultDomain residents = new DefaultDomain();
//                List<Resident> residentList = TableHandler.get().getResidentTable().getResidents(city);
//                for(Resident resident : residentList)
//                {
//                    residents.addPlayer(resident.getName());
//                }
//
//                List<Plot> plotList = TableHandler.get().getPlotTable().getPlots(city, true);
//                for(Plot plot : plotList)
//                {
//                    WorldGuardManager.getRegion(plot.getRegionId()).setMembers(residents);
//                }
//            }
//        };
//        task.start(true);
//
//        //update chunk messages
//        updateChunkMessages(city);
//    }
//
//    public static void saveWorldGuardDelayed()
//    {
//        Task task = new Task(RCCitiesPlugin.get())
//        {
//            @Override
//            public void run()
//            {
//                WorldGuardManager.save();
//            }
//        };
//        task.startDelayed(7*20);
//    }
//
//    public static ProtectedRegion restoreCityPlot(Plot plot)
//    {
//        //check if plot already exist
//        ProtectedRegion region = WorldGuardManager.getRegion(plot.getRegionId());
//        if(region != null)
//            return region;
//
//        //claim plot chunk
//        Location chunkLocation = new Location(plot.getCity().getSpawn().getWorld(), plot.getX()*16, 0, plot.getZ()*16);
//        ProtectedRegion newRegion = getProtectedCuboidRegion(plot.getRegionId(), chunkLocation);
//        WorldGuardManager.getWorldGuard().getGlobalRegionManager().get(plot.getCity().getSpawn().getWorld()).addRegion(newRegion);
//        WorldGuardManager.setTownFlags(plot.getRegionId());
//
//        DefaultDomain members = new DefaultDomain();
//        //set public
//        if(plot.isOpen())
//        {
//
//            List<Resident> residentList = TableHandler.get().getResidentTable().getResidents(plot.getCity());
//            for(Resident resident : residentList)
//            {
//                members.addPlayer(resident.getName());
//            }
//        }
//        //set member
//        else
//        {
//            List<Assignment> assignmentList = TableHandler.get().getAssignmentsTable().getAssignments(plot);
//            if(assignmentList != null)
//                for(Assignment assignment : assignmentList)
//                {
//                    members.addPlayer(TableHandler.get().getResidentTable().getResident(assignment.getResident_id()).getName());
//                }
//        }
//        WorldGuardManager.getRegion(plot.getRegionId()).setMembers(members);
//
//        //set pvp
//        if(plot.isPvp())
//            WorldGuardManager.getRegion(plot.getRegionId()).setFlag(DefaultFlag.PVP, StateFlag.State.ALLOW);
//
//        //set owner
//        updatePlotOwner(plot.getCity());
//
//        return newRegion;
//    }
//
//    public static Plot getLocalCityPlot(Location location, Resident resident)
//    {
//        ApplicableRegionSet regionSet = WorldGuardManager.getLocalRegions(location);
//        for(ProtectedRegion region : regionSet)
//        {
//            Plot plot = TableHandler.get().getPlotTable().getPlot(region.getId());
//            if(plot == null)
//                continue;
//            if(plot.getCity().getId() == resident.getCity().getId())
//            {
//                return plot;
//            }
//        }
//        return null;
//    }
//
//    public static void markChunk(Chunk chunk) {
//        //set torches
//        ChunkSnapshot chunkSnapshot = chunk.getChunkSnapshot();
//        Block block;
//        int i;
//
//        //EAST
//        for(i = 0; i<16; i++)
//        {
//            block = chunk.getBlock(i, chunkSnapshot.getHighestBlockYAt(i, 0), 0);
//            Toolbox.setTorch(block);
//        }
//
//        //WEST
//        for(i = 0; i<16; i++)
//        {
//            block = chunk.getBlock(i, chunkSnapshot.getHighestBlockYAt(i, 15), 15);
//            Toolbox.setTorch(block);
//        }
//
//        //NORTH
//        for(i = 0; i<16; i++)
//        {
//            block = chunk.getBlock(0, chunkSnapshot.getHighestBlockYAt(0, i), i);
//            Toolbox.setTorch(block);
//        }
//
//        //SOUTH
//        for(i = 0; i<16; i++)
//        {
//            block = chunk.getBlock(15, chunkSnapshot.getHighestBlockYAt(15, i), i);
//            Toolbox.setTorch(block);
//        }
//    }
//
//    public static boolean unmarkChunk(Chunk chunk) {
//        boolean success = false;
//        List<Block> blocks = new ArrayList<Block>(markBackup);
//        for(Block block : blocks) {
//            if(block.getChunk() == chunk && block.getType() == Material.TORCH) {
//                block.setType(Material.AIR);
//                markBackup.remove(block);
//                success = true;
//            }
//        }
//        return success;
//    }
//}