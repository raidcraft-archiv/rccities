package de.strasse36.rccities.bukkit;

import com.silthus.raidcraft.bukkit.BukkitBasePlugin;
import de.strasse36.rccities.commands.CommandUtility;
import de.strasse36.rccities.config.MainConfig;
import de.strasse36.rccities.listeners.PlayerListener;

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
        MainConfig.init(this);
        registerCommand("rccities", new CommandUtility());
        _self = this;
        getServer().getPluginManager().registerEvents(new PlayerListener(), this);
    }

    public static RCCitiesPlugin get()
    {
        return _self;
    }
}
