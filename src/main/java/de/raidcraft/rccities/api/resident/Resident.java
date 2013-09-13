package de.raidcraft.rccities.api.resident;

import de.raidcraft.rccities.api.city.City;
import org.bukkit.entity.Player;

/**
 * @author Philip Urban
 */
public interface Resident {

    public int getId();

    public String getName();

    public Role getRole();

    public void setRole(Role role);

    public City getCity();

    public Player getPlayer();

    public void save();

    public void delete();
}
