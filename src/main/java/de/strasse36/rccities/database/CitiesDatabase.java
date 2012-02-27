package de.strasse36.rccities.database;

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
    @Length(max = 50)
    private String name;
    @NotNull
    private int size;
    @NotNull
    private String description;
    @NotNull
    private double spawn_x;
    @NotNull
    private double spawn_y;
    @NotNull
    private double spawn_z;
    @NotNull
    private double spawn_pitch;
    @NotNull
    private double spawn_yaw;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public double getSpawn_x() {
        return spawn_x;
    }

    public void setSpawn_x(double spawn_x) {
        this.spawn_x = spawn_x;
    }

    public double getSpawn_y() {
        return spawn_y;
    }

    public void setSpawn_y(double spawn_y) {
        this.spawn_y = spawn_y;
    }

    public double getSpawn_z() {
        return spawn_z;
    }

    public void setSpawn_z(double spawn_z) {
        this.spawn_z = spawn_z;
    }

    public double getSpawn_pitch() {
        return spawn_pitch;
    }

    public void setSpawn_pitch(double spawn_pitch) {
        this.spawn_pitch = spawn_pitch;
    }

    public double getSpawn_yaw() {
        return spawn_yaw;
    }

    public void setSpawn_yaw(double spawn_yaw) {
        this.spawn_yaw = spawn_yaw;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
