package de.raidcraft.rccities.requirements;

import de.raidcraft.api.requirement.AbstractRequirement;
import de.raidcraft.api.requirement.RequirementInformation;
import de.raidcraft.api.requirement.RequirementResolver;
import de.raidcraft.rccities.api.city.City;
import de.raidcraft.rcupgrades.api.level.UpgradeLevel;
import de.raidcraft.rcupgrades.api.upgrade.Upgrade;
import org.bukkit.configuration.ConfigurationSection;

/**
 * @author Philip Urban
 */
@RequirementInformation("UPGRADE_LEVEL")
public class UpgradeLevelRequirement extends AbstractRequirement<City> {

    private String upgradeType;
    private String upgradeLevelName;

    public UpgradeLevelRequirement(RequirementResolver<City> resolver, ConfigurationSection config) {

        super(resolver, config);
    }

    @Override
    protected void load(ConfigurationSection data) {

        upgradeType = data.getString("upgrade-type");
        upgradeLevelName = data.getString("upgrade-level");
    }

    @Override
    public boolean isMet(City city) {

        Upgrade upgrade = city.getUpgrades().getUpgrade(upgradeType);
        if(upgrade == null) return false;
        UpgradeLevel upgradeLevel = upgrade.getLevel(upgradeLevelName);
        if(upgradeLevel == null) return false;

        if(upgradeLevel.isUnlocked()) {
            return true;
        }
        return false;
    }

    @Override
    public String getShortReason() {

        return "Anderes Upgrade nicht freigeschaltet!";
    }

    @Override
    public String getLongReason() {

        return "Es muss das Update-Level '" + upgradeLevelName + "' freigeschaltet sein!";
    }

    @Override
    public String getDescription() {

        return "Level '" + upgradeLevelName + "' freigeschaltet";
    }
}