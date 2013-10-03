package de.raidcraft.rccities;

import de.raidcraft.RaidCraft;
import de.raidcraft.rccities.api.city.City;
import de.raidcraft.rccities.api.request.AbstractUpgradeRequest;
import de.raidcraft.rccities.tables.TUpgradeRequest;
import de.raidcraft.rcupgrades.api.level.UpgradeLevel;

/**
 * @author Philip Urban
 */
public class DatabaseUpgradeRequest extends AbstractUpgradeRequest {

    public DatabaseUpgradeRequest(City city, UpgradeLevel<City> upgradeLevel, String staffInfo) {

        super(city, upgradeLevel, staffInfo);
    }

    @Override
    public void save() {

        TUpgradeRequest tUpgradeRequest = RaidCraft.getDatabase(RCCitiesPlugin.class).find(TUpgradeRequest.class).where()
                .eq("city_id", getCity().getId()).ieq("level_identifier", getUpgradeLevel().getId()).findUnique();
        if(tUpgradeRequest == null) {
            tUpgradeRequest = new TUpgradeRequest();
            tUpgradeRequest.setCity(getCity());
            tUpgradeRequest.setInfo(getInfo());
            tUpgradeRequest.setLevel_identifier(getUpgradeLevel().getId());
            RaidCraft.getDatabase(RCCitiesPlugin.class).save(tUpgradeRequest);
        }
        else {
            tUpgradeRequest = new TUpgradeRequest();
            tUpgradeRequest.setCity(getCity());
            tUpgradeRequest.setInfo(getInfo());
            tUpgradeRequest.setLevel_identifier(getUpgradeLevel().getId());
            RaidCraft.getDatabase(RCCitiesPlugin.class).update(tUpgradeRequest);
        }
    }

    @Override
    public void delete() {
        TUpgradeRequest tUpgradeRequest = RaidCraft.getDatabase(RCCitiesPlugin.class).find(TUpgradeRequest.class).where()
                .eq("city_id", getCity().getId()).ieq("level_identifier", getUpgradeLevel().getId()).findUnique();
        if(tUpgradeRequest != null) {
            RaidCraft.getDatabase(RCCitiesPlugin.class).delete(tUpgradeRequest);
        }
    }

    @Override
    public void accept() {

        accepted = true;
        getUpgradeLevel().tryToUnlock(getCity());
    }

    @Override
    public void reject(String reason) {

        accepted = false;
        rejectReason = reason;
        rejected = true;
        rejectDate = System.currentTimeMillis();
    }
}
