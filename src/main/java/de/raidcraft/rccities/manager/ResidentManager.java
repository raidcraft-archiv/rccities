package de.raidcraft.rccities.manager;

import de.raidcraft.RaidCraft;
import de.raidcraft.rccities.DatabaseResident;
import de.raidcraft.rccities.RCCitiesPlugin;
import de.raidcraft.rccities.api.city.City;
import de.raidcraft.rccities.api.resident.Resident;
import de.raidcraft.rccities.tables.TResident;
import de.raidcraft.util.CaseInsensitiveMap;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author Philip Urban
 */
public class ResidentManager {

    private RCCitiesPlugin plugin;
    private Map<String, List<Resident>> cachedResidents = new CaseInsensitiveMap<>();

    public ResidentManager(RCCitiesPlugin plugin) {

        this.plugin = plugin;
    }

    public List<Resident> getCitizenships(String name) {

        List<Resident> residents = cachedResidents.get(name);

        if(residents == null || residents.size() == 0) {
            List<TResident> tResidents = RaidCraft.getDatabase(RCCitiesPlugin.class).find(TResident.class).where().ieq("name", name).findList();
            if(tResidents != null && tResidents.size() > 0) {
                cachedResidents.put(name, new ArrayList<Resident>());
                for(TResident tResident : tResidents) {
                    Resident resident = new DatabaseResident(tResident);
                    cachedResidents.get(name).add(resident);
                }
            }
        }
        return cachedResidents.get(name);
    }

    public Resident getResident(String name, City city) {

        List<Resident> residents = getCitizenships(name);

        if(residents != null) {
            for(Resident resident : residents) {
                if(resident.getCity().equals(city)) {
                    return resident;
                }
            }
        }

        return null;
    }

}
