package de.strasse36.rccities;

import com.silthus.raidcraft.database.UnknownTableException;
import de.strasse36.rccities.database.CityTable;
import de.strasse36.rccities.database.RCCitiesDatabase;

/**
 * Author: Philip Urban
 * Date: 28.02.12 - 23:12
 * Description:
 */
public class Resident {
    private int id;
    private String name;
    private City city;
    private String profession;

    public Resident() {};

    public Resident(String name, int cityId, String profession)
    {
        this.setName(name);
        try {
            this.setCity(((CityTable)RCCitiesDatabase.get().getTable("rccities_cities")).getCity(id));
        } catch (UnknownTableException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
        this.setProfession(profession);
    }

    public Resident(String name, City city, String profession)
    {
        this.setName(name);
        this.setCity(city);
        this.setProfession(profession);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public City getCity() {
        return city;
    }

    public void setCity(City city) {
        this.city = city;
    }

    public String getProfession() {
        return profession;
    }

    public void setProfession(String profession) {
        this.profession = profession;
    }
}
