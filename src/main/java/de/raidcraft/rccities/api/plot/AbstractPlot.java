package de.raidcraft.rccities.api.plot;

import com.sk89q.worldedit.regions.CuboidRegion;
import com.sk89q.worldedit.regions.Region;
import de.raidcraft.rccities.api.city.City;
import org.bukkit.Location;

/**
 * @author Philip Urban
 */
public abstract class AbstractPlot implements Plot {

    private Location location;
    private Region region;
    private City city;

    protected AbstractPlot(Location location, Region region, City city) {

        Location simpleLocation = new Location(location.getWorld(), location.getX(), location.getY(), location.getZ());
        this.location = simpleLocation;
        this.region = region;
        this.city = city;

        updateRegion();
    }

    @Override
    public Location getLocation() {

        return location;
    }

    @Override
    public Region getRegion() {

        return region;
    }

    @Override
    public City getCity() {

        return city;
    }

    @Override
    public void updateRegion() {

        CuboidRegion cuboidRegion = new CuboidRegion()
    }
}
