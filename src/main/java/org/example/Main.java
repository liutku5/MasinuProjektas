package org.example;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

public class Main {
//    public static List<Automobile> automobiles = new ArrayList<>();
    public static Gson gson = new Gson();

    public static void main(String[] args) throws IOException {
        HttpServer server = HttpServer.create(new InetSocketAddress(8000), 0);
        server.createContext("/createAutomobile", new AutomobileHandler());
        server.createContext("/getAutomobiles", new AutomobileHandler());
        server.createContext("/getAutomobile", new AutomobileHandler());
        server.createContext("/updateAutomobile", new AutomobileHandler());
        server.createContext("/deleteAutomobile", new AutomobileHandler());
        server.setExecutor(null);
        server.start();
    }

    public static Connection connect() {
        Connection con = null;
        try {
            con = DriverManager.getConnection("jdbc:mysql://localhost:3306/Automobiles_list", "root", "");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return con;
    }

}