package main.java.de.strasse36.rccities.database;

import com.avaje.ebean.validation.Length;
import com.avaje.ebean.validation.NotNull;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;


@Entity()
@Table(name = "rccities_cities")
public class CitiesDatabase {

    @Id
    private int id;
    @NotNull
    @Length(max = 32)

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
