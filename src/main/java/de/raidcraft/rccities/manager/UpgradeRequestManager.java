package de.raidcraft.rccities.manager;

import de.raidcraft.RaidCraft;
import de.raidcraft.rccities.DatabaseUpgradeRequest;
import de.raidcraft.rccities.RCCitiesPlugin;
import de.raidcraft.rccities.api.city.City;
import de.raidcraft.rccities.api.request.UpgradeRequest;
import de.raidcraft.rccities.tables.TUpgradeRequest;
import de.raidcraft.rcupgrades.api.level.UpgradeLevel;
import org.apache.commons.lang.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Philip Urban
 */
public class UpgradeRequestManager {

    private RCCitiesPlugin plugin;
    private UpgradeRequestInformTask informTask;

    public UpgradeRequestManager(RCCitiesPlugin plugin) {

        this.plugin = plugin;

        informTask = new UpgradeRequestInformTask(this);
        Bukkit.getScheduler().runTaskTimer(plugin, informTask, 5 * 60 * 20, 5 * 60 * 20);
    }

    public UpgradeRequest getRequest(City city, UpgradeLevel upgradeLevel) {

        TUpgradeRequest tUpgradeRequest = RaidCraft.getDatabase(RCCitiesPlugin.class).find(TUpgradeRequest.class)
                .where().eq("city_id", city.getId()).ieq("level_identifier", upgradeLevel.getId()).findUnique();
        if(tUpgradeRequest == null) {
            return null;
        }
        return new DatabaseUpgradeRequest(tUpgradeRequest);
    }

    public List<UpgradeRequest> getOpenRequests() {

        List<UpgradeRequest> requests = new ArrayList<>();
        List<TUpgradeRequest> tUpgradeRequests = RaidCraft.getDatabase(RCCitiesPlugin.class).find(TUpgradeRequest.class)
                .where().eq("rejected", false).eq("accepted", false).findList();
        for(TUpgradeRequest tUpgradeRequest : tUpgradeRequests) {
            requests.add(new DatabaseUpgradeRequest(tUpgradeRequest));
        }
        return requests;
    }

    public List<UpgradeRequest> getOpenRequests(City city) {

        List<UpgradeRequest> requests = new ArrayList<>();
        List<TUpgradeRequest> tUpgradeRequests = RaidCraft.getDatabase(RCCitiesPlugin.class).find(TUpgradeRequest.class)
                .where().eq("city_id", city.getId()).eq("rejected", false).eq("accepted", false).findList();
        for(TUpgradeRequest tUpgradeRequest : tUpgradeRequests) {
            requests.add(new DatabaseUpgradeRequest(tUpgradeRequest));
        }
        return requests;
    }

    public class UpgradeRequestInformTask implements Runnable {

        UpgradeRequestManager upgradeRequestManager;

        public UpgradeRequestInformTask(UpgradeRequestManager upgradeRequestManager) {

            this.upgradeRequestManager = upgradeRequestManager;
        }

        @Override
        public void run() {

            List<UpgradeRequest> openRequests = upgradeRequestManager.getOpenRequests();
            if(openRequests.size() == 0) return;

            String permission  = "rccities.upgrades.process";
            Bukkit.broadcast(ChatColor.GRAY + "Es liegen Upgrade-Anfragen von Gilden vor:", permission);
            Bukkit.broadcast(ChatColor.GRAY + StringUtils.join(openRequests, ", ") + " (nutze /gilde upgrade <Gilde>)", permission);
        }
    }
}
