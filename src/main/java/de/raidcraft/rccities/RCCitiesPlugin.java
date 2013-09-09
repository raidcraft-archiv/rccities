package de.raidcraft.rccities;

import de.raidcraft.api.BasePlugin;
import de.raidcraft.rccities.commands.TownCommands;
import de.raidcraft.rccities.tables.*;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Philip Urban
 */
public class RCCitiesPlugin extends BasePlugin {

    @Override
    public void enable() {

        registerCommands(TownCommands.class);
    }

    @Override
    public void disable() {
    }

    @Override
    public List<Class<?>> getDatabaseClasses() {

        List<Class<?>> databases = new ArrayList<>();
        databases.add(TCity.class);
        databases.add(TPlot.class);
        databases.add(TResident.class);
        databases.add(TAssignment.class);
        databases.add(TLocation.class);
        databases.add(TSetting.class);
        return databases;
    }
}
