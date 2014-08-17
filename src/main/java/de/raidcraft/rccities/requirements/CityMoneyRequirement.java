package de.raidcraft.rccities.requirements;

import de.raidcraft.RaidCraft;
import de.raidcraft.api.economy.AccountType;
import de.raidcraft.api.requirement.AbstractRequirement;
import de.raidcraft.api.requirement.RequirementInformation;
import de.raidcraft.api.requirement.RequirementResolver;
import de.raidcraft.rccities.api.city.City;
import org.bukkit.configuration.ConfigurationSection;

/**
 * @author Philip Urban
 */
@RequirementInformation("CITY_MONEY")
public class CityMoneyRequirement extends AbstractRequirement<City> {

    private int amount;

    public CityMoneyRequirement(RequirementResolver<City> resolver, ConfigurationSection config) {

        super(resolver, config);
    }

    @Override
    protected void load(ConfigurationSection data) {

        amount = data.getInt("money");
    }

    @Override
    public boolean isMet(City city) {

        if (RaidCraft.getEconomy().hasEnough(AccountType.CITY, city.getBankAccountName(), amount)) {
            return true;
        }
        return false;
    }

    @Override
    public String getShortReason() {

        return "Zu wenig Geld";
    }

    @Override
    public String getLongReason() {

        return "Es ist zu wenig Geld in der Stadtkasse. Benötigt werden " + RaidCraft.getEconomy().getFormattedAmount(amount) + "!";
    }

    @Override
    public String getDescription() {

        return RaidCraft.getEconomy().getFormattedAmount(amount);
    }
}