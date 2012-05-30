package de.strasse36.rccities.database;

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
public class CityTable extends RCTable<CityTable> {

    public CityTable(Database database) {
        super(CityTable.class, database, TableNames.cityTable);
    }

    @Override
    public void createTable() {
	    getDatabase().executeUpdate(
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
					    "`greetings` TINYINT ( 1 ) NOT NULL DEFAULT 1 ," +
					    "`last_tax_payment` BIGINT NULL ," +
					    "`last_penalty` BIGINT NULL" +
					    ") ENGINE = InnoDB;");
    }
    
    public City getCity(int id)
    {
        PreparedStatement statement = getDatabase().prepare(
                "SELECT * FROM " + getName() + " WHERE id = '" + id + "';"
        );
        ResultSet resultSet = getDatabase().executeQuery(statement);
        try {
            City city;
            if (resultSet.next()) {
                do {
                    city = new City();
                    city.setId(resultSet.getInt("id"));
                    city.setName(resultSet.getString("name"));
                    city.setDescription(resultSet.getString("description"));
                    city.setSize(resultSet.getLong("size"));
                    Location spawn = new Location (
                            Bukkit.getWorld(resultSet.getString("spawn_world")),
                            resultSet.getDouble("spawn_x"),
                            resultSet.getDouble("spawn_y"),
                            resultSet.getDouble("spawn_z"),
                            resultSet.getFloat("spawn_yaw"),
                            resultSet.getFloat("spawn_pitch")
                    );
                    city.setSpawn(spawn);
                    city.setGreetings(resultSet.getBoolean("greetings"));
                    city.setLastTaxPayment(resultSet.getLong("last_tax_payment"));
                    city.setLastPenalty(resultSet.getLong("last_penalty"));
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
        PreparedStatement statement = getDatabase().prepare(
                "SELECT * FROM " + getName() + " WHERE name = '" + name + "';"
        );
        ResultSet resultSet = getDatabase().executeQuery(statement);
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
                    city.setLastTaxPayment(resultSet.getLong("last_tax_payment"));
                    city.setLastPenalty(resultSet.getLong("last_penalty"));
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
        PreparedStatement statement = getDatabase().prepare(
                "SELECT * FROM " + getName() + ";"
        );
        ResultSet resultSet = getDatabase().executeQuery(statement);
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
                city.setLastTaxPayment(resultSet.getLong("last_tax_payment"));
                city.setLastPenalty(resultSet.getLong("last_penalty"));
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
        PreparedStatement statement = getDatabase().prepare(
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
        getDatabase().executeUpdate(statement);
    }

    public void updateCity(City city)
    {
        PreparedStatement statement = getDatabase().prepare(
                "UPDATE " + getName() + " SET " +
                        "name = '" + city.getName() + "'," +
                        "size = '" + city.getSize() + "'," +
                        "description = '" + city.getDescription() + "'," +
                        "spawn_world = '" + city.getSpawn().getWorld().getName() + "'," +
                        "spawn_x = '" + city.getSpawn().getX() + "'," +
                        "spawn_y = '" + city.getSpawn().getY() + "'," +
                        "spawn_z = '" + city.getSpawn().getZ() + "'," +
                        "spawn_pitch = '" + city.getSpawn().getPitch() + "'," +
                        "spawn_yaw = '" + city.getSpawn().getYaw() + "'," +
                        "greetings = '" + city.getGreetings() + "'," +
                        "last_tax_payment = '" + city.getLastTaxPayment() + "'," +
                        "last_penalty = '" + city.getLastPenalty() + "'" +
                        " WHERE id = '" + city.getId() + "';"
        );
        getDatabase().executeUpdate(statement);
    }

    public void deleteCity(int cityId)
    {
        PreparedStatement statement = getDatabase().prepare(
                "DELETE FROM " + getName() + " WHERE id = '" + cityId + "';"
        );
        getDatabase().executeUpdate(statement);
    }
}
