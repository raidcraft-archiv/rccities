package de.raidcraft.rccities.api.settings;

import de.raidcraft.util.StringUtils;

/**
 * @author Philip Urban
 */
public abstract class AbstractSetting implements Setting {

    private String name;
    private String value;

    protected AbstractSetting() {

        this.name = StringUtils.formatName(getClass().getAnnotation(SettingInformation.class).name());
    }

    @Override
    public String getValue() {

        return value;
    }

    @Override
    public void setValue(String value) {

        this.value = value;
        refresh();
    }

    @Override
    public String getName() {

        return name;
    }
}
