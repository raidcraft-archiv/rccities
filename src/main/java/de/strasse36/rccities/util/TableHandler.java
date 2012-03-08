package de.strasse36.rccities.util;

import com.silthus.raidcraft.database.UnknownTableException;
import com.silthus.raidcraft.util.RCLogger;
import de.strasse36.rccities.database.CityTable;
import de.strasse36.rccities.database.PlotTable;
import de.strasse36.rccities.database.RCCitiesDatabase;
import de.strasse36.rccities.database.ResidentTable;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;

/**
 * Author: Philip Urban
 * Date: 01.03.12 - 04:14
 * Description:
 */
public class TableHandler {
    private static TableHandler _self;
    private CityTable cityTable;
    private ResidentTable residentTable;
    private PlotTable plotTable;

    public static void init()
    {
        if(_self == null)
            _self = new TableHandler();
        _self.loadTables();
    }

    private void loadTables()
    {
        try {
            this.cityTable = ((CityTable) RCCitiesDatabase.get().getTable(RCCitiesDatabase.get().getPrefix()+TableNames.getCityTable()));
        } catch (UnknownTableException e) {
            RCLogger.warning("Citytable not found! -  Disabling RCCities...");
            Plugin rccities = Bukkit.getServer().getPluginManager().getPlugin("RCCities");
            Bukkit.getServer().getPluginManager().disablePlugin(rccities);
        }

        try {
            this.residentTable = ((ResidentTable) RCCitiesDatabase.get().getTable(RCCitiesDatabase.get().getPrefix()+TableNames.getResidentTable()));
        } catch (UnknownTableException e1) {
            RCLogger.warning("Residenttable not found! -  Disabling RCCities...");
            Plugin rccities = Bukkit.getServer().getPluginManager().getPlugin("RCCities");
            Bukkit.getServer().getPluginManager().disablePlugin(rccities);
        }

        try {
            this.plotTable = ((PlotTable) RCCitiesDatabase.get().getTable(RCCitiesDatabase.get().getPrefix()+TableNames.getPlotTable()));
        } catch (UnknownTableException e1) {
            RCLogger.warning("Plottable not found! -  Disabling RCCities...");
            Plugin rccities = Bukkit.getServer().getPluginManager().getPlugin("RCCities");
            Bukkit.getServer().getPluginManager().disablePlugin(rccities);
        }
    }

    public static TableHandler get()
    {
        return _self;
    }

    public CityTable getCityTable()
    {
       return this.cityTable;
    }

    public ResidentTable getResidentTable()
    {
       return this.residentTable;
    }

    public PlotTable getPlotTable() {
        return plotTable;
    }
}
