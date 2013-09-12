package de.raidcraft.rccities.api.settings;

import de.raidcraft.rccities.api.city.City;

/**
 * @author Philip Urban
 */
public interface Setting {

    public int getId();

    public SettingType getType();

    public String getValue();

    public City getCity();

    public void refresh();

    public void save();

    public void delete();
}
