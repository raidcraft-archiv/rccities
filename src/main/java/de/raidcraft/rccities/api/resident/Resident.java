package de.raidcraft.rccities.api.resident;

import de.raidcraft.rccities.api.city.City;
import org.bukkit.entity.Player;

import java.util.UUID;

/**
 * @author Philip Urban
 */
public interface Resident {

    int getId();

    UUID getPlayerId();

    String getName();

    Role getRole();

    void setRole(Role role);

    City getCity();

    Player getPlayer();

    void save();

    void delete();
}
