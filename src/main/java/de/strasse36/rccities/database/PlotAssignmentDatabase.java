package main.java.de.strasse36.rccities.database;

import com.avaje.ebean.validation.NotNull;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;


@Entity()
@Table(name = "rccities_plotassignment")
public class PlotAssignmentDatabase {

    @Id
    private int id;
    @NotNull
    private int plot;
    @NotNull
    private int resident;
}
