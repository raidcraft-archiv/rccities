package de.raidcraft.rccities.api.settings;

import de.raidcraft.api.RaidCraftException;
import de.raidcraft.util.StringUtils;

/**
 * @author Philip Urban
 */
public abstract class AbstractSetting implements Setting {

    private String name;
    private String value;
    private SettingType type;

    protected AbstractSetting() {

        SettingInformation information = getClass().getAnnotation(SettingInformation.class);
        this.name = StringUtils.formatName(information.name());
        this.type = information.type();
    }

    @Override
    public String getValue() {

        return value;
    }

    @Override
    public void setValue(String value) throws RaidCraftException {

        if(!type.validate(value)) throw new RaidCraftException(type.getErrorMsg());

        this.value = value;
        refresh();
    }

    @Override
    public String getName() {

        return name;
    }

    @Override
    public SettingType getType() {

        return type;
    }
}
