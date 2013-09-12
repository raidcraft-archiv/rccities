package de.raidcraft.rccities.api.settings;

import de.raidcraft.rccities.api.city.City;

/**
 * @author Philip Urban
 */
public abstract class AbstractSetting implements Setting {

    private int id;
    private SettingType type;
    private String value;
    private City city;

    protected AbstractSetting() {}

    protected AbstractSetting(SettingType type, String value, City city) {

        this.type = type;
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

    protected void setType(SettingType type) {

        this.type = type;
    }

    protected void setValue(String value) {

        this.value = value;
    }

    @Override
    public int getId() {

        return id;
    }

    @Override
    public SettingType getType() {

        return type;
    }

    @Override
    public String getValue() {

        return value;
    }

    @Override
    public City getCity() {

        return city;
    }
}
