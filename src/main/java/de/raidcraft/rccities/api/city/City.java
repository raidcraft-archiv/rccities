package de.raidcraft.rccities.api.city;

import de.raidcraft.api.RaidCraftException;
import de.raidcraft.rccities.api.resident.Resident;
import org.bukkit.Location;

import java.sql.Timestamp;
import java.util.List;

/**
 * @author Philip Urban
 */
public interface City {

    public int getId();

    public String getName();

    public String getFriendlyName();

    public String getCreator();

    public Timestamp getCreationDate();

    public Location getSpawn();

    public void setSpawn(Location spawn);

    public String getDescription();

    public void setDescription(String description);

    public int getPlotCredit();

    public void setPlotCredit(int plotCredit);

    public int getMaxRadius();

    public void setMaxRadius(int maxRadius);

    public int getExp();

    public void addExp(int exp);

    public void removeExp(int exp);

    public int getSize();

    public void setFlag(String flagName, String flagValue) throws RaidCraftException;

    public void removeFlag(String flagName);

    public void refreshFlags();

    public List<Resident> getResidents();

    public void save();

    public void delete();

    public boolean equals(Object o);

    public int hashCode();
}
