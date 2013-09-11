package de.raidcraft.rccities.api.settings;

import de.raidcraft.rccities.api.city.City;

/**
 * @author Philip Urban
 */
public abstract class AbstractSetting implements Setting {

    private int id;
    private String key;
    private String value;
    private City city;

    protected AbstractSetting() {}

    protected AbstractSetting(String key, String value, City city) {

        this.key = key;
        this.value = value;
        this.city = city;

        save();
    }

    protected void setCity(City city) {

        this.city = city;
    }

    protected void setId(int id) {

        this.id = id;
    }

    protected void setKey(String key) {

        this.key = key;
    }

    protected void setValue(String value) {

        this.value = value;
    }

    @Override
    public int getId() {

        return id;
    }

    @Override
    public String getKey() {

        return key;
    }

    @Override
    public String getValue() {

        return value;
    }

    @Override
    public SettingType getSettingType() {

        return SettingType.valueOf(key);
    }

    @Override
    public City getCity() {

        return city;
    }
}
