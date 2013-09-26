package de.raidcraft.rccities.api.plot;

import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import de.raidcraft.api.RaidCraftException;
import de.raidcraft.rccities.api.city.City;
import de.raidcraft.rccities.api.resident.Resident;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.List;

/**
 * @author Philip Urban
 */
public interface Plot {

    public int getId();

    public String getRegionName();

    public Location getLocation();

    public ProtectedRegion getRegion();

    public City getCity();

    public List<Resident> getAssignedResidents();

    public void assignResident(Resident resident);

    public void removeResident(Resident resident);

    public void setFlag(Player player, String flagName, String flagValue) throws RaidCraftException;

    public void removeFlag(String flagName);

    public void refreshFlags();

    public void save();

    public void updateRegion(boolean create);

    public void delete();
}
