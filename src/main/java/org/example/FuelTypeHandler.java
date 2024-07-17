package org.example;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.stream.Collectors;

import static org.example.FuelType.selectAll;
import static org.example.Main.gson;

public class FuelTypeHandler implements HttpHandler {

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String path = exchange.getRequestURI().getPath();
        String method = exchange.getRequestMethod();
        handleCORS(exchange);
        if (path.equals("/getFuelTypes") && method.equals("GET")) {
            handleGetFuelTypes(exchange);
        }
        if (path.equals("/createFuelType") && method.equals("POST")) {//+
            handleCreateFuelType(exchange);
        }
        if (path.equals("/getFuelType") && method.equals("GET")) {// +
            handleGetFuelTypeById(exchange);
        }
        if (path.equals("/updateFuelType") && method.equals("POST")) {//+
            handleUpdateFuelType(exchange);
        }
        if (path.equals("/deleteFuelType") && method.equals("POST")) {//+
            handleDeleteFuelType(exchange);
        }
    }

    private void handleGetFuelTypes(HttpExchange exchange) throws IOException {
        String response = gson.toJson(selectAll());
        exchange.sendResponseHeaders(200, response.getBytes().length);
        OutputStream os = exchange.getResponseBody();
        os.write(response.getBytes());
        os.close();
    }

    private void handleCreateFuelType(HttpExchange exchange) throws IOException {
        System.out.println("create");
        InputStreamReader isr = new InputStreamReader(exchange.getRequestBody(), "utf-8");
        BufferedReader br = new BufferedReader(isr);
        StringBuilder json = new StringBuilder();
        String line;
        while ((line = br.readLine()) != null) {
            json.append(line);
            System.out.println(line);
        }
        br.close();
        isr.close();

        FuelType newFuelType = gson.fromJson(json.toString(), FuelType.class);
        String query = "INSERT INTO `fuel_type` (type) VALUES (?)";
        try (Connection con = Main.connect(); PreparedStatement pst = con.prepareStatement(query)) {
            pst.setString(1, newFuelType.getType());
            int rowsAffected = pst.executeUpdate();
            if (rowsAffected > 0) {
                String response = "Fuel type has been created successfully";
                sendResponse(exchange, response, 201);
            } else {
                sendResponse(exchange, "Failed to create fuel type", 500);
            }
        } catch (Exception e) {
            e.printStackTrace();
            sendResponse(exchange, "Failed to create fuel type", 500);
        }
    }

    private void handleUpdateFuelType(HttpExchange exchange) throws IOException {
        InputStreamReader isr = new InputStreamReader(exchange.getRequestBody(), "utf-8");
        BufferedReader br = new BufferedReader(isr);
        String body = br.lines().collect(Collectors.joining());
        FuelType fuel = gson.fromJson(body, FuelType.class);
        fuel.updateFuelType();
        String response = "Fuel type updated successfully";
        sendResponse(exchange, response, 200);
    }

    private void handleDeleteFuelType(HttpExchange exchange) throws IOException {
        InputStreamReader isr = new InputStreamReader(exchange.getRequestBody(), "utf-8");
        BufferedReader br = new BufferedReader(isr);
        StringBuilder json = new StringBuilder();
        String line;
        while ((line = br.readLine()) != null) {
            json.append(line);
        }
        br.close();
        isr.close();

        FuelType deleteFuelType = gson.fromJson(json.toString(), FuelType.class);
        String query = "DELETE FROM `fuel_type` WHERE id = ?";
        try (Connection con = Main.connect(); PreparedStatement pst = con.prepareStatement(query)) {
            pst.setLong(1, deleteFuelType.getId());
            int rowsAffected = pst.executeUpdate();
            if (rowsAffected > 0) {
                String response = "Fuel type has been deleted successfully";
                sendResponse(exchange, response, 200);
            } else {
                sendResponse(exchange, "Fuel type not found", 404);
            }
        } catch (Exception e) {
            e.printStackTrace();
            sendResponse(exchange, "Failed to delete fuel type", 500);
        }
    }

    private void handleGetFuelTypeById(HttpExchange exchange) throws IOException {
        String query = exchange.getRequestURI().getQuery();
        long id = Long.parseLong(query.split("=")[1]);
        FuelType fuel = FuelType.findFuelTypeById(id);
        String response = gson.toJson(fuel);
        sendResponse(exchange, response, 200);
    }

    private void sendResponse(HttpExchange exchange, String response, int statusCode) throws IOException {
        exchange.sendResponseHeaders(statusCode, response.getBytes().length);
        OutputStream os = exchange.getResponseBody();
        os.write(response.getBytes());
        os.close();
    }

    private void handleCORS(HttpExchange exchange) {
        // Allow requests from all origins
        exchange.getResponseHeaders().set("Access-Control-Allow-Origin", "*");
        // Allow specific methods
        exchange.getResponseHeaders().set("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE");
        // Allow specific headers
        exchange.getResponseHeaders().set("Access-Control-Allow-Headers", "*");
        // Allow credentials, if needed
        exchange.getResponseHeaders().set("Access-Control-Allow-Credentials", "true");
    }

}

