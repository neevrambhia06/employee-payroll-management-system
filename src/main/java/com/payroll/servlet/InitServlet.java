package com.payroll.servlet;

import com.payroll.database.DatabaseInitializer;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

@WebListener
public class InitServlet implements ServletContextListener {
    
    @Override
    public void contextInitialized(ServletContextEvent event) {
        try {
            Class.forName("org.sqlite.JDBC");
            DatabaseInitializer.initialize();
            System.out.println("Database initialized successfully");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    @Override
    public void contextDestroyed(ServletContextEvent event) {
        // Cleanup if needed
    }
}

