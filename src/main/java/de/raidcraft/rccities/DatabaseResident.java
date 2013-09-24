package de.raidcraft.rccities;

import de.raidcraft.RaidCraft;
import de.raidcraft.rccities.api.city.City;
import de.raidcraft.rccities.api.plot.Plot;
import de.raidcraft.rccities.api.resident.AbstractResident;
import de.raidcraft.rccities.api.resident.Role;
import de.raidcraft.rccities.tables.TResident;

/**
 * @author Philip Urban
 */
public class DatabaseResident extends AbstractResident {

    public DatabaseResident(String name, Role profession, City city) {

        super(name, profession, city);
    }

    public DatabaseResident(TResident tResident) {

        //XXX setter call order is important!!!
        this.id = tResident.getId();

        City city = RaidCraft.getComponent(RCCitiesPlugin.class).getCityManager().getCity(tResident.getCity().getName());
        assert city != null : "City of resident is null!";
        this.city = city;
        this.name = tResident.getName();
        setRole(Role.valueOf(tResident.getProfession()));
    }

    @Override
    public void save() {

        // save new resident
        if(getId() == 0) {
            TResident tResident = new TResident();
            tResident.setCity(getCity());
            tResident.setName(getName());
            tResident.setProfession(getRole().name());
            RaidCraft.getDatabase(RCCitiesPlugin.class).save(tResident);
            this.id = tResident.getId();
        }
        // update existing resident
        else {
            TResident tResident = RaidCraft.getDatabase(RCCitiesPlugin.class).find(TResident.class, getId());
            tResident.setProfession(getRole().name());
            RaidCraft.getDatabase(RCCitiesPlugin.class).update(tResident);
        }
    }

    @Override
    public void delete() {

        RCCitiesPlugin plugin = RaidCraft.getComponent(RCCitiesPlugin.class);

        for(Plot plot : plugin.getPlotManager().getPlots(city)) {
            plot.removeResident(this);
            plot.updateRegion(false);
        }

        plugin.getResidentManager().removeFromCache(this);
        TResident tResident = RaidCraft.getDatabase(RCCitiesPlugin.class).find(TResident.class, getId());
        RaidCraft.getDatabase(RCCitiesPlugin.class).delete(tResident);
    }
}
