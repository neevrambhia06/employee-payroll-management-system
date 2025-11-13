package com.payroll;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.eclipse.jetty.servlet.DefaultServlet;
import java.net.URL;
import java.net.URI;

import com.payroll.servlet.EmployeeServlet;
import com.payroll.servlet.PayrollServlet;
import com.payroll.database.DatabaseInitializer;

public class Launcher {
    private static Server server;
    
    public static void main(String[] args) {
        try {
            System.out.println("Initializing database...");
            DatabaseInitializer.initialize();
            System.out.println("Database initialized successfully!");
            System.out.println();
            
            System.out.println("Starting server...");
            server = new Server(8080);
            
            ServletContextHandler context = new ServletContextHandler(ServletContextHandler.SESSIONS);
            context.setContextPath("/");
            
            // Add servlets
            context.addServlet(new ServletHolder(new EmployeeServlet()), "/api/employees/*");
            context.addServlet(new ServletHolder(new PayrollServlet()), "/api/payroll/*");
            
            // Add default servlet for static files
            URL webappURL = Launcher.class.getResource("/webapp");
            if (webappURL != null) {
                String webDir = webappURL.toExternalForm();
                context.setResourceBase(webDir);
                context.addServlet(DefaultServlet.class, "/");
            }
            
            server.setHandler(context);
            server.start();
            
            System.out.println();
            System.out.println("=========================================");
            System.out.println("  Payroll System Started Successfully!");
            System.out.println("=========================================");
            System.out.println("  Open your browser and visit:");
            System.out.println("  http://localhost:8080");
            System.out.println("=========================================");
            System.out.println();
            System.out.println("Press Ctrl+C to stop the server");
            System.out.println();
            
            server.join();
        } catch (Exception e) {
            System.err.println("Error starting server: " + e.getMessage());
            e.printStackTrace();
        } finally {
            if (server != null && server.isStarted()) {
                try {
                    server.stop();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }
}

