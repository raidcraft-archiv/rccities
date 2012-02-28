package de.strasse36.rccities;

/**
 * Author: Philip Urban
 * Date: 28.02.12 - 20:49
 * Description:
 */
public class City {
    private int id;
    private String name;
    private String description;
    private long size;
    private double spawn_x;
    private double spawn_y;
    private double spawn_z;
    private double spawn_pitch;
    private double spawn_yaw;

    public City(){};

    public City(String name)
    {
        this.setName(name);
        this.setDescription("Noch keine Beschreibung verfügbar");
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public long getSize() {
        return size;
    }

    public void setSize(long size) {
        this.size = size;
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
}
