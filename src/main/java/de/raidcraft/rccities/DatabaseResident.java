package de.raidcraft.rccities;

import de.raidcraft.RaidCraft;
import de.raidcraft.rccities.api.city.City;
import de.raidcraft.rccities.api.resident.AbstractResident;
import de.raidcraft.rccities.api.resident.Profession;
import de.raidcraft.rccities.tables.TResident;
import de.raidcraft.rccities.tables.TSetting;

/**
 * @author Philip Urban
 */
public class DatabaseResident extends AbstractResident {

    public DatabaseResident(String name, Profession profession, City city) {

        super(name, profession, city);
    }

    public DatabaseResident(TResident tResident) {

        //XXX setter call order is important!!!
        setId(tResident.getId());

        City city = RaidCraft.getComponent(RCCitiesPlugin.class).getCityManager().getCity(tResident.getCity().getName());
        assert city != null : "City of resident is null!";
        setCity(city);
        setName(tResident.getName());
        setProfession(Profession.valueOf(tResident.getProfession()));
    }

    @Override
    public void save() {

        // save new resident
        if(getId() == 0) {
            TResident tResident = new TResident();
            tResident.setCity(getCity());
            tResident.setName(getName());
            tResident.setProfession(getProfession().name());
            RaidCraft.getDatabase(RCCitiesPlugin.class).save(tResident);
            setId(tResident.getId());
        }
        // update existing resident
        else {
            TResident tResident = RaidCraft.getDatabase(RCCitiesPlugin.class).find(TResident.class, getId());
            tResident.setProfession(getProfession().name());
            RaidCraft.getDatabase(RCCitiesPlugin.class).update(tResident);
        }
    }

    @Override
    public void delete() {

        RaidCraft.getDatabase(RCCitiesPlugin.class).delete(TSetting.class, getId());
    }
}
