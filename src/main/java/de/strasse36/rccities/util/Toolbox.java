package de.strasse36.rccities.util;

import org.bukkit.Material;
import org.bukkit.block.Block;

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
}
