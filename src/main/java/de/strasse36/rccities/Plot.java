package de.strasse36.rccities;

import de.strasse36.rccities.util.TableHandler;

/**
 * Author: Philip Urban
 * Date: 06.03.12 - 19:36
 * Description:
 */
public class Plot {
    private int id;
    private City city;
    private String regionId;
    private int x;
    private int z;

    public Plot() {
    }

    public Plot(City city, String regionId, int x, int z) {
        this.city = city;
        this.regionId = regionId;
        this.x = x;
        this.z = z;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public City getCity() {
        return city;
    }

    public void setCity(City city) {
        this.city = city;
    }

    public void setCity(int id) {
        this.city = TableHandler.get().getCityTable().getCity(id);
    }

    public String getRegionId() {
        return regionId;
    }

    public void setRegionId(String regionId) {
        this.regionId = regionId;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getZ() {
        return z;
    }

    public void setZ(int z) {
        this.z = z;
    }
}
