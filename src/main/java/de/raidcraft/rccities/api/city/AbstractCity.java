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

    private int id;
    private String name;
    private Location spawn;
    private String description;
    private Map<String, Setting> settings = new CaseInsensitiveMap<>();
    private Map<String, Resident> residents = new CaseInsensitiveMap<>();
    private Map<Location, Plot> plots = new HashMap<>();

    protected AbstractCity(String name, Location spawn) {

        this.name = name;
        this.spawn = spawn;

        save();
    }

    @Override
    public int getId() {

        return id;
    }

    protected void setId(int id) {

        this.id = id;
    }

    @Override
    public final String getName() {

        return name;
    }

    @Override
    public String getFriendlyName() {

        return name.replace('_', ' ');
    }

    @Override
    public final void setName(String name) {

        name = name.replace(' ', '_');
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
    public final Setting getSetting(String key) {

        return settings.get(key);
    }

    @Override
    public final Collection<Setting> getSettings() {

        return settings.values();
    }

    @Override
    public final void setSetting(String key, String value) {

        settings.put(key, new Setting(key, value));
        save();
    }

    @Override
    public final void removeSetting(Setting setting) {

        removeSetting(setting.getKey());
    }

    @Override
    public final void removeSetting(String key) {

        settings.remove(key);
    }

    @Override
    public final Collection<Resident> getResidents() {

        return residents.values();
    }

    @Override
    public final Resident getResident(String residentName) {

        return residents.get(residentName);
    }

    @Override
    public final void addResident(Resident resident) {

        residents.put(resident.getName(), resident);
    }

    @Override
    public final void removeResident(Resident resident) {

        removeResident(resident.getName());
    }

    @Override
    public final void removeResident(String name) {

        removeResident(name);
    }

    @Override
    public final Collection<Plot> getPlots() {

        return plots.values();
    }

    @Override
    public final Plot getPlot(Location location) {

        Location simpleLocation = new Location(location.getWorld(), location.getX(), location.getY(), location.getZ());
        return plots.get(simpleLocation);
    }

    @Override
    public final void addPlot(Plot plot) {

        plots.put(plot.getLocation(), plot);
        save();
    }

    @Override
    public final void removePlot(Plot plot) {

        removePlot(plot.getLocation());
    }

    @Override
    public final void removePlot(Location location) {

        Location simpleLocation = new Location(location.getWorld(), location.getX(), location.getY(), location.getZ());
        plots.remove(simpleLocation);
    }
}
