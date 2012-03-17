package de.strasse36.rccities.util;

import de.strasse36.rccities.City;

/**
 * Author: Philip Urban
 * Date: 17.03.12 - 16:51
 * Description:
 */
public class Taxes {
    private static Taxes _self;

    public static void init()
    {
        if(_self == null)
            _self = new Taxes();
        _self.startTask();
    }

    public static Taxes get()
    {
        return _self;
    }

    //start tax schedule
    private void startTask()
    {

    }

    //this method is called by the tax schedule
    private void levy()
    {

    }

    //award the penalty
    private void penalty(City city)
    {

    }
}
