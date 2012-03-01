package de.strasse36.rccities.database;

import com.silthus.raidcraft.database.Connection;
import com.silthus.raidcraft.database.Database;
import com.silthus.raidcraft.database.RCTable;
import com.silthus.raidcraft.database.UnknownTableException;
import de.strasse36.rccities.Resident;
import de.strasse36.rccities.util.TableNames;

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
public class ResidentTable extends RCTable {

    public ResidentTable(Database database) {
        super(database, TableNames.getResidentTable());
    }

    @Override
    public void createTable() {
        Connection connection = getDatabase().getConnection();
        PreparedStatement prepare = connection.prepare(
                "CREATE TABLE  `" + getDatabase().getName() + "`.`" + getName() + "` (\n" +
                    "`id` INT NOT NULL ," +
                    "`name` VARCHAR( 32 ) NULL ," +
                    "`city` INT NULL ," +
                    "`profession` VARCHAR( 32 ) NULL ," +
                    "PRIMARY KEY ( `id` )" +
                ") ENGINE = InnoDB;"
        );
        connection.executeUpdate(prepare);
    }

    public List<Resident> getResidents() {
        Connection connection = getDatabase().getConnection();
        PreparedStatement statement = connection.prepare(
                "SELECT * FROM " + getName() + ";"
        );
        ResultSet resultSet = connection.execute(statement);
        List<Resident> residentlist = new ArrayList<Resident>();
        try {
            Resident resident;
            while (resultSet.next()) {
                resident = new Resident();
                resident.setId(resultSet.getInt("id"));
                resident.setName(resultSet.getString("name"));
                try {
                    resident.setCity(((CityTable) RCCitiesDatabase.get().getTable(TableNames.getCityTable())).getCity(resultSet.getInt("city")));
                } catch (UnknownTableException e) {
                    e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
                }
                resident.setProfession(resultSet.getString("profession"));

                residentlist.add(resident);
            }
            return residentlist;
        } catch (SQLException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
        return null;
    }

    public Resident getResident(int id) {
        Connection connection = getDatabase().getConnection();
        PreparedStatement statement = connection.prepare(
                "SELECT * FROM " + getName() + " WHERE id = '" + id + "';"
        );
        ResultSet resultSet = connection.execute(statement);
        try {
            Resident resident = new Resident();
            while (resultSet.next()) {
                resident = new Resident();
                resident.setId(resultSet.getInt("id"));
                resident.setName(resultSet.getString("name"));
                try {
                    resident.setCity(((CityTable) RCCitiesDatabase.get().getTable(TableNames.getCityTable())).getCity(resultSet.getInt("city")));
                } catch (UnknownTableException e) {
                    e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
                }
                resident.setProfession(resultSet.getString("profession"));
            }
            return resident;
        } catch (SQLException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
        return null;
    }

    public Resident getResident(String name) {
        Connection connection = getDatabase().getConnection();
        PreparedStatement statement = connection.prepare(
                "SELECT * FROM " + getName() + " WHERE name = '" + name + "';"
        );
        ResultSet resultSet = connection.execute(statement);
        try {
            Resident resident = new Resident();
            while (resultSet.next()) {
                resident = new Resident();
                resident.setId(resultSet.getInt("id"));
                resident.setName(resultSet.getString("name"));
                try {
                    resident.setCity(((CityTable) RCCitiesDatabase.get().getTable(TableNames.getCityTable())).getCity(resultSet.getInt("city")));
                } catch (UnknownTableException e) {
                    e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
                }
                resident.setProfession(resultSet.getString("profession"));
            }
            return resident;
        } catch (SQLException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
        return null;
    }

    public void updateResident(Resident resident)
    {
        Connection connection = getDatabase().getConnection();
        PreparedStatement statement;
        if(getResident(resident.getId()) == null)
        {
            statement = connection.prepare(
                    "INSERT INTO "  + getName() + "(name, city, profession) VALUES (" + getName() +
                            "name = '" + resident.getName() + "'," +
                            "city = '" + resident.getCity().getId() + "'," +
                            "profession = '" + resident.getProfession() + "'" +
                            ");"
            );

        }
        else
        {
            statement = connection.prepare(
                    "UPDATE " + getName() + " SET " +
                            "name = '" + resident.getName() + "'," +
                            "city = '" + resident.getCity().getId() + "'," +
                            "profession = '" + resident.getProfession() + "'" +
                            ");"
            );
        }
        connection.execute(statement);
    }
}
