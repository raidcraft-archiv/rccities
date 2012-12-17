package de.strasse36.rccities.config;

import com.silthus.raidcraft.bukkit.BukkitBasePlugin;
import com.silthus.raidcraft.config.RCConfig;
import com.silthus.raidcraft.database.AbstractDatabaseConfig;
import de.strasse36.rccities.bukkit.RCCitiesPlugin;
import org.bukkit.configuration.ConfigurationSection;

import java.util.List;

/**
 *
 * 17.12.11 - 11:27
 * @author Silthus
 */
public class MainConfig extends RCConfig {

    private static final String FILENAME = "config.yml";
    private static MainConfig self;

    public MainConfig(BukkitBasePlugin plugin) {
        super(plugin, FILENAME);
    }

    public static MainConfig get() {
        if (self == null) {
            self = new MainConfig(RCCitiesPlugin.get());
        }
        return self;
    }
    
    public static ConfigurationSection getRCCitiesSection() {
        return get().getConfig().getConfigurationSection("rccities");
    }
    
    public static int getChunksPerPlayer()
    {
        return getRCCitiesSection().getInt("chunksPerPlayer", 4);
    }

    public static double getChunkPrice()
    {
        return getRCCitiesSection().getDouble("chunkPrice", 0);
    }

    public static double getClaimPrice()
    {
        return getRCCitiesSection().getDouble("claimPrice", 0);
    }

    public static double getMarkPrice()
    {
        return getRCCitiesSection().getDouble("markPrice", 0);
    }

    public static int getTownspawnWarmup()
    {
        return getRCCitiesSection().getInt("townspawnWarmup", 0);
    }

    public static int getTownspawnCooldown()
    {
        return getRCCitiesSection().getInt("townspawnCooldown", 0);
    }

    public static int getTownspawnCooldownForeign()
    {
        return getRCCitiesSection().getInt("townspawnCooldownForeign", 0);
    }

    public static String getCityWorld()
    {
        return getRCCitiesSection().getString("cityWorld", "world");
    }

    public static int getLevyInterval()
    {
        return getRCCitiesSection().getInt("levyInterval", 24);
    }

    public static double getTaxAmount()
    {
        return getRCCitiesSection().getInt("taxAmount", 50);
    }

    public static int getPlotsPerPenalty()
    {
        return getRCCitiesSection().getInt("plotsPerPenalty", 1);
    }

    public static String getRCCitiesSignTag()
    {
        return getRCCitiesSection().getString("rccitiesSignTag", "[Town]");
    }
    
    public static List<String> getIgnoredRegions() {
        return getRCCitiesSection().getStringList("ignoredRegions");
    }

    public static boolean isBuildProtection() {
        return getRCCitiesSection().getBoolean("buildProtection", true);
    }

    public static ConfigurationSection getSignTopicSection() {
        return get().getConfig().getConfigurationSection("signTopics");
    }

    public static String getSignTopicJoinTown()
    {
        return getSignTopicSection().getString("joinTown", "Beitreten");
    }

    public static String getSignTopicTownBank()
    {
        return getSignTopicSection().getString("townBank", "Kontostand");
    }
}
