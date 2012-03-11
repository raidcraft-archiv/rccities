package de.strasse36.rccities.database;

import com.silthus.raidcraft.database.Connection;
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
public class PlotTable extends RCTable {

    public PlotTable(Database database) {
        super(database, TableNames.getPlotTable());
    }

    @Override
    public void createTable() {
        Connection connection = getDatabase().getConnection();
        PreparedStatement prepare = connection.prepare(
                "CREATE TABLE  `" + getDatabase().getName() + "`.`" + getName() + "` (" +
                        "`id` INT NOT NULL AUTO_INCREMENT PRIMARY KEY ," +
                        "`city` INT NULL ," +
                        "`regionId` VARCHAR( 100 ) NULL ," +
                        "`chunk_x` INT NULL ," +
                        "`chunk_z` INT NULL ," +
                        "`public` TINYINT ( 1 ) NOT NULL DEFAULT 0 ," +
                        "`pvp` TINYINT ( 1 ) NOT NULL DEFAULT 0 " +
                        ") ENGINE = InnoDB;");
        connection.executeUpdate(prepare);
    }
    
    public Plot getPlot(int id)
    {
        Connection connection = getDatabase().getConnection();
        PreparedStatement statement = connection.prepare(
                "SELECT * FROM " + getName() + " WHERE id = '" + id + "';"
        );
        ResultSet resultSet = connection.execute(statement);
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
        Connection connection = getDatabase().getConnection();
        PreparedStatement statement = connection.prepare(
                "SELECT * FROM " + getName() + " WHERE regionId = '" + regionId + "';"
        );
        ResultSet resultSet = connection.execute(statement);
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
        Connection connection = getDatabase().getConnection();
        PreparedStatement statement = connection.prepare(
                "SELECT * FROM " + getName() + ";"
        );
        ResultSet resultSet = connection.execute(statement);
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
        Connection connection = getDatabase().getConnection();
        PreparedStatement statement = connection.prepare(
                "SELECT * FROM " + getName() + " WHERE city = '" + city.getId() + "';"
        );
        ResultSet resultSet = connection.execute(statement);
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
        Connection connection = getDatabase().getConnection();
        PreparedStatement statement = connection.prepare(
                "SELECT * FROM " + getName() + " WHERE public = '" + open + "';"
        );
        ResultSet resultSet = connection.execute(statement);
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
        Connection connection = getConnection();
        PreparedStatement statement = connection.prepare(
                "INSERT INTO " + getName() + " (city, regionId, chunk_x, chunk_z) " +
                        "VALUES (" +
                        "'" + plot.getCity().getId() + "'" + "," +
                        "'" + plot.getRegionId() + "'" + "," +
                        "'" + plot.getX() + "'" + "," +
                        "'" + plot.getZ() + "'" + "," +
                        "'" + plot.getOpen() + "'," +
                        "'" + plot.getPvp() + "'" +
                        ");"
        );
        connection.executeUpdate(statement);
    }

    public void updatePlot(Plot plot)
    {
        Connection connection = getConnection();
        PreparedStatement statement = connection.prepare(
                "UPDATE " + getName() + " SET " +
                        "city = '" + plot.getCity().getId() + "'," +
                        "regionId = '" + plot.getRegionId() + "'," +
                        "chunk_x = '" + plot.getX() + "'," +
                        "chunk_y = '" + plot.getZ() + "'," +
                        "public = '" + plot.getOpen() + "'," +
                        "pvp = '" + plot.getPvp() + "'" +
                        " WHERE id = '" + plot.getId() + "';"
        );
        connection.executeUpdate(statement);
    }
    
    public void deletePlot(String regionId)
    {
        Connection connection = getConnection();
        PreparedStatement statement = connection.prepare(
                "DELETE FROM " + getName() + " WHERE regionId = '" + regionId + "';"
        );
        connection.executeUpdate(statement);
    }
}
