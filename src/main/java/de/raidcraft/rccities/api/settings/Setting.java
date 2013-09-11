package de.raidcraft.rccities.api.settings;

import de.raidcraft.rccities.api.city.City;

/**
 * @author Philip Urban
 */
public interface Setting {

    public int getId();

    public String getKey();

    public String getValue();

    public SettingType getSettingType();

    public City getCity();

    public void save();

    public void delete();
}
