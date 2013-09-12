package de.raidcraft.rccities;

import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import de.raidcraft.RaidCraft;
import de.raidcraft.rccities.RCCitiesPlugin;
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

    public DatabasePlot(int plotId, City city) {

        //XXX setter call order is important!!!
        setId(plotId);
        setCity(city);

        TPlot tPlot = RaidCraft.getDatabase(RCCitiesPlugin.class).find(TPlot.class, plotId);
        assert tPlot != null : "Kein Plot mit der ID " + plotId + " gefunden!";

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
