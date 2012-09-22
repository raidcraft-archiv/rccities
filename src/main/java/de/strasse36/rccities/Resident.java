package de.strasse36.rccities;

import de.strasse36.rccities.database.TableHandler;
import de.strasse36.rccities.util.Profession;

import java.util.ArrayList;
import java.util.List;

/**
 * Author: Philip Urban
 * Date: 28.02.12 - 23:12
 * Description:
 */
public class Resident {
    private String name;
    private List<CityProperties> citiesProperties = new ArrayList<CityProperties>();

    public Resident() {};

    public Resident(String player)
    {
        this.name = player;
        citiesProperties = TableHandler.get().getResidentTable().getCitiesProperties(player);
    }

    public String getName() {
        return name;
    }

    public List<City> getCities() {
        List<City> cities = new ArrayList<City>();
        for(CityProperties cityProperties : citiesProperties) {
            cities.add(cityProperties.getCity());
        }
        return cities;
    }
    
    public CityProperties getCityProperty(City city) {
        for(CityProperties cityProperties : citiesProperties) {
            if(cityProperties.getCity().getId() == city.getId()) {
                return cityProperties;
            }
        }
        return null;
    }

    public void addCity(City city, String profession) {
        for(CityProperties cityProperties : citiesProperties) {
            if(cityProperties.getCity().getId() == city.getId()) {
                return;
            }
        }
        citiesProperties.add(new CityProperties(city, profession));
    }
    
    public void removeCity(City city) {
        for(CityProperties cityProperties : citiesProperties) {
            if(cityProperties.getCity().getId() == city.getId()) {
                citiesProperties.remove(cityProperties);
                return;
            }
        }
    }
    
    public boolean isResident(City city) {
        if(getCityProperty(city) != null) {
            return true;
        }
        return false;
    }

    public String getProfession(City city) {
        CityProperties cityProperties = getCityProperty(city);
        if(cityProperties != null) {
            return cityProperties.getProfession();
        }
        return "";
    }

    public void setProfession(City city, String profession) {
        for(CityProperties cityProperties : citiesProperties) {
            if(cityProperties.getCity().getId() == city.getId()) {
                cityProperties.setProfession(profession);
                return;
            }
        }
    }

    public boolean isMayor(City city)
    {
        CityProperties cityProperties = getCityProperty(city);
        if(cityProperties == null) {
            return false;
        }
        return cityProperties.getProfession().equalsIgnoreCase(Profession.Professions.MAYOR.getDbName());
    }

    public boolean isViceMayor(City city)
    {
        CityProperties cityProperties = getCityProperty(city);
        if(cityProperties == null) {
            return false;
        }
        return cityProperties.getProfession().equalsIgnoreCase(Profession.Professions.VICEMAYOR.getDbName());
    }

    public boolean isAssistant(City city)
    {
        CityProperties cityProperties = getCityProperty(city);
        if(cityProperties == null) {
            return false;
        }
        return cityProperties.getProfession().equalsIgnoreCase(Profession.Professions.ASSISTANT.getDbName());
    }

    public boolean isStaff(City city)
    {
        return (isMayor(city) || isViceMayor(city) || isAssistant(city));
    }

    public boolean isLeadership(City city)
    {
        return (isMayor(city) || isViceMayor(city));
    }

    public class CityProperties {
        private City city;
        private String profession;

        public CityProperties(City city, String profession) {
            this.city = city;
            this.profession = profession;
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
}
