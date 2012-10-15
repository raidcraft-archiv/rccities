package de.strasse36.rccities;

import de.strasse36.rccities.database.TableHandler;

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
        this.setCity(TableHandler.get().getCityTable().getCity(id));
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

    public boolean isMayor()
    {
        return this.getProfession().equalsIgnoreCase("mayor");
    }

    public boolean isViceMayor()
    {
        return this.getProfession().equalsIgnoreCase("vicemayor");
    }

    public boolean isAssistant()
    {
        return this.getProfession().equalsIgnoreCase("assistant");
    }

    public boolean isStaff()
    {
        return (isMayor() || isViceMayor() || isAssistant());
    }

    public boolean isLeadership()
    {
        return (isMayor() || isViceMayor());
    }
}
