package de.raidcraft.rccities;

import com.sk89q.worldedit.bukkit.WorldEditPlugin;
import com.sk89q.worldguard.bukkit.WorldGuardPlugin;
import de.raidcraft.api.BasePlugin;
import de.raidcraft.rccities.commands.TownCommands;
import de.raidcraft.rccities.manager.*;
import de.raidcraft.rccities.settings.city.PVPCityFlag;
import de.raidcraft.rccities.tables.*;
import org.bukkit.Bukkit;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Philip Urban
 */
public class RCCitiesPlugin extends BasePlugin {

    private WorldGuardPlugin worldGuard;
    private WorldEditPlugin worldEdit;

    private CityManager cityManager;
    private PlotManager plotManager;
    private ResidentManager residentManager;
    private AssignmentManager assignmentManager;
    private FlagManager flagManager;

    @Override
    public void enable() {

        registerCommands(TownCommands.class);

        worldGuard = (WorldGuardPlugin) Bukkit.getPluginManager().getPlugin("WorldGuard");
        worldEdit = (WorldEditPlugin) Bukkit.getPluginManager().getPlugin("WorldEdit");

        cityManager = new CityManager(this);
        plotManager = new PlotManager(this);
        residentManager = new ResidentManager(this);
        assignmentManager = new AssignmentManager(this);
        flagManager = new FlagManager(this);

        flagManager.registerCityFlag(PVPCityFlag.class);
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
        databases.add(TFlag.class);
        return databases;
    }

    public WorldGuardPlugin getWorldGuard() {

        return worldGuard;
    }

    public WorldEditPlugin getWorldEdit() {

        return worldEdit;
    }

    public CityManager getCityManager() {

        return cityManager;
    }

    public PlotManager getPlotManager() {

        return plotManager;
    }

    public ResidentManager getResidentManager() {

        return residentManager;
    }

    public AssignmentManager getAssignmentManager() {

        return assignmentManager;
    }

    public FlagManager getFlagManager() {

        return flagManager;
    }
}
