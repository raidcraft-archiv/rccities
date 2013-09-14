package de.raidcraft.rccities.manager;

import de.raidcraft.rccities.RCCitiesPlugin;
import de.raidcraft.rccities.api.settings.CitySetting;
import de.raidcraft.rccities.api.settings.PlotSetting;
import de.raidcraft.rccities.api.settings.SettingInformation;
import de.raidcraft.util.CaseInsensitiveMap;
import de.raidcraft.util.StringUtils;

import java.util.Map;

/**
 * @author Philip Urban
 */
public class SettingManager {

    private RCCitiesPlugin plugin;
    private Map<String, Class<? extends CitySetting>> citySettings = new CaseInsensitiveMap<>();
    private Map<String, Class<? extends PlotSetting>> plotSettings = new CaseInsensitiveMap<>();

    public SettingManager(RCCitiesPlugin plugin) {

        this.plugin = plugin;
    }

    public void registerCitySetting(Class<? extends CitySetting> clazz) {

        String name = StringUtils.formatName(clazz.getAnnotation(SettingInformation.class).name());
        citySettings.put(name, clazz);
    }

    public void registerPlotSetting(Class<? extends PlotSetting> clazz) {

        String name = StringUtils.formatName(clazz.getAnnotation(SettingInformation.class).name());
        plotSettings.put(name, clazz);
    }
}
