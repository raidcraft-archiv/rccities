package de.strasse36.rccities.util;

import com.silthus.raidcraft.database.UnknownTableException;
import de.strasse36.rccities.database.CityTable;
import de.strasse36.rccities.database.RCCitiesDatabase;
import de.strasse36.rccities.database.ResidentTable;

/**
 * Author: Philip Urban
 * Date: 01.03.12 - 04:14
 * Description:
 */
public class TableGetter {
    
    public static CityTable getCityTable() throws UnknownTableException
    {
       return ((CityTable) RCCitiesDatabase.get().getTable(RCCitiesDatabase.get().getPrefix()+TableNames.getCityTable()));
    }

    public static ResidentTable getResidentTable() throws UnknownTableException
    {
       return ((ResidentTable) RCCitiesDatabase.get().getTable(RCCitiesDatabase.get().getPrefix()+TableNames.getResidentTable()));
    }
}
