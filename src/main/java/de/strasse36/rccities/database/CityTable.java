package de.strasse36.rccities.database;

import com.silthus.raidcraft.database.Connection;
import com.silthus.raidcraft.database.Database;
import com.silthus.raidcraft.database.RCTable;
import de.strasse36.rccities.City;

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
        super(database, "cities");
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
                        "`spawn_x` DOUBLE NULL ," +
                        "`spawn_y` DOUBLE NULL ," +
                        "`spawn_z` DOUBLE NULL ," +
                        "`spawn_pitch` DOUBLE NULL ," +
                        "`spawn_yaw` DOUBLE NULL ," +
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
                city.setSpawn_x(resultSet.getDouble("spawn_x"));
                city.setSpawn_y(resultSet.getDouble("spawn_y"));
                city.setSpawn_z(resultSet.getDouble("spawn_z"));
                city.setSpawn_pitch(resultSet.getDouble("spawn_pitch"));
                city.setSpawn_yaw(resultSet.getDouble("spawn_yaw"));
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
                city.setSpawn_x(resultSet.getDouble("spawn_x"));
                city.setSpawn_y(resultSet.getDouble("spawn_y"));
                city.setSpawn_z(resultSet.getDouble("spawn_z"));
                city.setSpawn_pitch(resultSet.getDouble("spawn_pitch"));
                city.setSpawn_yaw(resultSet.getDouble("spawn_yaw"));
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
                        city.getSpawn_x() + "," +
                        city.getSpawn_y() + "," +
                        city.getSpawn_z() + "," +
                        city.getSpawn_pitch() + "," +
                        city.getSpawn_yaw() + "," +
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
                        "spawn_x = " + city.getSpawn_x() + "," +
                        "spawn_y = " + city.getSpawn_y() + "," +
                        "spawn_z = " + city.getSpawn_z() + "," +
                        "spawn_pitch = " + city.getSpawn_pitch() + "," +
                        "spawn_yaw = " + city.getSpawn_yaw() + "," +
                        ");"
        );
        connection.execute(statement);
    }
}
