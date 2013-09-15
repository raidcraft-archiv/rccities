package de.raidcraft.rccities.api.settings;

import de.raidcraft.api.RaidCraftException;
import de.raidcraft.util.StringUtils;

/**
 * @author Philip Urban
 */
public abstract class AbstractFlag implements Flag {

    private String name;
    private String value;
    private FlagType type;

    protected AbstractFlag() {

        FlagInformation information = getClass().getAnnotation(FlagInformation.class);
        this.name = StringUtils.formatName(information.name());
        this.type = information.type();
    }

    @Override
    public String getValue() {

        return value;
    }

    @Override
    public void setValue(String value) throws RaidCraftException {

        if(!type.validate(value)) throw new RaidCraftException("Falscher Wertetyp: " + type.getErrorMsg());

        this.value = value;
        refresh();
    }

    @Override
    public String getName() {

        return name;
    }

    @Override
    public FlagType getType() {

        return type;
    }
}
