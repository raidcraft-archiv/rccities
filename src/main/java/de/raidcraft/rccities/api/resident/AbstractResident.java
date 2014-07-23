package de.raidcraft.rccities.api.resident;

import de.raidcraft.rccities.api.city.City;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

/**
 * @author Philip Urban
 */
public abstract class AbstractResident implements Resident {

    protected int id;
    protected String name;
    protected Role profession;
    protected City city;

    protected AbstractResident() {

    }

    ;

    public AbstractResident(String name, Role profession, City city) {

        this.name = name;
        this.profession = profession;
        this.city = city;

        save();
    }

    @Override
    public int getId() {

        return id;
    }

    @Override
    public String getName() {

        return name;
    }

    @Override
    public Role getRole() {

        return profession;
    }

    @Override
    public void setRole(Role role) {

        this.profession = role;
        save();
    }

    @Override
    public City getCity() {

        return city;
    }

    @Override
    public Player getPlayer() {

        return Bukkit.getPlayer(name);
    }

    @Override
    public boolean equals(Object o) {

        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        AbstractResident that = (AbstractResident) o;

        if (!city.equals(that.city)) return false;
        if (!name.equals(that.name)) return false;

        return true;
    }

    @Override
    public int hashCode() {

        int result = name.hashCode();
        result = 31 * result + city.hashCode();
        return result;
    }
}
