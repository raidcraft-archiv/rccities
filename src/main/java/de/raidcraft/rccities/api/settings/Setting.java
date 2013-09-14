package de.raidcraft.rccities.api.settings;

/**
 * @author Philip Urban
 */
public interface Setting {

    public String getName();

    public String getValue();

    public void setValue(String value);

    public void refresh();
}
