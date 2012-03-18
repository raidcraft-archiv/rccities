package de.strasse36.rccities.database;

import com.silthus.raidcraft.database.Database;
import com.silthus.raidcraft.database.RCTable;
import de.strasse36.rccities.City;
import de.strasse36.rccities.Plot;
import de.strasse36.rccities.exceptions.AlreadyExistsException;
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
public class PlotTable extends RCTable<PlotTable> {

    public PlotTable(Database database) {
        super(PlotTable.class, database, TableNames.plotTable);
    }

    @Override
    public void createTable() {
	    getDatabase().executeUpdate(
			    "CREATE TABLE  `" + getDatabase().getName() + "`.`" + getName() + "` (" +
					    "`id` INT NOT NULL AUTO_INCREMENT PRIMARY KEY ," +
					    "`city` INT NULL ," +
					    "`regionId` VARCHAR( 100 ) NULL ," +
					    "`chunk_x` INT NULL ," +
					    "`chunk_z` INT NULL ," +
					    "`public` TINYINT ( 1 ) NOT NULL DEFAULT 0 ," +
					    "`pvp` TINYINT ( 1 ) NOT NULL DEFAULT 0 " +
					    ") ENGINE = InnoDB;");
    }
    
    public int getNextIndex()
    {
	    try {
		    ResultSet resultSet = getDatabase().executeQuery(
				    "SHOW TABLE STATUS LIKE '" + getName() + "';"
		    );
		    if (resultSet.next()) {
                do {
                    return resultSet.getInt("Auto_increment");
                } while (resultSet.next());
            } else {
                return 0;
            }
        } catch (SQLException e) {
            return 0;
        }
    }
    
    public Plot getPlot(int id)
    {
	    ResultSet resultSet = getDatabase().executeQuery(
			    "SELECT * FROM " + getName() + " WHERE id = '" + id + "';"
	    );
	    try {
            Plot plot = new Plot();
            if (resultSet.next()) {
                do {
                    plot.setId(resultSet.getInt("id"));
                    plot.setCity(resultSet.getInt("city"));
                    plot.setRegionId(resultSet.getString("regionId"));
                    plot.setX(resultSet.getInt("chunk_x"));
                    plot.setZ(resultSet.getInt("chunk_z"));
                    plot.setOpen(resultSet.getBoolean("public"));
                    plot.setPvp(resultSet.getBoolean("pvp"));
                } while (resultSet.next());
            } else {
                return null;
            }
            return plot;
        } catch (SQLException e) {
            return null;
        }
    }

    public Plot getPlot(String regionId)
    {
        PreparedStatement statement = getDatabase().prepare(
		        "SELECT * FROM " + getName() + " WHERE regionId = '" + regionId + "';"
        );
        ResultSet resultSet = getDatabase().executeQuery(statement);
        try {
            Plot plot = new Plot();
            if (resultSet.next()) {
                do {
                    plot.setId(resultSet.getInt("id"));
                    plot.setCity(resultSet.getInt("city"));
                    plot.setRegionId(resultSet.getString("regionId"));
                    plot.setX(resultSet.getInt("chunk_x"));
                    plot.setZ(resultSet.getInt("chunk_z"));
                    plot.setOpen(resultSet.getBoolean("public"));
                    plot.setPvp(resultSet.getBoolean("pvp"));
                } while (resultSet.next());
            } else {
                return null;
            }
            return plot;
        } catch (SQLException e) {
            return null;
        }
    }

    public List<Plot> getPlots()
    {
        PreparedStatement statement = getDatabase().prepare(
		        "SELECT * FROM " + getName() + ";"
        );
        ResultSet resultSet = getDatabase().executeQuery(statement);
        List<Plot> plotlist = new ArrayList<Plot>();
        try {
            Plot plot;
            while (resultSet.next()) {
                plot = new Plot();
                plot.setId(resultSet.getInt("id"));
                plot.setCity(resultSet.getInt("city"));
                plot.setRegionId(resultSet.getString("regionId"));
                plot.setX(resultSet.getInt("chunk_x"));
                plot.setZ(resultSet.getInt("chunk_z"));
                plot.setOpen(resultSet.getBoolean("public"));
                plot.setPvp(resultSet.getBoolean("pvp"));
                plotlist.add(plot);
            }
            return plotlist;
        } catch (SQLException e) {
            return null;
        }
    }

    public List<Plot> getPlots(City city)
    {
        PreparedStatement statement = getDatabase().prepare(
		        "SELECT * FROM " + getName() + " WHERE city = '" + city.getId() + "';"
        );
        ResultSet resultSet = getDatabase().executeQuery(statement);
        List<Plot> plotlist = new ArrayList<Plot>();
        try {
            Plot plot;
            while (resultSet.next()) {
                plot = new Plot();
                plot.setId(resultSet.getInt("id"));
                plot.setCity(resultSet.getInt("city"));
                plot.setRegionId(resultSet.getString("regionId"));
                plot.setX(resultSet.getInt("chunk_x"));
                plot.setZ(resultSet.getInt("chunk_z"));
                plot.setOpen(resultSet.getBoolean("public"));
                plot.setPvp(resultSet.getBoolean("pvp"));
                plotlist.add(plot);
            }
            return plotlist;
        } catch (SQLException e) {
            return null;
        }
    }

    public List<Plot> getPlots(int open)
    {
        PreparedStatement statement = getDatabase().prepare(
		        "SELECT * FROM " + getName() + " WHERE public = '" + open + "';"
        );
        ResultSet resultSet = getDatabase().executeQuery(statement);
        List<Plot> plotlist = new ArrayList<Plot>();
        try {
            Plot plot;
            while (resultSet.next()) {
                plot = new Plot();
                plot.setId(resultSet.getInt("id"));
                plot.setCity(resultSet.getInt("city"));
                plot.setRegionId(resultSet.getString("regionId"));
                plot.setX(resultSet.getInt("chunk_x"));
                plot.setZ(resultSet.getInt("chunk_z"));
                plot.setOpen(resultSet.getBoolean("public"));
                plot.setPvp(resultSet.getBoolean("pvp"));
                plotlist.add(plot);
            }
            return plotlist;
        } catch (SQLException e) {
            return null;
        }
    }

    public void newPlot(Plot plot) throws AlreadyExistsException
    {
        if(this.getPlot(plot.getRegionId()) != null)
        {
            throw new AlreadyExistsException("Es exisitert bereits ein Plot mit dieser Region ID!");
        }
        PreparedStatement statement = getDatabase().prepare(
		        "INSERT INTO " + getName() + " (city, regionId, chunk_x, chunk_z, public, pvp) " +
				        "VALUES (" +
				        "'" + plot.getCity().getId() + "'" + "," +
				        "'" + plot.getRegionId() + "'" + "," +
				        "'" + plot.getX() + "'" + "," +
				        "'" + plot.getZ() + "'" + "," +
				        "'" + plot.getOpen() + "'," +
				        "'" + plot.getPvp() + "'" +
				        ");"
        );
        getDatabase().executeUpdate(statement);
    }

    public void updatePlot(Plot plot)
    {
        PreparedStatement statement = getDatabase().prepare(
		        "UPDATE " + getName() + " SET " +
				        "city = '" + plot.getCity().getId() + "'," +
				        "regionId = '" + plot.getRegionId() + "'," +
				        "chunk_x = '" + plot.getX() + "'," +
				        "chunk_z = '" + plot.getZ() + "'," +
				        "public = '" + plot.getOpen() + "'," +
				        "pvp = '" + plot.getPvp() + "'" +
				        " WHERE id = '" + plot.getId() + "';"
        );
        getDatabase().executeUpdate(statement);
    }
    
    public void deletePlot(String regionId)
    {
        PreparedStatement statement = getDatabase().prepare(
		        "DELETE FROM " + getName() + " WHERE regionId = '" + regionId + "';"
        );
        getDatabase().executeUpdate(statement);
    }
}
