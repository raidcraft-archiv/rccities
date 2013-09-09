package de.raidcraft.rccities.api.city;

import de.raidcraft.rccities.api.plot.Plot;
import de.raidcraft.rccities.api.resident.Resident;
import de.raidcraft.rccities.api.settings.Setting;
import org.bukkit.Location;

import java.util.Set;

/**
 * @author Philip Urban
 */
public interface City {

    public String getName();

    public Location getSpawn();

    public String getDescription();

    public Set<Setting> getSettings();

    public Set<Resident> getResidents();

    public Set<Plot> getPlots();
}
