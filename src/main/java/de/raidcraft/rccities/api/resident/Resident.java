package de.raidcraft.rccities.api.resident;

import de.raidcraft.rccities.api.city.City;

/**
 * @author Philip Urban
 */
public interface Resident {

    public int getId();

    public String getName();

    public Profession getProfession();

    public void setProfession(Profession profession);

    public City getCity();

    public void save();

    public void delete();
}
