package de.strasse36.rccities.database;

/**
 * Author: Philip Urban
 * Date: 29.02.12 - 01:20
 * Description:
 */
public class TableNames {
    public static final String cityTable = "cities";
    public static final String residentTable = "residents";
    public static final String plotTable = "plots";
    public static final String assignmentsTable = "assignments";

    public static String getCityTable() {
        return cityTable;
    }

    public static String getResidentTable() {
        return residentTable;
    }

    public static String getPlotTable() {
        return plotTable;
    }

    public static String getAssignmentsTable() {
        return assignmentsTable;
    }
}
