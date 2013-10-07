package de.raidcraft.rccities.listener;

import de.raidcraft.RaidCraft;
import de.raidcraft.api.events.RCPlayerGainExpEvent;
import de.raidcraft.rccities.RCCitiesPlugin;
import de.raidcraft.rccities.api.resident.Resident;
import de.raidcraft.rccities.api.resident.Role;
import de.raidcraft.rccities.api.resident.RolePermission;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import java.util.List;

/**
 * @author Philip Urban
 */
public class ExpListener implements Listener {

    @EventHandler(ignoreCancelled = true)
    public void onExpGain(RCPlayerGainExpEvent event) {

        if(event.getGainedExp() < 0) return;

        RCCitiesPlugin plugin = RaidCraft.getComponent(RCCitiesPlugin.class);
        List<Resident> residents = plugin.getResidentManager().getCitizenships(event.getPlayer().getName());
        if(residents == null) return;
        boolean slave = false;
        Resident resident = null;
        for(Resident res : residents) {
            if(res.getRole() == Role.SLAVE) {
                slave = true;
                resident = res;
            }
            if(!slave && res.getRole().hasPermission(RolePermission.COLLECT_EXP)) {
                resident = res;
            }
        }
        if(resident != null) {
            resident.getCity().addExp(event.getGainedExp());
        }
    }

}
