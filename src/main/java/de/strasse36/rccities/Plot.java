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
    private boolean open;
    private boolean pvp;
    private boolean mobs;

    public Plot() {
    }

    public Plot(City city, String regionId, int x, int z) {
        this.city = city;
        this.regionId = regionId;
        this.x = x;
        this.z = z;
        pvp = false;
        mobs = false;
        open = false;
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

    public boolean isOpen() {
        return open;
    }

    public void setOpen(boolean open) {
        this.open = open;
    }
    
    public int getOpen()
    {
        if(isOpen())
            return 1;
        else
            return 0;
    }

    public boolean isPvp() {
        return pvp;
    }

    public void setPvp(boolean pvp) {
        this.pvp = pvp;
    }

    public int getPvp()
    {
        if(isPvp())
            return 1;
        else
            return 0;
    }

    public boolean isMobs() {
        return mobs;
    }

    public void setMobs(boolean mobs) {
        this.mobs = mobs;
    }
}
