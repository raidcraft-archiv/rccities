package de.strasse36.rccities.util;

import com.sk89q.worldedit.BlockVector;
import com.sk89q.worldguard.protection.regions.ProtectedCuboidRegion;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import org.bukkit.Chunk;
import org.bukkit.Location;

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
                (chunk.getX()*16)+16,
                location.getWorld().getMaxHeight(),
                (chunk.getZ()*16)+16
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
}
