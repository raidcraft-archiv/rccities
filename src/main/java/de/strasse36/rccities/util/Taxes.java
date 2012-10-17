package de.strasse36.rccities.util;

import com.silthus.raidcraft.util.Task;
import com.silthus.rccoins.Bank;
import com.silthus.rccoins.MoneyTransfer;
import com.silthus.rccoins.database.Database;
import de.strasse36.rccities.City;
import de.strasse36.rccities.bukkit.RCCitiesPlugin;
import de.strasse36.rccities.config.MainConfig;
import de.strasse36.rccities.database.TableHandler;

import java.util.List;

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
        Task task = new Task(RCCitiesPlugin.get())
        {
            @Override
            public void run()
            {
                List<City> allCities = TableHandler.get().getCityTable().getCitys();
                if(allCities == null)
                    return;
                for(City city : allCities)
                {
                    if((System.currentTimeMillis()/1000)-city.getLastTaxPayment() > MainConfig.getLevyInterval()*60)
                    {
                        Taxes.get().levy(city);
                        city.setLastTaxPayment(System.currentTimeMillis()/1000);
                        TableHandler.get().getCityTable().updateCity(city);
                    }
                }
            }
        };
        task.startRepeating(60*20*60);
    }

    //this method is called by the tax schedule
    private void levy(City city)
    {
        int cityPopulation = TableHandler.get().getResidentTable().getResidents(city).size();
        if(RCCitiesPlugin.get().getEconomy().has(city.getBankAccount(), cityPopulation*MainConfig.getTaxAmount()))
        {
            RCCitiesPlugin.get().getEconomy().remove(city.getBankAccount(), cityPopulation*MainConfig.getTaxAmount());
            TownMessaging.sendTownResidents(city, "Der Stadtkasse wurden Steuern in Höhe von " + cityPopulation*MainConfig.getTaxAmount() + "c abgezogen!");

            //log in money flow
            MoneyTransfer moneyTransfer = new MoneyTransfer(city.getName()
                    , "RaidCraftBank"
                    , -(cityPopulation*MainConfig.getTaxAmount())
                    , Bank.getTimestamp()
                    , true
                    , "DailyTax");
            Database.addMoneyTransfer(moneyTransfer);
        }
        else
        {
            penalty(city);
            TownMessaging.sendTownResidents(city, "Die Stadt konnte die Steuern in Höhe von " + cityPopulation*MainConfig.getTaxAmount() + "c nicht bezahlen!");
            TownMessaging.sendTownResidents(city, "Aus Strafe wurde die max. Stadtgrösse verringert!");
        }
    }

    //award the penalty
    private void penalty(City city)
    {
        city.setSize(city.getSize()- MainConfig.getPlotsPerPenalty());
        city.setLastPenalty(System.currentTimeMillis()/1000);
        TableHandler.get().getCityTable().updateCity(city);
    }
}
