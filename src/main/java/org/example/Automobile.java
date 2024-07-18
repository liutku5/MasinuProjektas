package org.example;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;

import static org.example.Main.connect;


public class Automobile {
    private long id;
    private String manufacturer;
    private String model;
    private int releaseYear;
    private long fuelTypeId;

    public Automobile() {
    }

    public Automobile(long id, String manufacturer, String model, int releaseYear) {
        this.id = id;
        this.manufacturer = manufacturer;
        this.model = model;
        this.releaseYear = releaseYear;
    }

    public long getFuelTypeId() {
        return fuelTypeId;
    }

    public void setFuelTypeId(long fuelTypeId) {
        this.fuelTypeId = fuelTypeId;
    }

    public static ArrayList<Automobile> selectAll() {
        ArrayList<Automobile> automobiles = new ArrayList<>();
        String query = "SELECT * FROM automobiles";
        try {
            Connection con = connect();
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery(query);
            while (rs.next()) {
                Automobile auto = new Automobile(
                        rs.getLong("id"),
                        rs.getString("manufacturer"),
                        rs.getString("model"),
                        rs.getInt("release_Year"));
                automobiles.add(auto);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return automobiles;
    }

    public static Automobile findAutomobileById(long id) {

        String query = "SELECT * FROM automobiles where id = ?";
        Automobile auto = null;
        try {
            Connection con = Main.connect();
            PreparedStatement pst = con.prepareStatement(query);
            pst.setLong(1, id);
            ResultSet rs = pst.executeQuery();
            while ((rs.next())) {
                auto = new Automobile(rs.getLong("id"), rs.getString("manufacturer"), rs.getString("model"), rs.getInt("release_Year"));
            }
            con.close();
            pst.close();
            rs.close();
        } catch (Exception e) {
            System.out.println("Failed to find automobile.");
        }
        return auto;
    }

    public void updateAutomobile() {
        String query = "UPDATE `automobiles` SET `manufacturer`= ? ,`model`= ? ,`release_Year`= ? WHERE id = ?";
        try {
            Connection con = Main.connect();
            PreparedStatement pst = con.prepareStatement(query);
            pst.setString(1, this.manufacturer);
            pst.setString(2, this.model);
            pst.setInt(3, this.releaseYear);
            pst.setLong(4, this.id);
            pst.executeUpdate();
            con.close();
            pst.close();
        } catch (Exception e) {
            System.out.println("Failed to update automobile!");
        }
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
