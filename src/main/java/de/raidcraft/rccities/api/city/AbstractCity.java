package de.raidcraft.rccities.api.city;

import de.raidcraft.RaidCraft;
import de.raidcraft.rccities.RCCitiesPlugin;
import org.bukkit.Location;

import java.sql.Timestamp;

/**
 * @author Philip Urban
 */
public abstract class AbstractCity implements City {

    protected int id;
    protected String name;
    protected String creator;
    protected Timestamp creationDate;
    protected Location spawn;
    protected String description;
    protected int plotCredit;
    protected int maxRadius;
    protected int exp;

    protected AbstractCity() {}

    protected AbstractCity(String name, Location spawn, String creator) {

        this.name = name;
        this.spawn = spawn;
        this.creator = creator;
        this.plotCredit = RaidCraft.getComponent(RCCitiesPlugin.class).getConfig().initialPlotCredit;
        this.maxRadius = RaidCraft.getComponent(RCCitiesPlugin.class).getConfig().defaultMaxRadius;

        save();
    }

    @Override
    public int getId() {

        return id;
    }

    @Override
    public final String getName() {

        return name;
    }

    @Override
    public String getFriendlyName() {

        return name.replace('_', ' ');
    }

    @Override
    public final String getCreator() {

        return creator;
    }

    @Override
    public final Timestamp getCreationDate() {

        return creationDate;
    }

    @Override
    public final Location getSpawn() {

        return spawn;
    }

    @Override
    public final void setSpawn(Location spawn) {

        this.spawn = spawn;
        save();
    }

    @Override
    public final String getDescription() {

        return description;
    }

    @Override
    public final void setDescription(String description) {

        this.description = description;
        save();
    }

    @Override
    public void setPlotCredit(int plotCredit) {

        this.plotCredit = plotCredit;
        save();
    }

    @Override
    public int getPlotCredit() {

        return plotCredit;
    }

    @Override
    public void removeExp(int exp) {

        this.exp -= exp;
        save();
    }

    @Override
    public void addExp(int exp) {

        this.exp += exp;
        save();
    }

    @Override
    public int getExp() {

        return exp;
    }

    @Override
    public void setMaxRadius(int maxRadius) {

        this.maxRadius = maxRadius;
        save();
    }

    @Override
    public int getMaxRadius() {

        return maxRadius;
    }

    @Override
    public boolean equals(Object o) {

        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        AbstractCity that = (AbstractCity) o;

        if (!name.equals(that.name)) return false;

        return true;
    }

    @Override
    public int hashCode() {

        return name.hashCode();
    }
}
