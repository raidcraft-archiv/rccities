package de.strasse36.rccities.database;

import com.silthus.raidcraft.database.Database;
import com.silthus.raidcraft.database.RCTable;
import de.strasse36.rccities.Assignment;
import de.strasse36.rccities.Plot;
import de.strasse36.rccities.Resident;
import de.strasse36.rccities.exceptions.AlreadyExistsException;
import de.strasse36.rccities.util.TableNames;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Author: Philip Urban
 * Date: 10.03.12 - 15:00
 * Description:
 */
public class AssignmentsTable extends RCTable<AssignmentsTable> {

    public AssignmentsTable(Database database) {
        super(AssignmentsTable.class, database, TableNames.getAssignmentsTable());
    }

    @Override
    public void createTable() {
	    getDatabase().executeUpdate(
			    "CREATE TABLE  `" + getDatabase().getName() + "`.`" + getName() + "` (" +
					    "`id` INT NOT NULL AUTO_INCREMENT PRIMARY KEY ," +
					    "`plot_id` INT NULL ," +
					    "`resident_id` INT NULL" +
					    ") ENGINE = InnoDB;");
    }

    public List<Assignment> getAssignments()
    {
        ResultSet resultSet = getAllData();
        List<Assignment> assignmentList = new ArrayList<Assignment>();
        try {
            Assignment assignment;
            while (resultSet.next()) {
                assignment = new Assignment();
                assignment.setPlot_id(resultSet.getInt("plot_id"));
                assignment.setResident_id(resultSet.getInt("resident_id"));
                assignmentList.add(assignment);
            }
            return assignmentList;
        } catch (SQLException e) {
            return null;
        }
    }

    public List<Assignment> getAssignments(Resident resident)
    {
        try {
	        ResultSet resultSet = getDatabase().executeQuery(
			        "SELECT * FROM " + getName() + " WHERE resident_id = '" + resident.getId() + "';"
	        );
	        List<Assignment> assignmentList = new ArrayList<Assignment>();
	        Assignment assignment;
            while (resultSet.next()) {
                assignment = new Assignment();
                assignment.setPlot_id(resultSet.getInt("plot_id"));
                assignment.setResident_id(resultSet.getInt("resident_id"));
                assignmentList.add(assignment);
            }
            return assignmentList;
        } catch (SQLException e) {
            return null;
        }
    }

    public List<Assignment> getAssignments(Plot plot)
    {
        try {
	        PreparedStatement statement = getDatabase().prepare(
			        "SELECT * FROM " + getName() + " WHERE plot_id = '" + plot.getId() + "';"
	        );
	        ResultSet resultSet = getDatabase().executeQuery(statement);
	        List<Assignment> assignmentList = new ArrayList<Assignment>();
	        Assignment assignment;
            while (resultSet.next()) {
                assignment = new Assignment();
                assignment.setPlot_id(resultSet.getInt("plot_id"));
                assignment.setResident_id(resultSet.getInt("resident_id"));
                assignmentList.add(assignment);
            }
            return assignmentList;
        } catch (SQLException e) {
            return null;
        }
    }


    public void newAssignment(Plot plot, Resident resident) throws AlreadyExistsException
    {
        List<Assignment> assignmentList = this.getAssignments(resident);
        for(Assignment assignment : assignmentList)
        {
            if(assignment.getPlot_id() == plot.getId())
                throw new AlreadyExistsException("Dieser Plot ist dem Spieler bereits zugewiesen!");
        }
        
        PreparedStatement statement = getDatabase().prepare(
		        "INSERT INTO " + getName() + " (plot_id, resident_id) " +
				        "VALUES (" +
				        "'" + plot.getId() + "'" + "," +
				        "'" + resident.getId() + "'" +
				        ");"
        );
	    getDatabase().executeUpdate(statement);
    }
    
    public void deleteAssignment(Plot plot, Resident resident)
    {
        PreparedStatement statement = getDatabase().prepare(
		        "DELETE FROM " + getName() + " WHERE plot_id = '" + plot.getId() + "' AND resident_id = '" + resident.getId() + "';"
        );
	    getDatabase().executeUpdate(statement);
    }

    public void deleteAssignment(Plot plot)
    {
        PreparedStatement statement = getDatabase().prepare(
		        "DELETE FROM " + getName() + " WHERE plot_id = '" + plot.getId() + "';"
        );
	    getDatabase().executeUpdate(statement);
    }
}
