package de.strasse36.rccities.util;

import org.bukkit.Material;
import org.bukkit.block.Block;

import java.math.BigDecimal;

/**
 * Author: Philip Urban
 * Date: 10.03.12 - 23:22
 * Description:
 */
public class Toolbox {
    public static double isDouble(String input)
    {
        double result;
        try
        {
            result = Double.parseDouble(input);
        }
        catch( Exception e)
        {
            return -1;
        }

        if(result>0)
            return result;
        else
            return -1;
    }

    public static int isInteger(String input)
    {
        int result;
        try
        {
            result = Integer.parseInt(input);
        }
        catch( Exception e)
        {
            return -1;
        }

        if(result>0)
            return result;
        else
            return -1;
    }
    
    public static boolean canBuildTorch(Block block)
    {
        if(     block.isLiquid() ||
                block.getType() == Material.LEAVES ||
                block.getType() == Material.ICE ||
                block.getType() == Material.WATER_LILY ||
                block.getType() == Material.GLOWSTONE ||
                block.getType() == Material.SUGAR_CANE ||
                block.getType() == Material.DEAD_BUSH ||
                block.getType() == Material.CACTUS
        )
            return false;
        else
        {
            return true;
        }
    }

    public static void setTorch(Block block) {
        Block blockBelow = block.getRelative(0, -1, 0);
        Block blockAbove = block.getRelative(0, 1, 0);
        if(Toolbox.canBuildTorch(blockBelow) && blockAbove.getType() == Material.AIR && block.getType() == Material.AIR) {
            if(!ChunkUtil.markBackup.contains(block))
                ChunkUtil.markBackup.add(block);
            block.setType(Material.TORCH);
        }
    }

    public static synchronized long getTimestamp()
    {
        return System.currentTimeMillis() / 1000;
    }

    public static int[] splitToComponentTimes(int seconds)
    {
        int hours = seconds / 3600;
        int remainder = seconds - hours * 3600;
        int mins = remainder / 60;
        remainder = remainder - mins * 60;
        int secs = remainder;

        return new int[]{hours, mins, secs};
    }

}
