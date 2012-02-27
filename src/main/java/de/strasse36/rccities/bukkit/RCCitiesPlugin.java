package de.strasse36.rccities.bukkit;

import com.silthus.raidcraft.bukkit.BukkitBasePlugin;
import com.silthus.raidcraft.util.RCLogger;
import de.strasse36.rccities.commands.RCCitiesCommand;
import de.strasse36.rccities.config.MainConfig;
import de.strasse36.rccities.database.CitiesDatabase;
import de.strasse36.rccities.listeners.PlayerListener;

import javax.persistence.PersistenceException;
import java.util.ArrayList;
import java.util.List;

/**
 * Author: Philip Urban
 * Date: 26.02.12 - 20:10
 * Description:
 */
public class RCCitiesPlugin extends BukkitBasePlugin
{
    private static RCCitiesPlugin _self;

    @Override
    public void registerEvents() {
        setupDatabase();
        MainConfig.init(this);
        registerCommand("rccities", new RCCitiesCommand());
        _self = this;
        getServer().getPluginManager().registerEvents(new PlayerListener(), this);
    }

    public static RCCitiesPlugin get()
    {
        return _self;
    }

    private void setupDatabase() {
        try {
            getDatabase().find(CitiesDatabase.class).findRowCount();
        } catch (PersistenceException ex) {
            RCLogger.info("Installing database for " + getDescription().getName() + " due to first time usage.");
            installDDL();
        }
    }

    @Override
    public List<Class<?>> getDatabaseClasses() {
        List<Class<?>> list = new ArrayList<Class<?>>();
        list.add(CitiesDatabase.class);
        return list;
    }
}
