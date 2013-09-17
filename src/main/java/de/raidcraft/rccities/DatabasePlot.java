package de.raidcraft.rccities;

import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import de.raidcraft.RaidCraft;
import de.raidcraft.rccities.api.city.City;
import de.raidcraft.rccities.api.plot.AbstractPlot;
import de.raidcraft.rccities.tables.TPlot;
import org.bukkit.Location;

/**
 * @author Philip Urban
 */
public class DatabasePlot extends AbstractPlot {

    public DatabasePlot(Location location, City city) {

        super(location, city);
    }

    public DatabasePlot(TPlot tPlot) {

        //XXX setter call order is important!!!
        this.id = tPlot.getId();

        City city = RaidCraft.getComponent(RCCitiesPlugin.class).getCityManager().getCity(tPlot.getCity().getName());
        assert city != null : "City of plot is null!";
        this.city = city;

        Location location = new Location(city.getSpawn().getWorld(), tPlot.getX(), 0, tPlot.getZ());
        this.location = location;

        ProtectedRegion region = RaidCraft.getComponent(RCCitiesPlugin.class).getWorldGuard().getRegionManager(location.getWorld()).getRegion(getRegionName());
        this.region = region;
    }

    @Override
    public void save() {

        TPlot tPlot = new TPlot();
        tPlot.setCity(getCity());
        tPlot.setX(getLocation().getBlockX());
        tPlot.setZ(getLocation().getBlockZ());
        RaidCraft.getDatabase(RCCitiesPlugin.class).save(tPlot);
        this.id = tPlot.getId();
    }

    @Override
    public void delete() {

        super.delete();
        RCCitiesPlugin plugin = RaidCraft.getComponent(RCCitiesPlugin.class);

        //TODO delete assignments

        plugin.getPlotManager().removeFromCache(this);
        RaidCraft.getDatabase(RCCitiesPlugin.class).delete(TPlot.class, getId());
    }
}
