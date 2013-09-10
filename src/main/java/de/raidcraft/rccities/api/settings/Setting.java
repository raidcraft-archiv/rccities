package de.raidcraft.rccities.api.settings;

/**
 * @author Philip Urban
 */
public class Setting {

    private String key;
    private String value;

    public Setting(String key, String value) {

        this.key = key;
        this.value = value;
    }

    public String getKey() {

        return key;
    }

    public String getValue() {

        return value;
    }
}
