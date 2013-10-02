package de.raidcraft.rccities.api.request;

import de.raidcraft.rccities.api.city.City;
import de.raidcraft.rcupgrades.api.level.UpgradeLevel;

/**
 * @author Philip Urban
 */
public interface UpgradeRequest extends Request {

    public City getCity();

    public UpgradeLevel<City> getUpgradeLevel();

    public long getRejectExpirationDate();
}
