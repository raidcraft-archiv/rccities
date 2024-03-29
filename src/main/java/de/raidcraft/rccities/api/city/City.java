package de.raidcraft.rccities.api.city;

import de.raidcraft.api.RaidCraftException;
import de.raidcraft.rccities.api.request.JoinRequest;
import de.raidcraft.rccities.api.resident.Resident;
import de.raidcraft.rcupgrades.api.holder.UpgradeHolder;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.sql.Timestamp;
import java.util.List;
import java.util.UUID;

/**
 * @author Philip Urban
 */
public interface City {

    int getId();

    String getName();

    String getFriendlyName();

    String getBankAccountName();

    UUID getCreator();

    Timestamp getCreationDate();

    Location getSpawn();

    void setSpawn(Location spawn);

    String getDescription();

    void setDescription(String description);

    int getPlotCredit();

    void setPlotCredit(int plotCredit);

    UpgradeHolder<City> getUpgrades();

    int getMaxRadius();

    void setMaxRadius(int maxRadius);

    int getExp();

    void addExp(int exp);

    void removeExp(int exp);

    int getSize();

    void setFlag(Player player, String flagName, String flagValue) throws RaidCraftException;

    void removeFlag(String flagName);

    void refreshFlags();

    List<Resident> getResidents();

    List<JoinRequest> getJoinRequests();

    JoinRequest getJoinRequest(UUID playerId);

    void sendJoinRequest(UUID playerId);

    void save();

    void delete();

    boolean equals(Object o);

    int hashCode();

}
