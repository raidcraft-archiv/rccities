package de.raidcraft.rccities.manager;

import de.raidcraft.RaidCraft;
import de.raidcraft.api.RaidCraftException;
import de.raidcraft.rccities.RCCitiesPlugin;
import de.raidcraft.rccities.api.city.City;
import de.raidcraft.rccities.api.flags.CityFlag;
import de.raidcraft.rccities.api.flags.Flag;
import de.raidcraft.rccities.api.flags.FlagInformation;
import de.raidcraft.rccities.api.flags.FlagRefreshType;
import de.raidcraft.rccities.api.flags.PlotFlag;
import de.raidcraft.rccities.api.plot.Plot;
import de.raidcraft.rccities.tables.TCityFlag;
import de.raidcraft.rccities.tables.TPlotFlag;
import de.raidcraft.util.CaseInsensitiveMap;
import de.raidcraft.util.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

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
    private Map<Integer, Map<String, PlotFlag>> cachedPlotFlags = new HashMap<>();

    private FlagRefreshTask refreshTask;

    public FlagManager(RCCitiesPlugin plugin) {

        this.plugin = plugin;

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

    public void setCityFlag(City city, Player player, String flagName, String flagValue) throws RaidCraftException {

        if (!registeredCityFlags.containsKey(flagName)) {
            String flagList = "";
            for (String name : registeredCityFlags.keySet()) {
                if (!flagList.isEmpty()) flagList += ", ";
                flagList += name;
            }
            throw new RaidCraftException("Unbekannte Flag! Verfügbare Flags: " + flagList);
        }

        // delete flag if value is null
        if (flagValue == null) {
            TCityFlag tCityFlag = RaidCraft.getDatabase(RCCitiesPlugin.class).find(TCityFlag.class).where().eq("city_id", city.getId()).ieq("name", flagName).findUnique();
            if (tCityFlag != null) {
                RaidCraft.getDatabase(RCCitiesPlugin.class).delete(tCityFlag);
            }
            if (cachedCityFlags.containsKey(city.getName())) {
                cachedCityFlags.get(city.getName()).remove(flagName);
            }
            return;
        }

        CityFlag flag;
        // load cached flag
        if (cachedCityFlags.containsKey(city.getName()) && cachedCityFlags.get(city.getName()).containsKey(flagName)) {
            flag = cachedCityFlags.get(city.getName()).get(flagName);
        }
        // create new
        else {
            flag = loadCityFlag(registeredCityFlags.get(flagName), city);
        }

        FlagInformation annotation = flag.getClass().getAnnotation(FlagInformation.class);

        // check if admin only
        if (annotation.adminOnly() && player != null && !player.hasPermission("rccities.flag.all")) {
            throw new RaidCraftException("Diese Flag kann nur von Administratoren geändert werden!");
        }

        long cooldown = (flag.getLastChange() / 1000) + annotation.cooldown() - (System.currentTimeMillis() / 1000);
        if (cooldown > 0) {
            throw new RaidCraftException("Diese Flag kann erst wieder in " + cooldown + " Sekunden geändert werden!");
        }

        flag.setValue(flagValue);
        flag.refresh();

        // save in cache
        if (!cachedCityFlags.containsKey(city.getName())) {
            cachedCityFlags.put(city.getName(), new CaseInsensitiveMap<CityFlag>());
        }
        cachedCityFlags.get(city.getName()).put(flag.getName(), flag);

        flagName = flagName.toLowerCase();
        TCityFlag tFlag = RaidCraft.getDatabase(RCCitiesPlugin.class)
                .find(TCityFlag.class).where().eq("city_id", city.getId()).eq("name", flagName).findUnique();
        if (tFlag != null) {
            tFlag.setValue(flagValue);
            RaidCraft.getDatabase(RCCitiesPlugin.class).update(tFlag);
        } else {
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

        if (!cachedCityFlags.containsKey(city.getName())) return;
        cachedCityFlags.get(city.getName()).remove(flagName);
    }

    public void setPlotFlag(Plot plot, Player player, String flagName, String flagValue) throws RaidCraftException {

        if (!registeredPlotFlags.containsKey(flagName)) {
            String flagList = "";
            for (String name : registeredPlotFlags.keySet()) {
                if (!flagList.isEmpty()) flagList += ", ";
                flagList += name;
            }
            throw new RaidCraftException("Unbekannte Flag! Verfügbare Flags: " + flagList);
        }

        // delete flag if value is null
        if (flagValue == null) {
            TPlotFlag tPlotFlag = RaidCraft.getDatabase(RCCitiesPlugin.class).find(TPlotFlag.class).where().eq("plot_id", plot.getId()).ieq("name", flagName).findUnique();
            if (tPlotFlag != null) {
                RaidCraft.getDatabase(RCCitiesPlugin.class).delete(tPlotFlag);
            }
            if (cachedCityFlags.containsKey(plot.getId())) {
                cachedCityFlags.get(plot.getId()).remove(flagName);
            }
            return;
        }

        PlotFlag flag;
        // load cached flag
        if (cachedPlotFlags.containsKey(plot.getId()) && cachedPlotFlags.get(plot.getId()).containsKey(flagName)) {
            flag = cachedPlotFlags.get(plot.getId()).get(flagName);
        }
        // create new
        else {
            flag = loadPlotFlag(registeredPlotFlags.get(flagName), plot);
        }

        FlagInformation annotation = flag.getClass().getAnnotation(FlagInformation.class);

        // check if admin only
        if (annotation.adminOnly() && player != null && !player.hasPermission("rccities.flag.all")) {
            throw new RaidCraftException("Diese Flag kann nur von Administratoren geändert werden!");
        }

        long cooldown = (flag.getLastChange() / 1000) + annotation.cooldown() - (System.currentTimeMillis() / 1000);
        if (cooldown > 0) {
            throw new RaidCraftException("Diese Flag kann erst wieder in " + cooldown + " Sekunden geändert werden!");
        }

        flag.setValue(flagValue);
        flag.refresh();

        // save in cache
        if (!cachedPlotFlags.containsKey(plot.getId())) {
            cachedPlotFlags.put(plot.getId(), new CaseInsensitiveMap<PlotFlag>());
        }
        cachedPlotFlags.get(plot.getId()).put(flag.getName(), flag);

        flagName = flagName.toLowerCase();
        TPlotFlag tFlag = RaidCraft.getDatabase(RCCitiesPlugin.class)
                .find(TPlotFlag.class).where().eq("plot_id", plot.getId()).eq("name", flagName).findUnique();
        if (tFlag != null) {
            tFlag.setValue(flagValue);
            RaidCraft.getDatabase(RCCitiesPlugin.class).update(tFlag);
        } else {
            tFlag = new TPlotFlag();
            tFlag.setPlot(plot);
            tFlag.setName(flagName);
            tFlag.setValue(flagValue);
            RaidCraft.getDatabase(RCCitiesPlugin.class).save(tFlag);
        }
    }

    public void setCityFlag(City city, Player player, Class<? extends CityFlag> clazz, Object value) throws RaidCraftException {

        FlagInformation annotation = clazz.getAnnotation(FlagInformation.class);
        setCityFlag(city, player, annotation.name(), value.toString());
    }

    public void setPlotFlag(Plot plot, Player player, Class<? extends PlotFlag> clazz, Object value) throws RaidCraftException {

        FlagInformation annotation = clazz.getAnnotation(FlagInformation.class);
        setPlotFlag(plot, player, annotation.name(), value.toString());
    }

    public void removePlotFlag(Plot plot, String flagName) {

        TPlotFlag flag = RaidCraft.getDatabase(RCCitiesPlugin.class)
                .find(TPlotFlag.class).where().eq("plot_id", plot.getId()).ieq("name", flagName).findUnique();
        RaidCraft.getDatabase(RCCitiesPlugin.class).delete(flag);

        if (!cachedPlotFlags.containsKey(plot.getId())) return;
        cachedPlotFlags.get(plot.getId()).remove(flagName);
    }

    public void refreshCityFlags(City city) {

        if (!cachedCityFlags.containsKey(city.getName())) return;
        for (Flag flag : cachedCityFlags.get(city.getName()).values()) {
            try {
                FlagInformation annotation = flag.getClass().getAnnotation(FlagInformation.class);
                if (annotation.needsRefresh()) {
                    flag.refresh();
                }
            } catch (RaidCraftException e) {
                RaidCraft.LOGGER.warning("Fehler beim aktualisieren einer Flag! " + e.getMessage());
            }
        }
    }

    public void refreshPlotFlags(Plot plot) {

        if (!cachedPlotFlags.containsKey(plot.getId())) return;
        for (Flag flag : cachedPlotFlags.get(plot.getId()).values()) {
            try {
                flag.refresh();
            } catch (RaidCraftException e) {
                RaidCraft.LOGGER.warning("Fehler beim aktualisieren einer Flag! " + e.getMessage());
            }
        }
    }

    public void loadExistingFlags() {

        clearCache();

        List<TCityFlag> tCityFlags = RaidCraft.getDatabase(RCCitiesPlugin.class).find(TCityFlag.class).findList();
        for (TCityFlag tCityFlag : tCityFlags) {

            Class<? extends CityFlag> clazz = registeredCityFlags.get(tCityFlag.getName());
            if (clazz == null) continue;
            FlagInformation annotation = clazz.getAnnotation(FlagInformation.class);
            City city = plugin.getCityManager().getCity(tCityFlag.getCity().getName());
            if (city == null) continue;
            try {
                CityFlag flag = loadCityFlag(clazz, city);
                flag.setValue(tCityFlag.getValue());
                if (annotation.refreshType() == FlagRefreshType.ON_START) {
                    flag.refresh();
                }
                if (annotation.refreshType() == FlagRefreshType.PERIODICALLY) {
                    refreshTask.addFlagInformation(annotation, flag);
                }

            } catch (RaidCraftException e) {
            }
        }

        List<TPlotFlag> tPlotFlags = RaidCraft.getDatabase(RCCitiesPlugin.class).find(TPlotFlag.class).findList();
        for (TPlotFlag tPlotFlag : tPlotFlags) {

            Class<? extends PlotFlag> clazz = registeredPlotFlags.get(tPlotFlag.getName());
            if (clazz == null) continue;
            FlagInformation annotation = clazz.getAnnotation(FlagInformation.class);
            Plot plot = plugin.getPlotManager().getPlot(tPlotFlag.getPlot().getId());
            if (plot == null) continue;
            try {
                PlotFlag flag = loadPlotFlag(clazz, plot);
                flag.setValue(tPlotFlag.getValue());
                if (annotation.refreshType() == FlagRefreshType.ON_START) {
                    flag.refresh();
                }
                if (annotation.refreshType() == FlagRefreshType.PERIODICALLY) {
                    refreshTask.addFlagInformation(annotation, flag);
                }

            } catch (RaidCraftException e) {
            }
        }
    }

    private CityFlag loadCityFlag(Class<? extends CityFlag> clazz, City city) throws RaidCraftException {

        CityFlag flag;
        try {
            Class[] argTypes = {City.class};
            Constructor constructor = clazz.getDeclaredConstructor(argTypes);
            flag = (CityFlag) constructor.newInstance(city);
        } catch (NoSuchMethodException | InstantiationException | IllegalAccessException | InvocationTargetException e) {

            RaidCraft.LOGGER.warning("RCCities Flag Error: " + e.getMessage());
            e.printStackTrace();
            throw new RaidCraftException("Interner Fehler aufgetreten: " + e.getMessage());
        }
        if (!cachedCityFlags.containsKey(city.getName())) {
            cachedCityFlags.put(city.getName(), new CaseInsensitiveMap<CityFlag>());
        }
        cachedCityFlags.get(city.getName()).put(flag.getName(), flag);
        return flag;
    }

    private PlotFlag loadPlotFlag(Class<? extends PlotFlag> clazz, Plot plot) throws RaidCraftException {

        PlotFlag flag;
        try {
            Class[] argTypes = {Plot.class};
            Constructor constructor = clazz.getDeclaredConstructor(argTypes);
            flag = (PlotFlag) constructor.newInstance(plot);
        } catch (NoSuchMethodException | InstantiationException | IllegalAccessException | InvocationTargetException e) {

            RaidCraft.LOGGER.warning("RCCities Flag Error: " + e.getMessage());
            e.printStackTrace();
            throw new RaidCraftException("Interner Fehler aufgetreten: " + e.getMessage());
        }
        if (!cachedPlotFlags.containsKey(plot.getId())) {
            cachedPlotFlags.put(plot.getId(), new CaseInsensitiveMap<PlotFlag>());
        }
        cachedPlotFlags.get(plot.getId()).put(flag.getName(), flag);
        return flag;
    }

    public CityFlag getCityFlag(City city, String flagName) {

        if (!cachedCityFlags.containsKey(city.getName())) return null;
        return cachedCityFlags.get(city.getName()).get(flagName);
    }

    public PlotFlag getPlotFlag(Plot plot, String flagName) {

        if (!cachedPlotFlags.containsKey(plot.getId())) return null;
        return cachedPlotFlags.get(plot.getId()).get(flagName);
    }

    public CityFlag getCityFlag(City city, Class<? extends CityFlag> clazz) {

        FlagInformation annotation = clazz.getAnnotation(FlagInformation.class);
        return getCityFlag(city, annotation.name());
    }

    public PlotFlag getPlotFlag(Plot plot, Class<? extends PlotFlag> clazz) {

        FlagInformation annotation = clazz.getAnnotation(FlagInformation.class);
        return getPlotFlag(plot, annotation.name());
    }

    public List<FlagInformation> getRegisteredCityFlagInformationList() {

        List<FlagInformation> flagInformationList = new ArrayList<>();
        for (Class<? extends Flag> registeredFlagClass : registeredCityFlags.values()) {
            FlagInformation annotation = registeredFlagClass.getAnnotation(FlagInformation.class);
            flagInformationList.add(annotation);
        }
        return flagInformationList;
    }

    public List<FlagInformation> getRegisteredPlotFlagInformationList() {

        List<FlagInformation> flagInformationList = new ArrayList<>();
        for (Class<? extends Flag> registeredFlagClass : registeredPlotFlags.values()) {
            FlagInformation annotation = registeredFlagClass.getAnnotation(FlagInformation.class);
            flagInformationList.add(annotation);
        }
        return flagInformationList;
    }

    public FlagInformation getRegisteredCityFlagInformation(String flagName) {

        Class<? extends Flag> registeredFlagClass = registeredCityFlags.get(flagName);
        if (registeredFlagClass == null) return null;
        FlagInformation annotation = registeredFlagClass.getAnnotation(FlagInformation.class);
        return annotation;
    }

    public void clearCache() {

        cachedCityFlags.clear();
        cachedPlotFlags.clear();
    }

    public class FlagRefreshTask implements Runnable {

        private List<FlagRefreshInformation> refreshInformation = new ArrayList<>();

        @Override
        public void run() {

            for (FlagRefreshInformation information : refreshInformation) {
                information.increaseLastRefresh();
                if (information.getLastRefresh() > information.getAnnotation().refreshInterval()) {
                    information.resetLastRefresh();
                    try {
                        information.getFlag().refresh();
                    } catch (RaidCraftException e) {
                        RaidCraft.LOGGER.warning("Fehler beim aktualisieren einer Flag! " + e.getMessage());
                    }
                }
            }
        }

        public void addFlagInformation(FlagInformation annotation, Flag flag) {

            refreshInformation.add(new FlagRefreshInformation(annotation, flag));
        }

        public void removeFlagInformation(Flag flag) {

            for (FlagRefreshInformation information : refreshInformation) {
                if (information.getFlag() == flag) {
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
