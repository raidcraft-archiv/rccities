package de.raidcraft.rccities.manager;

import de.raidcraft.RaidCraft;
import de.raidcraft.rccities.DatabasePlot;
import de.raidcraft.rccities.RCCitiesPlugin;
import de.raidcraft.rccities.api.city.City;
import de.raidcraft.rccities.api.plot.Plot;
import de.raidcraft.rccities.tables.TPlot;
import org.bukkit.Location;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Philip Urban
 */
public class PlotManager {

    private RCCitiesPlugin plugin;
    private Map<Location, Plot> cachedPlots = new HashMap<>();

    public PlotManager(RCCitiesPlugin plugin) {

        this.plugin = plugin;
    }

    public List<Plot> getPlots(City city) {

        List<Plot> plots = new ArrayList<>();
        List<TPlot> tPlots = RaidCraft.getDatabase(RCCitiesPlugin.class).find(TPlot.class).where().eq("city_id", city.getId()).findList();
        for(TPlot tPlot : tPlots) {
            Location plotLocation = new Location(city.getSpawn().getWorld(), tPlot.getX(), 0, tPlot.getZ());
            if(!cachedPlots.containsKey(plotLocation)) {
                Plot plot = new DatabasePlot(tPlot);
                cachedPlots.put(plotLocation, plot);
                plots.add(plot);
            }
            else {
                plots.add(cachedPlots.get(plotLocation));
            }
        }
        return plots;
    }

    public Plot getPlot(int id) {

        for(Plot plot : cachedPlots.values()) {
            if(plot.getId() == id) {
                return plot;
            }
        }
        TPlot tPlot = RaidCraft.getDatabase(RCCitiesPlugin.class).find(TPlot.class, id);
        if(tPlot != null) {
            Plot plot = new DatabasePlot(tPlot);
            cachedPlots.put(plot.getLocation(), plot);
            return plot;
        }
        return null;
    }

    public void clearCache() {

        cachedPlots.clear();
    }
}
