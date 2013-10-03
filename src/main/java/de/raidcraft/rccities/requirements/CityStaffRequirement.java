package de.raidcraft.rccities.requirements;

import de.raidcraft.RaidCraft;
import de.raidcraft.api.requirement.AbstractRequirement;
import de.raidcraft.api.requirement.RequirementInformation;
import de.raidcraft.api.requirement.RequirementResolver;
import de.raidcraft.rccities.DatabaseUpgradeRequest;
import de.raidcraft.rccities.RCCitiesPlugin;
import de.raidcraft.rccities.api.city.City;
import de.raidcraft.rccities.api.request.UpgradeRequest;
import de.raidcraft.rcupgrades.api.level.UpgradeLevel;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;

/**
 * @author Philip Urban
 */
@RequirementInformation("CITY_STAFF")
public class CityStaffRequirement extends AbstractRequirement<City> {

    private String info;

    public CityStaffRequirement(RequirementResolver<City> resolver, ConfigurationSection config) {

        super(resolver, config);
    }

    @Override
    protected void load(ConfigurationSection data) {

        info = data.getString("info");
    }

    @Override
    public boolean isMet(City city) {

        UpgradeLevel<City> upgradeLevel = (UpgradeLevel<City>)getResolver();
        RCCitiesPlugin plugin = RaidCraft.getComponent(RCCitiesPlugin.class);
        UpgradeRequest request = plugin.getUpgradeRequestManager().getRequest(city, upgradeLevel);

        // new request
        if(request == null) {
            request = new DatabaseUpgradeRequest(city, upgradeLevel, info);
            request.save();

            for(Player player : Bukkit.getOnlinePlayers()) {

                if(!player.hasPermission("rccities.upgrades.process")) continue;

                player.sendMessage(ChatColor.GRAY + "Die Gilde '" + city.getFriendlyName() + "' hat ein Upgrade Antrag gestellt!");
                player.sendMessage(ChatColor.GRAY + "Upgrade-Level: " + upgradeLevel.getName());
            }
            return false;
        }

        // check old request
        if(request.isAccepted()) {
            request.delete();
            return true;
        }
        else {
            return false;
        }
    }

    @Override
    public String getShortReason() {

        return "In Bearbeitung";
    }

    @Override
    public String getLongReason() {

        return "Ein Teammitglied wird sich in kürze darum kümmern!";
    }
}