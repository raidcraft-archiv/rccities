package de.strasse36.rccities.util;

import com.sk89q.worldguard.protection.regions.ProtectedCuboidRegion;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import org.bukkit.Chunk;
import org.bukkit.Location;
import com.sk89q.worldedit.BlockVector;

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
            chunk.getBlock(0, 0, 0).getX(),
            chunk.getBlock(0, 0, 0).getY(),
            chunk.getBlock(0, 0, 0).getZ()
        );
        BlockVector blockVector2 = new BlockVector(
                chunk.getBlock(16, location.getWorld().getMaxHeight(), 16).getX(),
                chunk.getBlock(16, location.getWorld().getMaxHeight(), 16).getY(),
                chunk.getBlock(16, location.getWorld().getMaxHeight(), 16).getZ()
        );
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
}
