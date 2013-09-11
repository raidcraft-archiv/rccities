package de.raidcraft.rccities.api.plot;

import com.sk89q.worldedit.regions.Region;
import de.raidcraft.rccities.api.city.City;
import org.bukkit.Location;

/**
 * @author Philip Urban
 */
public interface Plot {

    public Location getLocation();

    public Region getRegion();

    public City getCity();

    public void save();

    public void updateRegion();
}
