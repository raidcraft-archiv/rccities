package de.strasse36.rccities.database;

import com.silthus.raidcraft.database.Connection;
import com.silthus.raidcraft.database.Database;
import com.silthus.raidcraft.database.RCTable;
import de.strasse36.rccities.City;
import de.strasse36.rccities.util.TableNames;
import org.bukkit.Bukkit;
import org.bukkit.Location;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Author: Philip Urban
 * Date: 28.02.12 - 21:22
 * Description:
 */
public class CityTable extends RCTable {

    public CityTable(Database database) {
        super(database, TableNames.getCityTable());
    }

    @Override
    public void createTable() {
        Connection connection = getDatabase().getConnection();
        PreparedStatement prepare = connection.prepare(
                "CREATE TABLE  `" + getDatabase().getName() + "`.`" + getName() + "` (" +
                        "`id` INT NOT NULL ," +
                        "`name` VARCHAR( 50 ) NULL ," +
                        "`size` BIGINT NULL ," +
                        "`description` TEXT NULL ," +
                        "`spawn_world` VARCHAR( 50 ) NULL" +
                        "`spawn_x` DOUBLE NULL ," +
                        "`spawn_y` DOUBLE NULL ," +
                        "`spawn_z` DOUBLE NULL ," +
                        "`spawn_pitch` FLOAT NULL ," +
                        "`spawn_yaw` FLOAT NULL ," +
                        "PRIMARY KEY ( `id` )" +
                        ") ENGINE = InnoDB;");
        connection.executeUpdate(prepare);
    }
    
    public City getCity(int id)
    {
        Connection connection = getDatabase().getConnection();
        PreparedStatement statement = connection.prepare(
                "SELECT * FROM " + getName() + " WHERE id = '" + id + "';"
        );
        ResultSet resultSet = connection.execute(statement);
        try {
            City city = new City();
            while (resultSet.next()) {
                city = new City();
                city.setName(resultSet.getString("name"));
                city.setDescription(resultSet.getString("description"));
                city.setSize(resultSet.getLong("size"));
                Location spawn = new Location(
                        Bukkit.getWorld(resultSet.getString("spawn_world")),
                        resultSet.getDouble("spawn_x"),
                        resultSet.getDouble("spawn_y"),
                        resultSet.getDouble("spawn_z"),
                        resultSet.getFloat("spawn_yaw"),
                        resultSet.getFloat("spawn_pitch")
                );
                city.setSpawn(spawn);
            }
            return city;
        } catch (SQLException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
        return null;
    }

    public City getCity(String name)
    {
        Connection connection = getDatabase().getConnection();
        PreparedStatement statement = connection.prepare(
                "SELECT * FROM " + getName() + " WHERE name = '" + name + "';"
        );
        ResultSet resultSet = connection.execute(statement);
        try {
            City city = new City();
            while (resultSet.next()) {
                city = new City();
                city.setName(resultSet.getString("name"));
                city.setDescription(resultSet.getString("description"));
                city.setSize(resultSet.getLong("size"));
                Location spawn = new Location(
                        Bukkit.getWorld(resultSet.getString("spawn_world")),
                        resultSet.getDouble("spawn_x"),
                        resultSet.getDouble("spawn_y"),
                        resultSet.getDouble("spawn_z"),
                        resultSet.getFloat("spawn_yaw"),
                        resultSet.getFloat("spawn_pitch")
                );
                city.setSpawn(spawn);
            }
            return city;
        } catch (SQLException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
        return null;
    }

    public List<City> getCitys() {
        Connection connection = getDatabase().getConnection();
        PreparedStatement statement = connection.prepare(
                "SELECT * FROM " + getName() + ";"
        );
        ResultSet resultSet = connection.execute(statement);
        List<City> citylist = new ArrayList<City>();
        try {
            City city;
            while (resultSet.next()) {
                city = new City();
                city.setName(resultSet.getString("name"));
                city.setDescription(resultSet.getString("description"));
                city.setSize(resultSet.getLong("size"));
                Location spawn = new Location(
                        Bukkit.getWorld(resultSet.getString("spawn_world")),
                        resultSet.getDouble("spawn_x"),
                        resultSet.getDouble("spawn_y"),
                        resultSet.getDouble("spawn_z"),
                        resultSet.getFloat("spawn_yaw"),
                        resultSet.getFloat("spawn_pitch")
                        );
                city.setSpawn(spawn);
                citylist.add(city);
            }
            return citylist;
        } catch (SQLException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
        return null;
    }

    public void newCity(City city)
    {
        Connection connection = getDatabase().getConnection();
        PreparedStatement statement = connection.prepare(
                "INSERT INTO " + getName() + "(name, size, description, spawn_x, spawn_y, spawn_z, spawn_pitch, spawn_yaw) " +
                        "VALUES (" +
                        city.getName() + "," +
                        city.getSize() + "," +
                        city.getDescription() + "," +
                        city.getSpawn().getWorld() + "," +
                        city.getSpawn().getX() + "," +
                        city.getSpawn().getY() + "," +
                        city.getSpawn().getZ() + "," +
                        city.getSpawn().getPitch() + "," +
                        city.getSpawn().getYaw() + "," +
                        ");"
        );
        connection.execute(statement);
    }

    public void updateCity(City city)
    {
        Connection connection = getDatabase().getConnection();
        PreparedStatement statement = connection.prepare(
                "UPDATE " + getName() + " SET " +
                        "name = " + city.getName() + "," +
                        "size = " + city.getSize() + "," +
                        "description = " + city.getDescription() + "," +
                        "spawn_world = " + city.getSpawn().getWorld() + "," +
                        "spawn_x = " + city.getSpawn().getX() + "," +
                        "spawn_y = " + city.getSpawn().getY() + "," +
                        "spawn_z = " + city.getSpawn().getZ() + "," +
                        "spawn_pitch = " + city.getSpawn().getPitch() + "," +
                        "spawn_yaw = " + city.getSpawn().getYaw() + "," +
                        ");"
        );
        connection.execute(statement);
    }
}
