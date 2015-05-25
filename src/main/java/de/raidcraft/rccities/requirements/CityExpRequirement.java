package de.raidcraft.rccities.requirements;

import de.raidcraft.api.action.requirement.ReasonableRequirement;
import de.raidcraft.rccities.api.city.City;
import org.bukkit.configuration.ConfigurationSection;

import java.util.Optional;

/**
 * @author Silthus
 */
public class CityExpRequirement implements ReasonableRequirement<City> {

    @Override
    @Information(
            value = "city.exp",
            desc = "Checks the exp of the city.",
            conf = {"exp: <min exp>"}
    )
    public boolean test(City city, ConfigurationSection config) {

        return city.getExp() >= config.getInt("exp");
    }

    @Override
    public Optional<String> getDescription(City entity, ConfigurationSection config) {

        return Optional.of("Es müssen sich mindestens " + config.getInt("exp") + " EXP in der Stadtkasse befinden!");
    }

    @Override
    public String getReason(City entity, ConfigurationSection config) {

        return config.getInt("exp") + " EXP";
    }
}
