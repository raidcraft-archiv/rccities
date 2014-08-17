package de.raidcraft.rccities.api.request;

import de.raidcraft.rccities.api.city.City;

import java.util.UUID;

/**
 * @author Philip Urban
 */
public interface JoinRequest extends Request {

    public UUID getPlayer();

    public City getCity();

    public void save();
}
