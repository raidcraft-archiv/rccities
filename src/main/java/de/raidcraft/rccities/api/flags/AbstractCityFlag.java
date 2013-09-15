package de.raidcraft.rccities.api.flags;

import de.raidcraft.rccities.api.city.City;

/**
 * @author Philip Urban
 */
public abstract class AbstractCityFlag extends AbstractFlag implements CityFlag {

    private City city;

    public AbstractCityFlag(City city) {

        this.city = city;
    }

    @Override
    public City getCity() {

        return city;
    }
}
