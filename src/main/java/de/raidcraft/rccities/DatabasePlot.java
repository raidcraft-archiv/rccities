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
        setId(tPlot.getId());

        City city = RaidCraft.getComponent(RCCitiesPlugin.class).getCityManager().getCity(tPlot.getCity().getName());
        assert city != null : "City of plot is null!";
        setCity(city);

        Location location = new Location(city.getSpawn().getWorld(), tPlot.getX(), 0, tPlot.getZ());
        setLocation(location);

        ProtectedRegion region = RaidCraft.getComponent(RCCitiesPlugin.class).getWorldGuard().getRegionManager(location.getWorld()).getRegion(getRegionName());
        setRegion(region);
    }

    @Override
    public void save() {

        TPlot tPlot = new TPlot();
        tPlot.setCity(getCity());
        tPlot.setX(getLocation().getBlockX());
        tPlot.setZ(getLocation().getBlockZ());
        RaidCraft.getDatabase(RCCitiesPlugin.class).save(tPlot);
        setId(tPlot.getId());
    }

    @Override
    public void delete() {

        super.delete();
        RaidCraft.getDatabase(RCCitiesPlugin.class).delete(TPlot.class, getId());
    }
}
