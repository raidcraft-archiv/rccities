package de.strasse36.rccities.bukkit;

import com.silthus.raidcraft.bukkit.BukkitBasePlugin;
import de.strasse36.rccities.commands.PlotCommandAllocater;
import de.strasse36.rccities.commands.TownCommandAllocater;
import de.strasse36.rccities.database.RCCitiesDatabase;
import de.strasse36.rccities.listeners.BlockListener;
import de.strasse36.rccities.listeners.PlayerListener;
import de.strasse36.rccities.util.TableHandler;
import de.strasse36.rccities.util.Taxes;
import org.bukkit.Bukkit;

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
        _self = this;
        Bukkit.getServer().getPluginManager().registerEvents(new PlayerListener(), this);
        getServer().getPluginManager().registerEvents(new BlockListener(), this);
        registerCommand("rccities", new TownCommandAllocater());
        registerCommand("plot", new PlotCommandAllocater());
        RCCitiesDatabase.init();
        TableHandler.init();
        Taxes.init();
//        if(MainConfig.isDynmapEnabled())
//            Dynmap.init();
    }

    public static RCCitiesPlugin get()
    {
        return _self;
    }
}
