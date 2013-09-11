package de.raidcraft.rccities.implementations;

import de.raidcraft.RaidCraft;
import de.raidcraft.rccities.RCCitiesPlugin;
import de.raidcraft.rccities.api.city.City;
import de.raidcraft.rccities.api.settings.AbstractSetting;
import de.raidcraft.rccities.tables.TSetting;

/**
 * @author Philip Urban
 */
public class DatabaseSetting extends AbstractSetting {

    public DatabaseSetting(String key, String value, City city) {

        super(key, value, city);
    }

    public DatabaseSetting(int settingId, City city) {

        //XXX setter call order is important!!!
        setId(settingId);
        setCity(city);

        TSetting tSetting = RaidCraft.getDatabase(RCCitiesPlugin.class).find(TSetting.class, settingId);
        assert tSetting != null : "Kein Setting mit der ID " + settingId + " gefunden!";

        setKey(tSetting.getKey());
        setValue(tSetting.getValue());
    }

    @Override
    public void save() {

        TSetting tSetting = new TSetting();
        tSetting.setCity(getCity());
        tSetting.setKey(getKey());
        tSetting.setValue(getValue());
        RaidCraft.getDatabase(RCCitiesPlugin.class).save(tSetting);
        setId(tSetting.getId());
    }

    @Override
    public void delete() {

        RaidCraft.getDatabase(RCCitiesPlugin.class).delete(TSetting.class, getId());
    }
}
