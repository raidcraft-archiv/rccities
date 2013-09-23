package de.raidcraft.rccities;

import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import de.raidcraft.RaidCraft;
import de.raidcraft.api.RaidCraftException;
import de.raidcraft.rccities.api.city.City;
import de.raidcraft.rccities.api.plot.AbstractPlot;
import de.raidcraft.rccities.api.resident.Resident;
import de.raidcraft.rccities.tables.TAssignment;
import de.raidcraft.rccities.tables.TPlot;
import de.raidcraft.util.CaseInsensitiveMap;
import org.bukkit.Location;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author Philip Urban
 */
public class DatabasePlot extends AbstractPlot {

    protected Map<String, Resident> assignedResidents = new CaseInsensitiveMap<>();

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
        loadAssignments();
    }

    @Override
    public void setFlag(String flagName, String flagValue) throws RaidCraftException {

        RaidCraft.getComponent(RCCitiesPlugin.class).getFlagManager().setPlotFlag(this, flagName, flagValue);
    }

    @Override
    public void removeFlag(String flagName) {

        RaidCraft.getComponent(RCCitiesPlugin.class).getFlagManager().removePlotFlag(this, flagName);
    }

    @Override
    public void refreshFlags() {

        RaidCraft.getComponent(RCCitiesPlugin.class).getFlagManager().refreshPlotFlags(this);
    }

    @Override
    public List<Resident> getAssignedResidents() {

        return new ArrayList<>(assignedResidents.values());
    }

    @Override
    public void assignResident(Resident resident) {

        if(assignedResidents.containsKey(resident.getName())) return;
        assignedResidents.put(resident.getName(), resident);
        TAssignment tAssignment = new TAssignment();
        tAssignment.setPlot(this);
        tAssignment.setResident(resident);
        RaidCraft.getDatabase(RCCitiesPlugin.class).save(tAssignment);
    }

    @Override
    public void removeResident(Resident resident) {

        Resident removedResident = assignedResidents.remove(resident.getName());
        if(removedResident != null) {
            // delete assignment
            TAssignment assignment = RaidCraft.getDatabase(RCCitiesPlugin.class)
                    .find(TAssignment.class).where().eq("resident_id", removedResident.getId()).eq("plot_id", getId()).findUnique();
            RaidCraft.getDatabase(RCCitiesPlugin.class).delete(assignment);
            // update region
            updateRegion(false);
        }
    }

    private void loadAssignments() {

        List<TAssignment> assignments = RaidCraft.getDatabase(RCCitiesPlugin.class).find(TAssignment.class).where().eq("plot_id", getId()).findList();
        for(TAssignment assignment : assignments) {

            Resident resident = RaidCraft.getComponent(RCCitiesPlugin.class).getResidentManager().getResident(assignment.getResident().getName(), getCity());
            if(resident == null) continue;
            assignedResidents.put(resident.getName(), resident);
        }
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

        // delete assignment
        List<TAssignment> assignments = RaidCraft.getDatabase(RCCitiesPlugin.class).find(TAssignment.class).where().eq("plot_id", getId()).findList();
        RaidCraft.getDatabase(RCCitiesPlugin.class).delete(assignments);

        // delete from cache
        plugin.getPlotManager().removeFromCache(this);

        // delete plot
        RaidCraft.getDatabase(RCCitiesPlugin.class).delete(TPlot.class, getId());
    }
}
