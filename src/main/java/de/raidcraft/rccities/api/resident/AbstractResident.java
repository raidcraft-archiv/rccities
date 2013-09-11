package de.raidcraft.rccities.api.resident;

import de.raidcraft.rccities.api.city.City;

/**
 * @author Philip Urban
 */
public abstract class AbstractResident implements Resident {

    private int id;
    private String name;
    private Profession profession;
    private City city;

    protected AbstractResident() {};

    public AbstractResident(String name, Profession profession, City city) {

        this.name = name;
        this.profession = profession;
        this.city = city;

        save();
    }

    protected void setId(int id) {

        this.id = id;
    }

    protected void setName(String name) {

        this.name = name;
    }

    protected void setProfession(String profession) {

        this.profession = Profession.valueOf(profession);
    }

    protected void setCity(City city) {

        this.city = city;
    }

    @Override
    public int getId() {

        return id;
    }

    @Override
    public String getName() {

        return name;
    }

    @Override
    public Profession getProfession() {

        return profession;
    }

    @Override
    public void setProfession(Profession profession) {

        this.profession = profession;
        save();
    }

    @Override
    public City getCity() {

        return city;
    }
}
