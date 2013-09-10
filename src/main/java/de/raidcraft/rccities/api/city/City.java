package de.raidcraft.rccities.api.city;

import de.raidcraft.rccities.api.plot.Plot;
import de.raidcraft.rccities.api.resident.Resident;
import de.raidcraft.rccities.api.settings.Setting;
import org.bukkit.Location;

import java.util.Collection;

/**
 * @author Philip Urban
 */
public interface City {

    public String getName();

    public void setName(String name);

    public Location getSpawn();

    public void setSpawn(Location spawn);

    public String getDescription();

    public void setDescription(String description);

    public Setting getSetting(String key);

    public Collection<Setting> getSettings();

    public void setSetting(String key, String value);

    public Collection<Resident> getResidents();

    public Resident getResident(String residentName);

    public void addResident(Resident resident);

    public Collection<Plot> getPlots();

    public Plot getPlot(Location location);

    public void addPlot(Plot plot);

    public void save();
}
