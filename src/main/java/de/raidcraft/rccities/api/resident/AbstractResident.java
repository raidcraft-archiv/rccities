package de.raidcraft.rccities.api.resident;

import de.raidcraft.rccities.api.city.City;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

/**
 * @author Philip Urban
 */
public abstract class AbstractResident implements Resident {

    private int id;
    private String name;
    private Role profession;
    private City city;

    protected AbstractResident() {};

    public AbstractResident(String name, Role profession, City city) {

        this.name = name;
        this.profession = profession;
        this.city = city;

        save();
    }

    protected void setId(int id) {

        this.id = id;
    }

    protected void setName(String name) {

        this.name = name;
    }

    protected void setProfession(String profession) {

        this.profession = Role.valueOf(profession);
    }

    protected void setCity(City city) {

        this.city = city;
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
