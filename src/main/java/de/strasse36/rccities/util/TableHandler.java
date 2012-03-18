package de.strasse36.rccities.util;

import de.strasse36.rccities.database.*;

/**
 * Author: Philip Urban
 * Date: 01.03.12 - 04:14
 * Description:
 */
public class TableHandler {
    private static TableHandler _self;
	private final RCCitiesDatabase database;

    public static void init()
    {
        if(_self == null)
            _self = new TableHandler();
    }

	private TableHandler() {
		this.database = RCCitiesDatabase.get();
	}

    public static TableHandler get()
    {
        return _self;
    }

    public CityTable getCityTable() {
	    return database.getTable(CityTable.class);
    }

    public ResidentTable getResidentTable() {
	    return database.getTable(ResidentTable.class);
    }

    public PlotTable getPlotTable() {
	    return database.getTable(PlotTable.class);
    }

    public AssignmentsTable getAssignmentsTable() {
	    return database.getTable(AssignmentsTable.class);
    }
}
