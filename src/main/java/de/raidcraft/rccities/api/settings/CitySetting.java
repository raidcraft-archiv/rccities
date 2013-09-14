package de.raidcraft.rccities.api.settings;

import de.raidcraft.rccities.api.city.City;

/**
 * @author Philip Urban
 */
public interface CitySetting extends Setting {

    public City getCity();

    public void setCity(City city);
}
