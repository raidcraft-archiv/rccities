package de.raidcraft.rccities.listener;

import de.raidcraft.skills.api.events.RCExpGainEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

/**
 * @author Philip Urban
 */
public class ExpListener implements Listener {

    @EventHandler(ignoreCancelled = true)
    public void onExpGain(RCExpGainEvent event) {

        //TODO
    }

}
