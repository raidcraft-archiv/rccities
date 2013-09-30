package de.raidcraft.rccities;

import com.sk89q.worldedit.bukkit.WorldEditPlugin;
import com.sk89q.worldguard.bukkit.WorldGuardPlugin;
import de.raidcraft.api.BasePlugin;
import de.raidcraft.api.config.ConfigurationBase;
import de.raidcraft.api.config.Setting;
import de.raidcraft.api.config.SimpleConfiguration;
import de.raidcraft.api.requirement.RequirementManager;
import de.raidcraft.api.reward.RewardManager;
import de.raidcraft.rccities.api.city.City;
import de.raidcraft.rccities.api.plot.Plot;
import de.raidcraft.rccities.commands.PlotCommands;
import de.raidcraft.rccities.commands.ResidentCommands;
import de.raidcraft.rccities.commands.TownCommands;
import de.raidcraft.rccities.conversation.*;
import de.raidcraft.rccities.flags.city.GreetingsCityFlag;
import de.raidcraft.rccities.flags.city.JoinCostsCityFlag;
import de.raidcraft.rccities.flags.city.PvpCityFlag;
import de.raidcraft.rccities.flags.city.admin.InviteCityFlag;
import de.raidcraft.rccities.flags.plot.MarkPlotFlag;
import de.raidcraft.rccities.flags.plot.PvpPlotFlag;
import de.raidcraft.rccities.listener.ExpListener;
import de.raidcraft.rccities.manager.*;
import de.raidcraft.rccities.requirements.CityExpRequirement;
import de.raidcraft.rccities.rewards.CityPlotsReward;
import de.raidcraft.rccities.tables.*;
import de.raidcraft.rcconversations.actions.ActionManager;
import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Philip Urban
 */
public class RCCitiesPlugin extends BasePlugin {

    private WorldGuardPlugin worldGuard;
    private WorldEditPlugin worldEdit;
    private LocalConfiguration config;
    private ConfigurationSection upgradeConfiguration;

    private CityManager cityManager;
    private PlotManager plotManager;
    private ResidentManager residentManager;
    private AssignmentManager assignmentManager;
    private FlagManager flagManager;
    private SchematicManager schematicManager;
    private DynmapManager dynmapManager;
    private WorldGuardManager worldGuardManager;

    @Override
    public void enable() {

        registerCommands(TownCommands.class);
        registerCommands(ResidentCommands.class);
        registerCommands(PlotCommands.class);

        registerEvents(new ExpListener());

        worldGuard = (WorldGuardPlugin) Bukkit.getPluginManager().getPlugin("WorldGuard");
        worldEdit = (WorldEditPlugin) Bukkit.getPluginManager().getPlugin("WorldEdit");

        reload();

        cityManager = new CityManager(this);
        plotManager = new PlotManager(this);
        residentManager = new ResidentManager(this);
        assignmentManager = new AssignmentManager(this);
        flagManager = new FlagManager(this);
        schematicManager = new SchematicManager(this);
        dynmapManager = new DynmapManager();
        worldGuardManager = new WorldGuardManager(this, worldGuard);

        // city flags
        flagManager.registerCityFlag(PvpCityFlag.class);
        flagManager.registerCityFlag(GreetingsCityFlag.class);
        flagManager.registerCityFlag(InviteCityFlag.class);
        flagManager.registerCityFlag(JoinCostsCityFlag.class);

        // plot flags
        flagManager.registerPlotFlag(MarkPlotFlag.class);
        flagManager.registerPlotFlag(PvpPlotFlag.class);

        flagManager.loadExistingFlags();

        // conversation actions
        ActionManager.registerAction(new FindCityAction());
        ActionManager.registerAction(new IsCityMemberAction());
        ActionManager.registerAction(new HasRolePermissionAction());
        ActionManager.registerAction(new DepositAction());
        ActionManager.registerAction(new WithdrawAction());
        ActionManager.registerAction(new SendJoinRequestAction());
        ActionManager.registerAction(new AcceptJoinRequestAction());
        ActionManager.registerAction(new RejectJoinRequestAction());
        ActionManager.registerAction(new HasCitizenshipAction());
        ActionManager.registerAction(new ListJoinRequestsAction());
        ActionManager.registerAction(new EditJoinRequestAction());
        ActionManager.registerAction(new ListUpgradeTypesAction());

        // upgrade rewards
        RewardManager.registerRewardType(CityPlotsReward.class);

        // upgrade requirements
        RequirementManager.registerRequirementType(CityExpRequirement.class);

        // create regions if they don't exist
        for(City city : cityManager.getCities()) {
            for(Plot plot : plotManager.getPlots(city)) {
                if(plot.getRegion() == null) {
                    plot.updateRegion(true);
                }
            }
        }
    }

    @Override
    public void reload() {

        config = configure(new LocalConfiguration(this));

        // load upgrade holder
        for(File file : getDataFolder().listFiles()) {
            if (file.getName().equalsIgnoreCase(config.upgradeHolder + ".yml")) {
                upgradeConfiguration = configure(new SimpleConfiguration<>(this, file));
            }
        }
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
        databases.add(TCityFlag.class);
        databases.add(TPlotFlag.class);
        databases.add(TJoinRequest.class);
        return databases;
    }

    public class LocalConfiguration extends ConfigurationBase<RCCitiesPlugin> {

        @Setting("ignored-regions")
        public String[] ignoredRegions = new String[]{"rcmap"};
        @Setting("default-town-radius")
        public int defaultMaxRadius = 64;
        @Setting("initial-plot-credit")
        public int initialPlotCredit = 3;
        @Setting("flag-plot-mark-cost")
        public double flagPlotMarkCost = 0.01;
        @Setting("city-upgrade-holder")
        public String upgradeHolder = "city-upgrade-holder";
        @Setting("join-costs")
        public double joinCosts = 0.1;

        public LocalConfiguration(RCCitiesPlugin plugin) {

            super(plugin, "config.yml");
        }
    }

    public WorldGuardPlugin getWorldGuard() {

        return worldGuard;
    }

    public WorldEditPlugin getWorldEdit() {

        return worldEdit;
    }

    public LocalConfiguration getConfig() {

        return config;
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

    public SchematicManager getSchematicManager() {

        return schematicManager;
    }

    public DynmapManager getDynmapManager() {

        return dynmapManager;
    }

    public WorldGuardManager getWorldGuardManager() {

        return worldGuardManager;
    }

    public ConfigurationSection getUpgradeConfiguration() {

        return upgradeConfiguration;
    }
}
