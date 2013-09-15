package de.raidcraft.rccities.api.settings;

import de.raidcraft.api.RaidCraftException;

/**
 * @author Philip Urban
 */
public interface Flag {

    public String getName();

    public FlagType getType();

    public String getValue();

    public void setValue(String value) throws RaidCraftException;

    public void refresh();
}
