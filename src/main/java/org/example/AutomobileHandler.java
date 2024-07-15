package org.example;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.Map;
import java.util.stream.Collectors;

import static org.example.Automobile.*;
import static org.example.Main.*;

public class AutomobileHandler implements HttpHandler {
    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String path = exchange.getRequestURI().getPath();
        String method = exchange.getRequestMethod();
        handleCORS(exchange);
        if (path.equals("/getAutomobiles") && method.equals("GET")) {
            handleGetAutomobiles(exchange);
        }
        if (path.equals("/createAutomobile") && method.equals("POST")) {//+
            handleCreateAutomobile(exchange);
        }
        if (path.equals("/getAutomobile") && method.equals("GET")) {// +
            handleGetAutomobileById(exchange);
        }
        if (path.equals("/updateAutomobile") && method.equals("POST")) {//+
            handleUpdateAutomobile(exchange);
        }
        if (path.equals("/deleteAutomobile") && method.equals("POST")) {//+
            handleDeleteAutomobile(exchange);
        }
    }

    private void handleGetAutomobiles(HttpExchange exchange) throws IOException {
        String response = gson.toJson(selectAll());
        exchange.sendResponseHeaders(200, response.getBytes().length);
        OutputStream os = exchange.getResponseBody();
        os.write(response.getBytes());
        os.close();
    }

    private void handleDeleteAutomobile(HttpExchange exchange) throws IOException {
        User userToDelete = requestUser(exchange);
        boolean removed = users.removeIf(u -> u.getId() == userToDelete.getId());
        if (removed) {
            saveUsers();
            String response = "User has been deleted successfully";
            exchange.sendResponseHeaders(200, response.getBytes().length);
            OutputStream os = exchange.getResponseBody();
            os.write(response.getBytes());
            os.close();
        } else {
            exchange.sendResponseHeaders(404, -1);
        }
    }

    private void handleCreateAutomobile(HttpExchange exchange) throws IOException {
        // Assuming the data is passed as JSON in the request body
        InputStreamReader isr = new InputStreamReader(exchange.getRequestBody(), "utf-8");
        BufferedReader br = new BufferedReader(isr);
        StringBuilder sb = new StringBuilder();
        String line;
        while ((line = br.readLine()) != null) {
            sb.append(line);
        }
        br.close();

        // Convert JSON data to Java object (if needed)
        // Example: Assuming JSON data is {"manufacturer":"Toyota","model":"Camry","releaseYear":2023}
        Gson gson = new Gson();
        Automobile automobile = gson.fromJson(sb.toString(), Automobile.class);

        // Create automobile in database
        createAutomobile(automobile.getManufacturer(), automobile.getModel(), automobile.getReleaseYear());

        // Prepare response
        String response = "Automobile created successfully";

        // Send response back to client
        exchange.sendResponseHeaders(200, response.getBytes().length);
        OutputStream os = exchange.getResponseBody();
        os.write(response.getBytes());
        os.close();
    }

    private void handleUpdateAutomobile(HttpExchange exchange) throws IOException {
        InputStreamReader isr = new InputStreamReader(exchange.getRequestBody(), "utf-8");
        BufferedReader br = new BufferedReader(isr);
        String body = br.lines().collect(Collectors.joining());
        Automobile auto = gson.fromJson(body, Automobile.class);
        auto.updateAutomobile();
        String response = "Automobile updated successfully";
        sendResponse(exchange, response, 200);
    }

//    private void handleDeleteAutomobile(HttpExchange exchange) throws IOException {
//
//        String query = exchange.getRequestURI().getQuery();
//        long automobileId = Long.parseLong(query.split("=")[1]); // Extract automobile ID from query parameter
//        deleteAutomobile(automobileId);
//        String response = "Automobile with ID " + automobileId + " deleted successfully";
//
//        exchange.sendResponseHeaders(200, response.getBytes().length);
//        OutputStream os = exchange.getResponseBody();
//        os.write(response.getBytes());
//        os.close();
//    }


    private void handleGetAutomobileById(HttpExchange exchange) throws IOException {
        String query = exchange.getRequestURI().getQuery();
        long id = Long.parseLong(query.split("=")[1]);
        Automobile auto = Automobile.findAutomobileById(id);
        String response = gson.toJson(auto);
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

