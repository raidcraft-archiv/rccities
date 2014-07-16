package de.raidcraft.rccities.listener;

import de.raidcraft.RaidCraft;
import de.raidcraft.rccities.RCCitiesPlugin;
import de.raidcraft.rcconversations.RCConversationsPlugin;
import net.citizensnpcs.api.CitizensAPI;
import org.bukkit.entity.Monster;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.CreatureSpawnEvent;

/**
 * @author Philip Urban
 */
public class EntityListener implements Listener {

    @EventHandler(ignoreCancelled = true, priority = EventPriority.MONITOR)
    public void onEntitySpwan(CreatureSpawnEvent event) {

        if(!(event.getEntity() instanceof Monster)) return;
        // TODO: own method
//        if(RaidCraft.getComponent(RCConversationsPlugin.class).getCitizens().getNPCRegistry().isNPC(event.getEntity())) return;
        if(RaidCraft.getComponent(RCCitiesPlugin.class).getPlotManager().getPlot(event.getEntity().getLocation().getChunk()) == null) return;

        event.getEntity().remove();
        event.setCancelled(true);
    }
}
