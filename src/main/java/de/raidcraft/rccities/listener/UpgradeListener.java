package de.raidcraft.rccities.listener;

import de.raidcraft.rccities.api.city.City;
import de.raidcraft.rcupgrades.events.UpgradeUnlockEvent;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

/**
 * @author Philip Urban
 */
public class UpgradeListener implements Listener {

    @EventHandler
    public void onUnlockUpgrade(UpgradeUnlockEvent event) {

        if(!(event.getUpgradeLevel().getUpgradeHolder().getObject() instanceof City)) return;

        City city = (City)event.getUpgradeLevel().getUpgradeHolder().getObject();
        int maxLevel = event.getUpgradeLevel().getLevel();
        Bukkit.broadcastMessage(ChatColor.GOLD + "Die Gilde '" + city.getFriendlyName() + "' ist auf Level " + ChatColor.RED + maxLevel + ChatColor.GOLD + " aufgestiegen!");
    }
}
