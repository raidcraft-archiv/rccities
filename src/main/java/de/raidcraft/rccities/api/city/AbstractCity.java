package de.raidcraft.rccities.api.city;

import de.raidcraft.rccities.api.plot.Plot;
import de.raidcraft.rccities.api.resident.Resident;
import de.raidcraft.rccities.api.settings.Setting;
import de.raidcraft.util.CaseInsensitiveMap;
import org.bukkit.Location;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Philip Urban
 */
public abstract class AbstractCity implements City {

    private String name;
    private Location spawn;
    private String description;
    private Map<String, Setting> settings = new CaseInsensitiveMap<>();
    private Map<String, Resident> residents = new CaseInsensitiveMap<>();
    private Map<Location, Plot> plots = new HashMap<>();

    @Override
    public final String getName() {

        return name;
    }

    @Override
    public final void setName(String name) {

        this.name = name;
        save();
    }

    @Override
    public final Location getSpawn() {

        return spawn;
    }

    @Override
    public final void setSpawn(Location spawn) {

        this.spawn = spawn;
        save();
    }

    @Override
    public final String getDescription() {

        return description;
    }

    @Override
    public final void setDescription(String description) {

        this.description = description;
        save();
    }

    @Override
    public Setting getSetting(String key) {

        return settings.get(key);
    }

    @Override
    public Collection<Setting> getSettings() {

        return settings.values();
    }

    @Override
    public void setSetting(String key, String value) {

        settings.put(key, new Setting(key, value));
        save();
    }

    @Override
    public Collection<Resident> getResidents() {

        return residents.values();
    }

    @Override
    public Resident getResident(String residentName) {

        return residents.get(residentName);
    }

    @Override
    public void addResident(Resident resident) {

        residents.put(resident.getName(), resident);
    }

    @Override
    public Collection<Plot> getPlots() {

        return plots.values();
    }

    @Override
    public Plot getPlot(Location location) {

        Location simpleLocation = new Location(location.getWorld(), location.getX(), location.getY(), location.getZ());
        return plots.get(simpleLocation);
    }

    @Override
    public void addPlot(Plot plot) {

        plots.put(plot.getLocation(), plot);
        save();
    }
}
