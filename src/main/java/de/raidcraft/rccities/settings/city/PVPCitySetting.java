package de.raidcraft.rccities.settings.city;

import de.raidcraft.rccities.api.settings.AbstractCitySetting;
import de.raidcraft.rcconversations.api.action.ActionInformation;

/**
 * @author Philip Urban
 */
@ActionInformation(name = "PVP")
public class PVPCitySetting extends AbstractCitySetting {

    @Override
    public void refresh() {

        if(getCity() == null) return;
    }
}
