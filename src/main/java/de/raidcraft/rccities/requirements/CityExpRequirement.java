package de.raidcraft.rccities.requirements;

import de.raidcraft.api.requirement.AbstractRequirement;
import de.raidcraft.api.requirement.RequirementInformation;
import de.raidcraft.api.requirement.RequirementResolver;
import de.raidcraft.rccities.api.city.City;
import org.bukkit.configuration.ConfigurationSection;

/**
 * @author Philip Urban
 */
@RequirementInformation("CITY_EXP")
public class CityExpRequirement<T> extends AbstractRequirement<T> {

    private int amount;

    public CityExpRequirement(RequirementResolver<T> resolver, ConfigurationSection config) {

        super(resolver, config);
    }

    @Override
    protected void load(ConfigurationSection data) {

        amount = data.getInt("exp");
    }

    @Override
    public boolean isMet(T object) {

        if (object == null || City.class != object.getClass()) return false;
        City city = (City)object;

        if(city.getExp() >= amount) {
            return true;
        }
        return false;
    }

    @Override
    public String getShortReason() {

        return "Zu wenig EXP";
    }

    @Override
    public String getLongReason() {

        return "Es müssen sich mindestens " + amount + " EXP in der Stadtkasse befinden!";
    }
}
