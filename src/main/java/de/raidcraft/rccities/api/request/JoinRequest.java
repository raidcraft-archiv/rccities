package de.raidcraft.rccities.api.request;

import de.raidcraft.rccities.api.city.City;

/**
 * @author Philip Urban
 */
public interface JoinRequest extends Request {

    public String getPlayer();

    public City getCity();

    public void save();
}
