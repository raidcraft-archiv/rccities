package de.raidcraft.rccities.api.city;

import de.raidcraft.api.RaidCraftException;
import de.raidcraft.rccities.api.request.JoinRequest;
import de.raidcraft.rccities.api.resident.Resident;
import de.raidcraft.rcupgrades.api.holder.UpgradeHolder;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.sql.Timestamp;
import java.util.List;

/**
 * @author Philip Urban
 */
public interface City {

    public int getId();

    public String getName();

    public String getFriendlyName();

    public String getBankAccountName();

    public String getCreator();

    public Timestamp getCreationDate();

    public Location getSpawn();

    public void setSpawn(Location spawn);

    public String getDescription();

    public void setDescription(String description);

    public int getPlotCredit();

    public void setPlotCredit(int plotCredit);

    public UpgradeHolder getUpgrades();

    public int getMaxRadius();

    public void setMaxRadius(int maxRadius);

    public int getExp();

    public void addExp(int exp);

    public void removeExp(int exp);

    public int getSize();

    public void setFlag(Player player, String flagName, String flagValue) throws RaidCraftException;

    public void removeFlag(String flagName);

    public void refreshFlags();

    public List<Resident> getResidents();

    public List<JoinRequest> getJoinRequests();

    public JoinRequest getJoinRequest(String playerName);

    public void sendJoinRequest(String playerName);

    public void save();

    public void delete();

    public boolean equals(Object o);

    public int hashCode();

}
