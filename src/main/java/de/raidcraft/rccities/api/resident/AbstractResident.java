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

    protected AbstractResident() {};

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
}
