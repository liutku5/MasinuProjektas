package org.example;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;

import static org.example.Main.connect;


public class Automobile {
    private long id;
    private String manufacturer;
    private String model;
    private int releaseYear;

    public Automobile() {
    }

    public Automobile(long id, String manufacturer, String model, int releaseYear) {
        this.id = id;
        this.manufacturer = manufacturer;
        this.model = model;
        this.releaseYear = releaseYear;
    }

    public static ArrayList<Automobile> selectAll() {
        ArrayList<Automobile> automobiles = new ArrayList<>();
        String query = "SELECT * FROM vehicles";
        try {
            Connection con = connect();
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery(query);
            while (rs.next()) {
                Automobile auto = new Automobile(
                        rs.getLong("id"),
                        rs.getString("manufacturer"),
                        rs.getString("model"),
                        rs.getInt("releaseYear"),
                        automobiles.add(auto);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return automobiles;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getManufacturer() {
        return manufacturer;
    }

    public void setManufacturer(String manufacturer) {
        this.manufacturer = manufacturer;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public int getReleaseYear() {
        return releaseYear;
    }

    public void setReleaseYear(int releaseYear) {
        this.releaseYear = releaseYear;
    }

    @Override
    public String toString() {
        return "automobile{" +
                "id=" + id +
                ", manufacturer='" + manufacturer + '\'' +
                ", model='" + model + '\'' +
                ", releaseYear=" + releaseYear +
                '}';
    }
}
