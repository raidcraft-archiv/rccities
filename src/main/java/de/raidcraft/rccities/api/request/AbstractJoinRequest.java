package de.raidcraft.rccities.api.request;

import de.raidcraft.rccities.api.city.City;

/**
 * @author Philip Urban
 */
public abstract class AbstractJoinRequest implements JoinRequest {

    private String player;
    private City city;
    private boolean rejected;
    private String rejectReason;

    protected AbstractJoinRequest(String player, City city, boolean rejected, String rejectReason) {

        this.player = player;
        this.city = city;
        this.rejected = rejected;
        this.rejectReason = rejectReason;
        save();
    }

    @Override
    public String getPlayer() {

        return player;
    }

    @Override
    public City getCity() {

        return city;
    }

    @Override
    public boolean isRejected() {

        return rejected;
    }

    @Override
    public void setRejected(boolean rejected) {

        this.rejected = rejected;
        save();
    }

    @Override
    public String getRejectReason() {

        return rejectReason;
    }

    @Override
    public void setRejectReason(String rejectReason) {

        this.rejectReason = rejectReason;
        save();
    }
}
