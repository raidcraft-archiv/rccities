package de.raidcraft.rccities.api.flags;

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

        FlagInformation annotation = getClass().getAnnotation(FlagInformation.class);
        this.name = StringUtils.formatName(annotation.name());
        this.type = annotation.type();
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
