package de.raidcraft.rccities.api.city;

import org.bukkit.Location;

import java.sql.Timestamp;

/**
 * @author Philip Urban
 */
public abstract class AbstractCity implements City {

    private int id;
    private String name;
    private String creator;
    private Timestamp creationDate;
    private Location spawn;
    private String description;
    private int plotCredit;

    protected AbstractCity() {}

    protected AbstractCity(String name, Location spawn, String creator) {

        this.name = name;
        this.spawn = spawn;
        this.creator = creator;

        save();
    }

    protected void setId(int id) {

        this.id = id;
    }

    protected void setName(String name) {

        this.name = name;
    }

    protected void setCreator(String creator) {

        this.creator = creator;
    }

    protected void setCreationDate(Timestamp creationDate) {

        this.creationDate = creationDate;
    }

    protected void setInitialPlotCredit(int plotCredit) {

        this.plotCredit = plotCredit;
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
