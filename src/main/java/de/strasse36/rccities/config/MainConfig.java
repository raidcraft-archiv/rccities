package de.strasse36.rccities.config;

import com.silthus.raidcraft.bukkit.BukkitBasePlugin;
import com.silthus.raidcraft.config.ConfigManager;
import org.bukkit.configuration.ConfigurationSection;

/**
 *
 * 17.12.11 - 11:27
 * @author Silthus
 */
public class MainConfig {

    private static final String FILENAME = "config.yml";
    private static BukkitBasePlugin plugin;

    public static void init(BukkitBasePlugin plugin) {
        MainConfig.plugin = plugin;
        load();
    }

    public static void save() {
        ConfigManager.save(FILENAME, plugin);
    }

    public static void reload() {
        ConfigManager.reload(FILENAME, plugin);
    }

    public static void load() {
        ConfigManager.loadConfig(FILENAME, plugin);
}

    public static ConfigurationSection getConfig() {
        return ConfigManager.getConfig(FILENAME, plugin);
    }



    public static DatabaseConfig getDatabase() {
        return new DatabaseConfig();
    }
    
    public static int getChunksPerPlayer()
    {
        return getConfig().getConfigurationSection("rccities").getInt("chunksPerPlayer", 4);
    }

    public static double getChunkPrice()
    {
        return getConfig().getConfigurationSection("rccities").getDouble("chunkPrice", 0);
    }

    public static double getClaimPrice()
    {
        return getConfig().getConfigurationSection("rccities").getDouble("claimPrice", 0);
    }

    public static double getMarkPrice()
    {
        return getConfig().getConfigurationSection("rccities").getDouble("markPrice", 0);
    }

    public static int getTownspawnWarmup()
    {
        return getConfig().getConfigurationSection("rccities").getInt("townspawnWarmup", 0);
    }

    public static String getCityWorld()
    {
        return getConfig().getConfigurationSection("rccities").getString("cityWorld", "world");
    }

    public static int getLevyInterval()
    {
        return getConfig().getConfigurationSection("rccities").getInt("levyInterval", 24);
    }

    public static double getTaxAmount()
    {
        return getConfig().getConfigurationSection("rccities").getInt("taxAmount", 50);
    }

    public static int getPlotsPerPenalty()
    {
        return getConfig().getConfigurationSection("rccities").getInt("plotsPerPenalty", 1);
    }

    public static boolean isDynmapEnabled()
    {
        return getConfig().getConfigurationSection("rccities").getBoolean("dynmapSupport", false);
    }

    public static class DatabaseConfig {

        private ConfigurationSection section;

        public DatabaseConfig() {
            this.section = getConfig().getConfigurationSection("database");
        }

        public String getType() {
            return section.getString("type");
        }

        public String getName() {
            return section.getString("database");
        }

        public String getUsername() {
            return section.getString("username");
        }

        public String getPassword() {
            return section.getString("password");
        }

        public String getUrl() {
            return section.getString("url");
        }

        public String getPrefix() {
            return section.getString("prefix");
        }
    }
}
