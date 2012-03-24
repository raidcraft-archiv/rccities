package de.strasse36.rccities.commands;

import com.silthus.raidcraft.util.RCMessaging;
import de.strasse36.rccities.Assignment;
import de.strasse36.rccities.City;
import de.strasse36.rccities.Resident;
import de.strasse36.rccities.config.MainConfig;
import de.strasse36.rccities.util.ChunkUtil;
import de.strasse36.rccities.util.Profession;
import de.strasse36.rccities.util.TableHandler;
import de.strasse36.rccities.util.TownMessaging;
import org.bukkit.command.CommandSender;

import java.util.List;

/**
 * Author: Philip Urban
 * Date: 01.03.12 - 14:53
 * Description:
 */
public class NonResidentCommands {

    public static void acceptTownInvite(CommandSender sender)
    {
        if(!CityStaffCommands.invites.containsKey(sender.getName()))
        {
            RCMessaging.warn(sender, "Du hast keine offenen Einladungen!");
            return;
        }
        City city = CityStaffCommands.invites.get(sender.getName());

        //create resident and update database
        Resident resident = new Resident(sender.getName(), city, "resident");
        TownMessaging.sendTownResidents(city, sender.getName() + " ist nun Einwohner von " + city.getName() + "!");
        TableHandler.get().getResidentTable().updateResident(resident);
        RCMessaging.send(sender, RCMessaging.blue("Du bist nun Einwohner von " + city.getName() + "!"), false);

        //update city-size
        city.setSize(city.getSize() + MainConfig.getChunksPerPlayer());
        TableHandler.get().getCityTable().updateCity(city);

        //update public plots
        ChunkUtil.setPublic(city);

        //remove from map
        CityStaffCommands.invites.remove(sender.getName());
    }
    
    public static void listTowns(CommandSender sender)
    {
        List<City> cityList = TableHandler.get().getCityTable().getCitys();
        String cities = "";
        int citycount = 0;
        for(City city : cityList)
        {
            if(cities.length() > 0)
                cities += ", ";
            cities += city.getName();
            citycount++;
        }
        RCMessaging.send(sender, RCMessaging.green("--- RCCities Städteinfo ---"), false);
        RCMessaging.send(sender, RCMessaging.green("Auf dem Server gibt es derzeit folgende Städte (" + citycount + "):"), false);
        RCMessaging.send(sender, cities, false);
    }

    public static void resident(CommandSender sender, String[] args)
    {
        if(args.length < 2)
        {
            RCMessaging.warn(sender, "Es wurden nicht genügend Parameter eingegeben!");
            RCMessaging.warn(sender, "/town resident <Spielername> zeigt Einwohner-Infos über einen Spieler");
            return;
        }
        Resident selectedResident = TableHandler.get().getResidentTable().getResident(args[1]);
        
        if(selectedResident == null)
        {
            TownCommandUtility.selectNoResident(sender);
            return;
        }

        String plots = "";
        List<Assignment> assignmentList = TableHandler.get().getAssignmentsTable().getAssignments(selectedResident);
        if(assignmentList == null)
            plots = "~keine~";
        else
        {
            for(Assignment assignment : assignmentList)
            {
                if(plots.equals(""))
                    plots += ", ";
                plots += TableHandler.get().getPlotTable().getPlot(assignment.getPlot_id()).getRegionId();
            }
        }

        RCMessaging.send(sender, RCMessaging.green("--- RCCities Einwohner-Info für " + selectedResident.getName() + " ---"), false);
        RCMessaging.send(sender, RCMessaging.green("Stadt: " + selectedResident.getCity().getName()), false);
        RCMessaging.send(sender, RCMessaging.green("Beruf: " + Profession.translateProfession(selectedResident.getProfession())), false);
        RCMessaging.send(sender, RCMessaging.green("Grundstücke: " + plots), false);

    }
}
