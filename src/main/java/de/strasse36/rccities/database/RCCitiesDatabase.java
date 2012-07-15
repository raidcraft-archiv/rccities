package de.strasse36.rccities.database;

import com.silthus.raidcraft.database.RCDatabase;
import de.strasse36.rccities.bukkit.RCCitiesPlugin;
import de.strasse36.rccities.config.MainConfig;

/**
 * User: Silthus
 */
public class RCCitiesDatabase extends RCDatabase {

	private static final String PREFIX = "rccities";
    private static RCCitiesDatabase _self;

    public static RCCitiesDatabase get() {
        if (_self == null) {
            _self = new RCCitiesDatabase();
            _self.setupTables();
        }
        return _self;
    }

    private RCCitiesDatabase() {
        super(RCCitiesPlugin.get(), PREFIX);
        addTable(new CityTable(this));
        addTable(new ResidentTable(this));
        addTable(new PlotTable(this));
        addTable(new AssignmentsTable(this));
    }
}
