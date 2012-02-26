package main.java.de.strasse36.rccities.database;

import com.avaje.ebean.validation.Length;
import com.avaje.ebean.validation.NotNull;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;


@Entity()
@Table(name = "rccities_plots")
public class PlotsDatabase {

    @Id
    private int id;
    @NotNull
    private int city;
    @NotNull
    private int point_1;
    @NotNull
    private int point_2;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getCity() {
        return city;
    }

    public void setCity(int city) {
        this.city = city;
    }

    public int getPoint_1() {
        return point_1;
    }

    public void setPoint_1(int point_1) {
        this.point_1 = point_1;
    }

    public int getPoint_2() {
        return point_2;
    }

    public void setPoint_2(int point_2) {
        this.point_2 = point_2;
    }
}
