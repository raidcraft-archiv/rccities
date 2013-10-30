package de.raidcraft.rccities.listener;

import de.raidcraft.RaidCraft;
import de.raidcraft.rccities.RCCitiesPlugin;
import de.raidcraft.rcconversations.RCConversationsPlugin;
import org.bukkit.entity.Monster;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.CreatureSpawnEvent;

/**
 * @author Philip Urban
 */
public class EntityListener implements Listener {

    @EventHandler
    public void onEntitySpwan(CreatureSpawnEvent event) {

        if(RaidCraft.getComponent(RCConversationsPlugin.class).getCitizens().getNPCRegistry().isNPC(event.getEntity())) return;
        if(!(event.getEntity() instanceof Monster)) return;
        if(RaidCraft.getComponent(RCCitiesPlugin.class).getPlotManager().getPlot(event.getEntity().getLocation().getChunk()) == null) return;

        event.getEntity().remove();
        event.setCancelled(true);
    }
}
