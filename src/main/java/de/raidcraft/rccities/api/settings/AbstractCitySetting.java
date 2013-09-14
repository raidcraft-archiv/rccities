package de.raidcraft.rccities.api.settings;

import de.raidcraft.rccities.api.city.City;

/**
 * @author Philip Urban
 */
public abstract class AbstractCitySetting extends AbstractSetting implements CitySetting {

    private City city;

    @Override
    public City getCity() {

        return city;
    }

    @Override
    public void setCity(City city) {

        this.city = city;
    }
}
