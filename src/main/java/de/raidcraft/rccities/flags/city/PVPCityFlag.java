package de.raidcraft.rccities.flags.city;

import de.raidcraft.rccities.api.city.City;
import de.raidcraft.rccities.api.flags.AbstractCityFlag;
import de.raidcraft.rccities.api.flags.FlagInformation;
import de.raidcraft.rccities.api.flags.FlagType;

/**
 * @author Philip Urban
 */
@FlagInformation(
        name = "PVP",
        type = FlagType.BOOLEAN
)
public class PVPCityFlag extends AbstractCityFlag {

    public PVPCityFlag(City city) {

        super(city);
    }

    @Override
    public void refresh() {

        if(getCity() == null) return;

        boolean currentValue = getType().convertToBoolean(getValue());

        //TODO: implement
    }
}
