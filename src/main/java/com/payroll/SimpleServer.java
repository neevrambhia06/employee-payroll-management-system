package com.payroll;

import com.google.gson.Gson;
import com.payroll.dao.EmployeeDAO;
import com.payroll.dao.PayrollDAO;
import com.payroll.model.Employee;
import com.payroll.model.Payroll;
import com.payroll.database.DatabaseInitializer;

import java.io.*;
import java.net.InetSocketAddress;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import com.sun.net.httpserver.*;

public class SimpleServer {
    
    public static void main(String[] args) {
        try {
            System.out.println("===========================================");
            System.out.println("  Initializing Payroll Management System");
            System.out.println("===========================================");
            
            // Initialize database
            System.out.println("Setting up database...");
            DatabaseInitializer.initialize();
            System.out.println("Database ready!");
            
            // Create server
            System.out.println("Starting server...");
            HttpServer server = HttpServer.create(new InetSocketAddress(8080), 0);
            
            EmployeeDAO empDAO = new EmployeeDAO();
            PayrollDAO payDAO = new PayrollDAO();
            Gson gson = new Gson();
            
            // API: Employee operations
            server.createContext("/api/employees", (HttpExchange exchange) -> {
                String method = exchange.getRequestMethod();
                String path = exchange.getRequestURI().getPath();
                
                try {
                    // Handle GET all employees
                    if ("GET".equals(method) && (path.equals("/api/employees") || path.equals("/api/employees/"))) {
                        exchange.getResponseHeaders().set("Content-Type", "application/json");
                        exchange.getResponseHeaders().set("Access-Control-Allow-Origin", "*");
                        String response = gson.toJson(empDAO.getAllEmployees());
                        exchange.sendResponseHeaders(200, response.length());
                        OutputStream os = exchange.getResponseBody();
                        os.write(response.getBytes());
                        os.close();
                    }
                    // Handle GET employee by ID (e.g., /api/employees/EMP001)
                    else if ("GET".equals(method) && path.matches("/api/employees/[^/]+")) {
                        String empId = path.substring(path.lastIndexOf("/") + 1);
                        Employee emp = empDAO.getEmployeeById(empId);
                        if (emp != null) {
                            exchange.getResponseHeaders().set("Content-Type", "application/json");
                            exchange.getResponseHeaders().set("Access-Control-Allow-Origin", "*");
                            String response = gson.toJson(emp);
                            exchange.sendResponseHeaders(200, response.length());
                            OutputStream os = exchange.getResponseBody();
                            os.write(response.getBytes());
                            os.close();
                        } else {
                            String response = "{\"error\":\"Employee not found\"}";
                            exchange.getResponseHeaders().set("Access-Control-Allow-Origin", "*");
                            exchange.sendResponseHeaders(404, response.length());
                            OutputStream os = exchange.getResponseBody();
                            os.write(response.getBytes());
                            os.close();
                        }
                    }
                    // Handle POST - Add employee
                    else if ("POST".equals(method)) {
                        InputStreamReader isr = new InputStreamReader(exchange.getRequestBody(), "utf-8");
                        BufferedReader br = new BufferedReader(isr);
                        StringBuilder requestBody = new StringBuilder();
                        String line;
                        while ((line = br.readLine()) != null) {
                            requestBody.append(line);
                        }
                        br.close();
                        
                        Employee emp = gson.fromJson(requestBody.toString(), Employee.class);
                        boolean success = empDAO.addEmployee(emp);
                        
                        exchange.getResponseHeaders().set("Content-Type", "application/json");
                        exchange.getResponseHeaders().set("Access-Control-Allow-Origin", "*");
                        String response = success ? "{\"message\":\"Employee added successfully\"}" : "{\"error\":\"Failed to add employee\"}";
                        exchange.sendResponseHeaders(success ? 201 : 400, response.length());
                        OutputStream os = exchange.getResponseBody();
                        os.write(response.getBytes());
                        os.close();
                    }
                    // Handle PUT - Update employee
                    else if ("PUT".equals(method)) {
                        InputStreamReader isr = new InputStreamReader(exchange.getRequestBody(), "utf-8");
                        BufferedReader br = new BufferedReader(isr);
                        StringBuilder requestBody = new StringBuilder();
                        String line;
                        while ((line = br.readLine()) != null) {
                            requestBody.append(line);
                        }
                        br.close();
                        
                        Employee emp = gson.fromJson(requestBody.toString(), Employee.class);
                        boolean success = empDAO.updateEmployee(emp);
                        
                        exchange.getResponseHeaders().set("Content-Type", "application/json");
                        exchange.getResponseHeaders().set("Access-Control-Allow-Origin", "*");
                        String response = success ? "{\"message\":\"Employee updated successfully\"}" : "{\"error\":\"Failed to update employee\"}";
                        exchange.sendResponseHeaders(success ? 200 : 400, response.length());
                        OutputStream os = exchange.getResponseBody();
                        os.write(response.getBytes());
                        os.close();
                    }
                    // Handle DELETE - Delete employee
                    else if ("DELETE".equals(method) && path.matches("/api/employees/[^/]+")) {
                        String empId = path.substring(path.lastIndexOf("/") + 1);
                        boolean success = empDAO.deleteEmployee(empId);
                        
                        exchange.getResponseHeaders().set("Content-Type", "application/json");
                        exchange.getResponseHeaders().set("Access-Control-Allow-Origin", "*");
                        String response = success ? "{\"message\":\"Employee deleted successfully\"}" : "{\"error\":\"Failed to delete employee\"}";
                        exchange.sendResponseHeaders(success ? 200 : 400, response.length());
                        OutputStream os = exchange.getResponseBody();
                        os.write(response.getBytes());
                        os.close();
                    }
                } catch (Exception e) {
                    exchange.getResponseHeaders().set("Access-Control-Allow-Origin", "*");
                    String errorResponse = "{\"error\":\"" + e.getMessage() + "\"}";
                    try {
                        exchange.sendResponseHeaders(500, errorResponse.length());
                        OutputStream os = exchange.getResponseBody();
                        os.write(errorResponse.getBytes());
                        os.close();
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                    e.printStackTrace();
                }
            });
            
            // API: Payroll operations
            server.createContext("/api/payroll", (HttpExchange exchange) -> {
                String method = exchange.getRequestMethod();
                String path = exchange.getRequestURI().getPath();
                
                try {
                    // Handle GET all payrolls
                    if ("GET".equals(method) && (path.equals("/api/payroll") || path.equals("/api/payroll/"))) {
                        exchange.getResponseHeaders().set("Content-Type", "application/json");
                        exchange.getResponseHeaders().set("Access-Control-Allow-Origin", "*");
                        String response = gson.toJson(payDAO.getAllPayrolls());
                        exchange.sendResponseHeaders(200, response.length());
                        OutputStream os = exchange.getResponseBody();
                        os.write(response.getBytes());
                        os.close();
                    }
                    // Handle POST - Add payroll
                    else if ("POST".equals(method)) {
                        InputStreamReader isr = new InputStreamReader(exchange.getRequestBody(), "utf-8");
                        BufferedReader br = new BufferedReader(isr);
                        StringBuilder requestBody = new StringBuilder();
                        String line;
                        while ((line = br.readLine()) != null) {
                            requestBody.append(line);
                        }
                        br.close();
                        
                        Payroll payroll = gson.fromJson(requestBody.toString(), Payroll.class);
                        double netSalary = payroll.getBaseSalary() + payroll.getOvertime() + 
                                          payroll.getBonus() - payroll.getDeductions();
                        payroll.setNetSalary(netSalary);
                        
                        boolean success = payDAO.addPayroll(payroll);
                        
                        exchange.getResponseHeaders().set("Content-Type", "application/json");
                        exchange.getResponseHeaders().set("Access-Control-Allow-Origin", "*");
                        String response = success ? "{\"message\":\"Payroll added successfully\"}" : "{\"error\":\"Failed to add payroll\"}";
                        exchange.sendResponseHeaders(success ? 201 : 400, response.length());
                        OutputStream os = exchange.getResponseBody();
                        os.write(response.getBytes());
                        os.close();
                    }
                    // Handle PUT - Update payroll
                    else if ("PUT".equals(method)) {
                        InputStreamReader isr = new InputStreamReader(exchange.getRequestBody(), "utf-8");
                        BufferedReader br = new BufferedReader(isr);
                        StringBuilder requestBody = new StringBuilder();
                        String line;
                        while ((line = br.readLine()) != null) {
                            requestBody.append(line);
                        }
                        br.close();
                        
                        Payroll payroll = gson.fromJson(requestBody.toString(), Payroll.class);
                        if (payroll.getId() <= 0) {
                            String response = "{\"error\":\"Payroll id is required for update\"}";
                            exchange.getResponseHeaders().set("Access-Control-Allow-Origin", "*");
                            exchange.sendResponseHeaders(400, response.length());
                            OutputStream os = exchange.getResponseBody();
                            os.write(response.getBytes());
                            os.close();
                            return;
                        }
                        double netSalary = payroll.getBaseSalary() + payroll.getOvertime() + 
                                          payroll.getBonus() - payroll.getDeductions();
                        payroll.setNetSalary(netSalary);
                        boolean success = payDAO.updatePayroll(payroll);

                        exchange.getResponseHeaders().set("Content-Type", "application/json");
                        exchange.getResponseHeaders().set("Access-Control-Allow-Origin", "*");
                        String response = success ? "{\"message\":\"Payroll updated successfully\"}" : "{\"error\":\"Failed to update payroll\"}";
                        exchange.sendResponseHeaders(success ? 200 : 400, response.length());
                        OutputStream os = exchange.getResponseBody();
                        os.write(response.getBytes());
                        os.close();
                    }
                    // Handle DELETE - Delete payroll by id
                    else if ("DELETE".equals(method) && path.matches("/api/payroll/\\d+")) {
                        String idStr = path.substring(path.lastIndexOf("/") + 1);
                        int id = Integer.parseInt(idStr);
                        boolean success = payDAO.deletePayroll(id);

                        exchange.getResponseHeaders().set("Content-Type", "application/json");
                        exchange.getResponseHeaders().set("Access-Control-Allow-Origin", "*");
                        String response = success ? "{\"message\":\"Payroll deleted successfully\"}" : "{\"error\":\"Failed to delete payroll\"}";
                        exchange.sendResponseHeaders(success ? 200 : 400, response.length());
                        OutputStream os = exchange.getResponseBody();
                        os.write(response.getBytes());
                        os.close();
                    }
                } catch (Exception e) {
                    exchange.getResponseHeaders().set("Access-Control-Allow-Origin", "*");
                    String errorResponse = "{\"error\":\"" + e.getMessage() + "\"}";
                    try {
                        exchange.sendResponseHeaders(500, errorResponse.length());
                        OutputStream os = exchange.getResponseBody();
                        os.write(errorResponse.getBytes());
                        os.close();
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                    e.printStackTrace();
                }
            });
            
            // Serve static files
            server.createContext("/", (HttpExchange exchange) -> {
                try {
                    String path = exchange.getRequestURI().getPath();
                    if (path.equals("/") || path.equals("")) {
                        path = "/index.html";
                    }
                    
                    Path filePath = Paths.get("webapp" + path);
                    
                    if (Files.exists(filePath) && Files.isRegularFile(filePath)) {
                        String contentType = getContentType(filePath.toString());
                        exchange.getResponseHeaders().set("Content-Type", contentType);
                        exchange.sendResponseHeaders(200, Files.size(filePath));
                        
                        Files.copy(filePath, exchange.getResponseBody());
                        exchange.getResponseBody().close();
                    } else {
                        String response = "File not found";
                        exchange.sendResponseHeaders(404, response.length());
                        OutputStream os = exchange.getResponseBody();
                        os.write(response.getBytes());
                        os.close();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
            
            server.start();
            
            System.out.println();
            System.out.println("===========================================");
            System.out.println("  Server Started Successfully!");
            System.out.println("===========================================");
            System.out.println("  Open your browser and visit:");
            System.out.println("  http://localhost:8080");
            System.out.println("===========================================");
            System.out.println();
            System.out.println("Press Ctrl+C to stop the server");
            System.out.println();
            
            // Keep server running
            Thread.currentThread().join();
            
        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    private static String getContentType(String filename) {
        if (filename.endsWith(".html")) return "text/html";
        if (filename.endsWith(".css")) return "text/css";
        if (filename.endsWith(".js")) return "application/javascript";
        if (filename.endsWith(".json")) return "application/json";
        return "application/octet-stream";
    }
}

