package de.raidcraft.rccities.api.request;

import de.raidcraft.rccities.api.city.City;

/**
 * @author Philip Urban
 */
public interface JoinRequest {

    public String getPlayer();

    public City getCity();

    public boolean isRejected();

    public void setRejected(boolean rejected);

    public String getRejectReason();

    public void setRejectReason(String rejectReason);

    public void accept();

    public void reject(String reason);

    public void save();
}
