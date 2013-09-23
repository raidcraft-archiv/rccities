package de.raidcraft.rccities.api.plot;

import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import de.raidcraft.rccities.api.city.City;
import org.bukkit.Location;

/**
 * @author Philip Urban
 */
public interface Plot {

    public int getId();

    public String getRegionName();

    public Location getLocation();

    public ProtectedRegion getRegion();

    public City getCity();

    public void save();

    public void updateRegion(boolean create);

    public void delete();
}
