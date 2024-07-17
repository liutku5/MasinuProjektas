package org.example;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;

import static org.example.Main.connect;

public class FuelType {
    private long id;
    private String type;

    public FuelType() {
    }

    public FuelType(long id, String type) {
        this.id = id;
        this.type = type;
    }

    public static ArrayList<FuelType> selectAll() {
        ArrayList<FuelType> fuelTypes = new ArrayList<>();
        String query = "SELECT * FROM fuel_type";
        try {
            Connection con = connect();
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery(query);
            while (rs.next()) {
                FuelType fuel = new FuelType(
                        rs.getLong("id"),
                        rs.getString("type"));
                        fuelTypes.add(fuel);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return fuelTypes;
    }

    public static FuelType findFuelTypeById(long id) {

        String query = "SELECT * FROM fuel_type where id = ?";
        FuelType fuel = null;
        try {
            Connection con = Main.connect();
            PreparedStatement pst = con.prepareStatement(query);
            pst.setLong(1, id);
            ResultSet rs = pst.executeQuery();
            while ((rs.next())) {
                fuel = new FuelType(rs.getLong("id"), rs.getString("type"));
            }
            con.close();
            pst.close();
            rs.close();
        } catch (Exception e) {
            System.out.println("Failed to find Fuel type.");
        }
        return fuel;
    }

    public void updateFuelType() {
        String query = "UPDATE `fuel_type` SET `type`= ?  WHERE id = ?";
        try {
            Connection con = Main.connect();
            PreparedStatement pst = con.prepareStatement(query);
            pst.setString(1, this.type);
            pst.setLong(4, this.id);
            pst.executeUpdate();
            con.close();
            pst.close();
        } catch (Exception e) {
            System.out.println("Failed to update Fuel type!");
        }
    }


    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "FuelType{" +
                "id=" + id +
                ", type='" + type + '\'' +
                '}';
    }
}
