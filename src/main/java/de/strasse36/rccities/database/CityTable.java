package de.strasse36.rccities.database;

import com.silthus.raidcraft.database.Connection;
import com.silthus.raidcraft.database.Database;
import com.silthus.raidcraft.database.RCTable;
import de.strasse36.rccities.City;
import de.strasse36.rccities.exceptions.AlreadyExistsException;
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
                        "`id` INT NOT NULL AUTO_INCREMENT PRIMARY KEY ," +
                        "`name` VARCHAR( 50 ) NULL ," +
                        "`size` BIGINT NULL ," +
                        "`description` TEXT NULL ," +
                        "`spawn_world` VARCHAR( 50 ) NULL ," +
                        "`spawn_x` DOUBLE NULL ," +
                        "`spawn_y` DOUBLE NULL ," +
                        "`spawn_z` DOUBLE NULL ," +
                        "`spawn_pitch` FLOAT NULL ," +
                        "`spawn_yaw` FLOAT NULL ," +
                        "`greetings` TINYINT ( 1 ) NOT NULL DEFAULT 1 " +
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
            City city;
            if (resultSet.next()) {
                do {
                    city = new City();
                    city.setId(resultSet.getInt("id"));
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
                    city.setGreetings(resultSet.getBoolean("greetings"));
                } while (resultSet.next());
            } else {
                return null;
            }
            return city;
        } catch (SQLException e) {
            return null;
        }

    }

    public City getCity(String name)
    {
        Connection connection = getDatabase().getConnection();
        PreparedStatement statement = connection.prepare(
                "SELECT * FROM " + getName() + " WHERE name = '" + name + "';"
        );
        ResultSet resultSet = connection.execute(statement);
        try {
            City city;
            if (resultSet.next()) {
                do {
                    city = new City();
                    city.setId(resultSet.getInt("id"));
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
                    city.setGreetings(resultSet.getBoolean("greetings"));
                } while (resultSet.next());
            } else {
                return null;
            }
            return city;
        } catch (SQLException e) {
            return null;
        }

    }

    public List<City> getCitys()
    {
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
                city.setId(resultSet.getInt("id"));
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
                city.setGreetings(resultSet.getBoolean("greetings"));
                citylist.add(city);
            }
            return citylist;
        } catch (SQLException e) {
            return null;
        }
    }

    public void newCity(City city) throws AlreadyExistsException
    {
        if(this.getCity(city.getName()) != null)
        {
            throw new AlreadyExistsException("Eine Stadt mit diesem Namen existiert bereits!");
        }
        Connection connection = getConnection();
        PreparedStatement statement = connection.prepare(
                "INSERT INTO " + getName() + " (name, size, description, spawn_world, spawn_x, spawn_y, spawn_z, spawn_pitch, spawn_yaw) " +
                        "VALUES (" +
                        "'" + city.getName() + "'" + "," +
                        "'" + city.getSize() + "'" + "," +
                        "'" + city.getDescription() + "'" + "," +
                        "'" + city.getSpawn().getWorld().getName() + "'" + "," +
                        "'" + city.getSpawn().getX() + "'" + "," +
                        "'" + city.getSpawn().getY() + "'" + "," +
                        "'" + city.getSpawn().getZ() + "'" + "," +
                        "'" + city.getSpawn().getPitch() + "'" + "," +
                        "'" + city.getSpawn().getYaw() + "'" +
                        ");"
        );
        connection.executeUpdate(statement);
    }

    public void updateCity(City city)
    {
        Connection connection = getConnection();
        PreparedStatement statement = connection.prepare(
                "UPDATE " + getName() + " SET " +
                        "name = '" + city.getName() + "'," +
                        "size = '" + city.getSize() + "'," +
                        "description = '" + city.getDescription() + "'," +
                        "spawn_world = '" + city.getSpawn().getWorld().getName() + "'," +
                        "spawn_x = '" + city.getSpawn().getX() + "'," +
                        "spawn_y = '" + city.getSpawn().getY() + "'," +
                        "spawn_z = '" + city.getSpawn().getZ() + "'," +
                        "spawn_pitch = '" + city.getSpawn().getPitch() + "'," +
                        "spawn_yaw = '" + city.getSpawn().getYaw() + "'" +
                        " WHERE id = '" + city.getId() + "';"
        );
        connection.executeUpdate(statement);
    }
}
