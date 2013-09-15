package de.raidcraft.rccities.manager;

import de.raidcraft.RaidCraft;
import de.raidcraft.api.RaidCraftException;
import de.raidcraft.rccities.RCCitiesPlugin;
import de.raidcraft.rccities.api.city.City;
import de.raidcraft.rccities.api.settings.CityFlag;
import de.raidcraft.rccities.api.settings.FlagInformation;
import de.raidcraft.rccities.api.settings.PlotFlag;
import de.raidcraft.rccities.tables.TFlag;
import de.raidcraft.util.CaseInsensitiveMap;
import de.raidcraft.util.StringUtils;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Map;

/**
 * @author Philip Urban
 */
public class FlagManager {

    private RCCitiesPlugin plugin;
    private Map<String, Class<? extends CityFlag>> cityFlags = new CaseInsensitiveMap<>();
    private Map<String, Class<? extends PlotFlag>> plotFlags = new CaseInsensitiveMap<>();

    public FlagManager(RCCitiesPlugin plugin) {

        this.plugin = plugin;
    }

    public void registerCityFlag(Class<? extends CityFlag> clazz) {

        String name = StringUtils.formatName(clazz.getAnnotation(FlagInformation.class).name());
        cityFlags.put(name, clazz);
    }

    public void registerPlotFlag(Class<? extends PlotFlag> clazz) {

        String name = StringUtils.formatName(clazz.getAnnotation(FlagInformation.class).name());
        plotFlags.put(name, clazz);
    }

    public void setCityFlag(City city, String flagName, String flagValue) throws RaidCraftException {

        if(!cityFlags.containsKey(flagName)) {
            String flagList = "";
            for(String name : cityFlags.keySet()) {
                if(!flagList.isEmpty()) flagList += ", ";
                flagList += name;
            }
            throw new RaidCraftException("Unbekannte Flag! Verfügbare Flags: " + flagList);
        }

        CityFlag flag;
        try {
            Class[] argTypes = {City.class};
            Constructor constructor = cityFlags.get(flagName).getDeclaredConstructor(argTypes);
            flag  = (CityFlag)constructor.newInstance(city);
        } catch (NoSuchMethodException | InstantiationException | IllegalAccessException | InvocationTargetException e) {

            RaidCraft.LOGGER.warning("RCCities Flag Error: " + e.getMessage());
            e.printStackTrace();
            throw new RaidCraftException("Interner Fehler aufgetreten: " + e.getMessage());
        }

        flag.setValue(flagValue);

        flagName = flagName.toLowerCase();
        TFlag tFlag = RaidCraft.getDatabase(RCCitiesPlugin.class)
                .find(TFlag.class).where().eq("city_id", city.getId()).eq("name", flagName).findUnique();
        if(tFlag != null) {
            tFlag.setValue(flagValue);
            RaidCraft.getDatabase(RCCitiesPlugin.class).update(tFlag);
        }
        else {
            tFlag = new TFlag();
            tFlag.setCity(city);
            tFlag.setName(flagName);
            tFlag.setValue(flagValue);
            RaidCraft.getDatabase(RCCitiesPlugin.class).save(tFlag);
        }
    }
}
