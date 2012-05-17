package de.strasse36.rccities.util;

import de.strasse36.rccities.City;
import de.strasse36.rccities.Resident;

/**
 * Author: Philip Urban
 * Date: 01.03.12 - 15:37
 * Description:
 */
public class ResidentHelper {
    
    public static Resident isResident(String name, City city)
    {
        Resident resident = TableHandler.get().getResidentTable().getResident(name);
        if(resident == null || resident.getCity() == null)
            return null;
        if(resident.getCity().getName().equalsIgnoreCase(city.getName()))
        {
            return resident;
        }
        return null;
    }
}
