package de.raidcraft.rccities.api.city;

import org.bukkit.Location;

import java.sql.Timestamp;

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

    public int getSize();

    public void save();

    public void delete();

    public boolean equals(Object o);

    public int hashCode();
}
