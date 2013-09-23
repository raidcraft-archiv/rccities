package de.raidcraft.rccities.manager;

import de.raidcraft.RaidCraft;
import de.raidcraft.api.RaidCraftException;
import de.raidcraft.rccities.RCCitiesPlugin;
import de.raidcraft.rccities.api.city.City;
import de.raidcraft.rccities.api.flags.*;
import de.raidcraft.rccities.api.plot.Plot;
import de.raidcraft.rccities.tables.TCityFlag;
import de.raidcraft.rccities.tables.TPlotFlag;
import de.raidcraft.util.CaseInsensitiveMap;
import de.raidcraft.util.StringUtils;
import org.bukkit.Bukkit;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Philip Urban
 */
public class FlagManager {

    private RCCitiesPlugin plugin;
    private Map<String, Class<? extends CityFlag>> registeredCityFlags = new CaseInsensitiveMap<>();
    private Map<String, Class<? extends PlotFlag>> registeredPlotFlags = new CaseInsensitiveMap<>();

    // city name -> map(flag name, flag)
    private Map<String, Map<String, CityFlag>> cachedCityFlags = new CaseInsensitiveMap<>();
    // plot id -> map(flag name, flag)
    private Map<Integer,  Map<String, PlotFlag>> cachedPlotFlags = new HashMap<>();

    private FlagRefreshTask refreshTask;

    public FlagManager(RCCitiesPlugin plugin) {

        this.plugin = plugin;
        loadExistingFlags();

        refreshTask = new FlagRefreshTask();
        Bukkit.getScheduler().runTaskTimer(plugin, refreshTask, 0, 60 * 20);
    }

    public void registerCityFlag(Class<? extends CityFlag> clazz) {

        String name = StringUtils.formatName(clazz.getAnnotation(FlagInformation.class).name());
        registeredCityFlags.put(name, clazz);
    }

    public void registerPlotFlag(Class<? extends PlotFlag> clazz) {

        String name = StringUtils.formatName(clazz.getAnnotation(FlagInformation.class).name());
        registeredPlotFlags.put(name, clazz);
    }

    public void setCityFlag(City city, String flagName, String flagValue) throws RaidCraftException {

        if(!registeredCityFlags.containsKey(flagName)) {
            String flagList = "";
            for(String name : registeredCityFlags.keySet()) {
                if(!flagList.isEmpty()) flagList += ", ";
                flagList += name;
            }
            throw new RaidCraftException("Unbekannte Flag! Verfügbare Flags: " + flagList);
        }

        CityFlag flag;
        // load cached flag
        if(cachedCityFlags.containsKey(city.getName()) && cachedCityFlags.get(city.getName()).containsKey(flagName)) {
            flag = cachedCityFlags.get(city.getName()).get(flagName);
        }
        // create new
        else {
            flag = loadCityFlag(registeredCityFlags.get(flagName), city);
        }
        flag.setValue(flagValue);
        // save in cache
        if(!cachedCityFlags.containsKey(city.getName())) {
            cachedCityFlags.put(city.getName(), new CaseInsensitiveMap<CityFlag>());
        }
        cachedCityFlags.get(city.getName()).put(flag.getName(), flag);

        flagName = flagName.toLowerCase();
        TCityFlag tFlag = RaidCraft.getDatabase(RCCitiesPlugin.class)
                .find(TCityFlag.class).where().eq("city_id", city.getId()).eq("name", flagName).findUnique();
        if(tFlag != null) {
            tFlag.setValue(flagValue);
            RaidCraft.getDatabase(RCCitiesPlugin.class).update(tFlag);
        }
        else {
            tFlag = new TCityFlag();
            tFlag.setCity(city);
            tFlag.setName(flagName);
            tFlag.setValue(flagValue);
            RaidCraft.getDatabase(RCCitiesPlugin.class).save(tFlag);
        }
    }

    public void removeCityFlag(City city, String flagName) {

        TCityFlag flag = RaidCraft.getDatabase(RCCitiesPlugin.class)
                .find(TCityFlag.class).where().eq("city_id", city.getId()).ieq("name", flagName).findUnique();
        RaidCraft.getDatabase(RCCitiesPlugin.class).delete(flag);

        if(!cachedCityFlags.containsKey(city.getName())) return;
        cachedCityFlags.get(city.getName()).remove(flagName);
    }

    public void setPlotFlag(Plot plot, String flagName, String flagValue) throws RaidCraftException {

        if(!registeredPlotFlags.containsKey(flagName)) {
            String flagList = "";
            for(String name : registeredPlotFlags.keySet()) {
                if(!flagList.isEmpty()) flagList += ", ";
                flagList += name;
            }
            throw new RaidCraftException("Unbekannte Flag! Verfügbare Flags: " + flagList);
        }

        PlotFlag flag;
        // load cached flag
        if(cachedPlotFlags.containsKey(plot.getId()) && cachedPlotFlags.get(plot.getId()).containsKey(flagName)) {
            flag = cachedPlotFlags.get(plot.getId()).get(flagName);
        }
        // create new
        else {
            flag = loadPlotFlag(registeredPlotFlags.get(flagName), plot);
        }
        flag.setValue(flagValue);
        // save in cache
        if(!cachedPlotFlags.containsKey(plot.getId())) {
            cachedPlotFlags.put(plot.getId(), new CaseInsensitiveMap<PlotFlag>());
        }
        cachedPlotFlags.get(plot.getId()).put(flag.getName(), flag);

        flagName = flagName.toLowerCase();
        TPlotFlag tFlag = RaidCraft.getDatabase(RCCitiesPlugin.class)
                .find(TPlotFlag.class).where().eq("plot_id", plot.getId()).eq("name", flagName).findUnique();
        if(tFlag != null) {
            tFlag.setValue(flagValue);
            RaidCraft.getDatabase(RCCitiesPlugin.class).update(tFlag);
        }
        else {
            tFlag = new TPlotFlag();
            tFlag.setPlot(plot);
            tFlag.setName(flagName);
            tFlag.setValue(flagValue);
            RaidCraft.getDatabase(RCCitiesPlugin.class).save(tFlag);
        }
    }

    public void removePlotFlag(Plot plot, String flagName) {

        TPlotFlag flag = RaidCraft.getDatabase(RCCitiesPlugin.class)
                .find(TPlotFlag.class).where().eq("plot_id", plot.getId()).ieq("name", flagName).findUnique();
        RaidCraft.getDatabase(RCCitiesPlugin.class).delete(flag);

        if(!cachedPlotFlags.containsKey(plot.getId())) return;
        cachedPlotFlags.get(plot.getId()).remove(flagName);
    }

    public void refreshCityFlags(City city) {

        if(!cachedCityFlags.containsKey(city.getName())) return;
        for(Flag flag : cachedCityFlags.get(city.getName()).values()) {
            flag.refresh();
        }
    }

    public void refreshPlotFlags(Plot plot) {

        if(!cachedPlotFlags.containsKey(plot.getId())) return;
        for(Flag flag : cachedPlotFlags.get(plot.getId()).values()) {
            flag.refresh();
        }
    }

    private void loadExistingFlags() {

        clearCache();

        List<TCityFlag> tCityFlags = RaidCraft.getDatabase(RCCitiesPlugin.class).find(TCityFlag.class).findList();
        for(TCityFlag tCityFlag : tCityFlags) {

            Class<? extends CityFlag> clazz = registeredCityFlags.get(tCityFlag.getName());
            if(clazz == null) continue;
            FlagInformation annotation = clazz.getAnnotation(FlagInformation.class);
            City city = plugin.getCityManager().getCity(tCityFlag.getName());
            if(city == null) continue;
            try {
                CityFlag flag = loadCityFlag(clazz, city);
                flag.setValue(tCityFlag.getValue());
                if(annotation.refreshType() == FlagRefreshType.ON_START) {
                    flag.refresh();
                }
                if(annotation.refreshType() == FlagRefreshType.PERIODICALLY) {
                    refreshTask.addFlagInformation(annotation, flag);
                }

            } catch (RaidCraftException e) {}
        }

        List<TPlotFlag> tPlotFlags = RaidCraft.getDatabase(RCCitiesPlugin.class).find(TPlotFlag.class).findList();
        for(TPlotFlag tPlotFlag : tPlotFlags) {

            Class<? extends PlotFlag> clazz = registeredPlotFlags.get(tPlotFlag.getName());
            if(clazz == null) continue;
            Plot plot = plugin.getPlotManager().getPlot(tPlotFlag.getPlot().getId());
            if(plot == null) continue;
            try {
                PlotFlag flag = loadPlotFlag(clazz, plot);
                flag.setValue(tPlotFlag.getValue());
            } catch (RaidCraftException e) {}
        }
    }

    private CityFlag loadCityFlag(Class<? extends CityFlag> clazz, City city) throws RaidCraftException {

        CityFlag flag;
        try {
            Class[] argTypes = {City.class};
            Constructor constructor = clazz.getDeclaredConstructor(argTypes);
            flag  = (CityFlag)constructor.newInstance(city);
        } catch (NoSuchMethodException | InstantiationException | IllegalAccessException | InvocationTargetException e) {

            RaidCraft.LOGGER.warning("RCCities Flag Error: " + e.getMessage());
            e.printStackTrace();
            throw new RaidCraftException("Interner Fehler aufgetreten: " + e.getMessage());
        }
        if(!cachedCityFlags.containsKey(city.getName())) {
            cachedCityFlags.put(city.getName(), new CaseInsensitiveMap<CityFlag>());
        }
        cachedCityFlags.get(city.getName()).put(flag.getName(), flag);
        return flag;
    }

    private PlotFlag loadPlotFlag(Class<? extends PlotFlag> clazz, Plot plot) throws RaidCraftException {

        PlotFlag flag;
        try {
            Class[] argTypes = {City.class};
            Constructor constructor = clazz.getDeclaredConstructor(argTypes);
            flag  = (PlotFlag)constructor.newInstance(plot);
        } catch (NoSuchMethodException | InstantiationException | IllegalAccessException | InvocationTargetException e) {

            RaidCraft.LOGGER.warning("RCCities Flag Error: " + e.getMessage());
            e.printStackTrace();
            throw new RaidCraftException("Interner Fehler aufgetreten: " + e.getMessage());
        }
        if(!cachedPlotFlags.containsKey(plot.getId())) {
            cachedPlotFlags.put(plot.getId(), new CaseInsensitiveMap<PlotFlag>());
        }
        cachedPlotFlags.get(plot.getCity().getName()).put(flag.getName(), flag);
        return flag;
    }

    public void clearCache() {

        cachedCityFlags.clear();
        cachedPlotFlags.clear();
    }

    public class FlagRefreshTask implements Runnable {

        private List<FlagRefreshInformation> refreshInformation = new ArrayList<>();

        @Override
        public void run() {

            for(FlagRefreshInformation information : refreshInformation) {
                information.increaseLastRefresh();
                if(information.getLastRefresh() > information.getAnnotation().refreshInterval()) {
                    information.resetLastRefresh();
                    information.getFlag().refresh();
                }
            }
        }

        public void addFlagInformation(FlagInformation annotation, Flag flag) {

            refreshInformation.add(new FlagRefreshInformation(annotation, flag));
        }

        public void removeFlagInformation(Flag flag) {

            for(FlagRefreshInformation information : refreshInformation) {
                if(information.getFlag() == flag) {
                    refreshInformation.remove(information);
                    return;
                }
            }
        }

        public class FlagRefreshInformation {

            private FlagInformation annotation;
            private Flag flag;
            private int lastRefresh = 0;

            public FlagRefreshInformation(FlagInformation annotation, Flag flag) {

                this.annotation = annotation;
                this.flag = flag;
            }

            public FlagInformation getAnnotation() {

                return annotation;
            }

            public Flag getFlag() {

                return flag;
            }

            public int getLastRefresh() {

                return lastRefresh;
            }

            public void increaseLastRefresh() {

                this.lastRefresh++;
            }

            public void resetLastRefresh() {

                this.lastRefresh = 0;
            }
        }
    }
}
