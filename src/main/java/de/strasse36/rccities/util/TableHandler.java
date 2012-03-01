package de.strasse36.rccities.util;

import com.silthus.raidcraft.database.UnknownTableException;
import com.silthus.raidcraft.util.RCLogger;
import de.strasse36.rccities.database.CityTable;
import de.strasse36.rccities.database.RCCitiesDatabase;
import de.strasse36.rccities.database.ResidentTable;

/**
 * Author: Philip Urban
 * Date: 01.03.12 - 04:14
 * Description:
 */
public class TableHandler {
    private static TableHandler _self;
    private CityTable cityTable;
    private ResidentTable residentTable;

    public static void init()
    {
        if(_self == null)
            _self = new TableHandler();
        _self.loadTables();
    }

    private void loadTables()
    {
        try {
            this.cityTable = ((CityTable) RCCitiesDatabase.get().getTable(RCCitiesDatabase.get().getPrefix()+TableNames.getCityTable()));
        } catch (UnknownTableException e) {
            RCLogger.warning("Citytable not found!");
        }

        try {
            this.residentTable = ((ResidentTable) RCCitiesDatabase.get().getTable(RCCitiesDatabase.get().getPrefix()+TableNames.getResidentTable()));
        } catch (UnknownTableException e1) {
            RCLogger.warning("Residenttable not found!");
        }
    }

    public static TableHandler get()
    {
        return _self;
    }

    public CityTable getCityTable()
    {
       return this.cityTable;
    }

    public ResidentTable getResidentTable()
    {
       return this.residentTable;
    }
}
