package de.raidcraft.rccities.listener;

import de.raidcraft.RaidCraft;
import de.raidcraft.rccities.RCCitiesPlugin;
import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.CreatureSpawnEvent;

import java.util.Arrays;

/**
 * @author Philip Urban
 */
public class EntityListener implements Listener {

    public EntityType[] HOSTILE_MOB_TYPES = new EntityType[]{
            EntityType.ZOMBIE,
            EntityType.PIG_ZOMBIE,
            EntityType.GHAST,
            EntityType.SKELETON,
            EntityType.CREEPER,
            EntityType.ENDERMAN,
    };

    @EventHandler
    public void onEntitySpwan(CreatureSpawnEvent event) {

        if(event.getEntity().hasMetadata("NPC")) return;
        if(!Arrays.asList(HOSTILE_MOB_TYPES).contains(event.getEntityType())) return;
        if(RaidCraft.getComponent(RCCitiesPlugin.class).getPlotManager().getPlot(event.getEntity().getLocation().getChunk()) == null) return;

        event.getEntity().remove();
        event.setCancelled(true);
    }
}
