package de.raidcraft.rccities.api.request;

import de.raidcraft.rccities.api.city.City;

import java.util.UUID;

/**
 * @author Philip Urban
 */
public abstract class AbstractJoinRequest extends AbstractRequest implements JoinRequest {

    private UUID player;
    private City city;

    protected AbstractJoinRequest(UUID player, City city, boolean rejected, String rejectReason) {

        this.player = player;
        this.city = city;
        this.rejected = rejected;
        this.rejectReason = rejectReason;
        save();
    }

    @Override
    public UUID getPlayer() {

        return player;
    }

    @Override
    public City getCity() {

        return city;
    }
}
