package de.raidcraft.rccities.settings.city;

import de.raidcraft.rccities.api.settings.AbstractCitySetting;
import de.raidcraft.rccities.api.settings.SettingInformation;
import de.raidcraft.rccities.api.settings.SettingType;

/**
 * @author Philip Urban
 */
@SettingInformation(
        name = "PVP",
        type = SettingType.BOOLEAN
)
public class PVPCitySetting extends AbstractCitySetting {

    @Override
    public void refresh() {

        if(getCity() == null) return;

        //TODO
    }
}
