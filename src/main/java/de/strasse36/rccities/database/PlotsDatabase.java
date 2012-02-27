package de.strasse36.rccities.database;

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
    private int chunk_x;
    @NotNull
    private int chunk_y;

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

    public int getChunk_x() {
        return chunk_x;
    }

    public void setChunk_x(int chunk_x) {
        this.chunk_x = chunk_x;
    }

    public int getChunk_y() {
        return chunk_y;
    }

    public void setChunk_y(int chunk_y) {
        this.chunk_y = chunk_y;
    }
}
