package de.strasse36.rccities.database.tables;

import com.silthus.raidcraft.database.Database;
import com.silthus.raidcraft.database.RCTable;
import com.silthus.raidcraft.util.RCLogger;
import de.strasse36.rccities.City;
import de.strasse36.rccities.Resident;
import de.strasse36.rccities.database.TableHandler;
import de.strasse36.rccities.database.TableNames;

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
public class ResidentTable extends RCTable<ResidentTable> {

    public ResidentTable(Database database) {
        super(ResidentTable.class, database, TableNames.residentTable);
    }

    @Override
    public void createTable() {
        PreparedStatement prepare = getDatabase().prepare(
		        "CREATE TABLE  `" + getDatabase().getName() + "`.`" + getName() + "` (\n" +
				        "`id` INT NOT NULL AUTO_INCREMENT PRIMARY KEY ," +
				        "`name` VARCHAR( 32 ) NULL ," +
				        "`city` INT NULL ," +
				        "`profession` VARCHAR( 32 ) NULL" +
				        ") ENGINE = InnoDB;"
        );
        getDatabase().executeUpdate(prepare);
    }

    public List<Resident> getResidents(City city) {
        PreparedStatement statement = getDatabase().prepare(
		        "SELECT * FROM " + getName() + " WHERE city = '" + city.getId() + "';"
        );
        ResultSet resultSet = getDatabase().executeQuery(statement);
        List<Resident> residentlist = new ArrayList<Resident>();
        try {
            Resident resident;
            if (resultSet.next()) {
                do {
                    resident = new Resident();
                    resident.setId(resultSet.getInt("id"));
                    resident.setName(resultSet.getString("name"));
                    resident.setCity(TableHandler.get().getCityTable().getCity(resultSet.getInt("city")));
                    resident.setProfession(resultSet.getString("profession"));
                    residentlist.add(resident);
                } while (resultSet.next());
            }
            return residentlist;
        } catch (SQLException e) {
           RCLogger.warning(e.getMessage());
        }
        return null;
    }

    public void updateResident(Resident resident)
    {
        PreparedStatement statement;
        if(getResident(resident.getName()) == null)
        {
            statement = getDatabase().prepare(
		            "INSERT INTO " + getName() + "(name, city, profession) VALUES (" +
				            "'" + resident.getName() + "'," +
				            "'" + resident.getCity().getId() + "'," +
				            "'" + resident.getProfession() + "'" +
				            ");"
            );

        }
        else
        {
            statement = getDatabase().prepare(
		            "UPDATE " + getName() + " SET " +
				            "name = '" + resident.getName() + "'," +
				            "city = '" + resident.getCity().getId() + "'," +
				            "profession = '" + resident.getProfession() + "'" +
				            " WHERE name = '" + resident.getName() + "'"
            );
        }
        getDatabase().executeUpdate(statement);
    }
    
    public List<Resident.CityProperties> getCitiesProperties(String player) {
        List<Resident.CityProperties> cities = new ArrayList<Resident.CityProperties>();

        //TODO implement


        return cities;
    }
}
