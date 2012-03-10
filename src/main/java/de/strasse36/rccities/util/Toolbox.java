package de.strasse36.rccities.util;

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
}
