package de.raidcraft.rccities;

import de.raidcraft.api.BasePlugin;
import de.raidcraft.rccities.commands.TownCommands;
import de.raidcraft.rccities.tables.TAssignments;
import de.raidcraft.rccities.tables.TCities;
import de.raidcraft.rccities.tables.TPlots;
import de.raidcraft.rccities.tables.TResidents;

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
        databases.add(TCities.class);
        databases.add(TPlots.class);
        databases.add(TResidents.class);
        databases.add(TAssignments.class);
        return databases;
    }
}
