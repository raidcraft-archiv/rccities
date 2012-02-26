package main.java.de.strasse36.rccities.config;

import com.silthus.raidcraft.bukkit.BukkitBasePlugin;
import com.silthus.raidcraft.config.ConfigManager;

public class MainConfig {

    private static final String FILENAME = "config.yml";
    private static BukkitBasePlugin plugin;
    
    public static void init(BukkitBasePlugin plugin) {
        MainConfig.plugin = plugin;
        load();
    }
    
    private static void load() {
        ConfigManager.loadConfig(FILENAME, plugin);
    }
    
    private static void save() {
        ConfigManager.save(FILENAME, plugin);
    }
    
    public static void reload() {
        ConfigManager.reload(FILENAME, plugin);
    }
}
