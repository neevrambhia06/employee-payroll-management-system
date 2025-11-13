package com.payroll.servlet;

import com.google.gson.Gson;
import com.payroll.dao.EmployeeDAO;
import com.payroll.model.Employee;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

@WebServlet("/api/employees/*")
public class EmployeeServlet extends HttpServlet {
    private EmployeeDAO employeeDAO;
    private Gson gson;
    
    @Override
    public void init() throws ServletException {
        employeeDAO = new EmployeeDAO();
        gson = new Gson();
    }
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        PrintWriter out = response.getWriter();
        
        String pathInfo = request.getPathInfo();
        
        if (pathInfo == null || pathInfo.equals("/")) {
            // Get all employees
            out.print(gson.toJson(employeeDAO.getAllEmployees()));
        } else {
            // Get employee by ID
            String employeeId = pathInfo.substring(1);
            Employee emp = employeeDAO.getEmployeeById(employeeId);
            if (emp != null) {
                out.print(gson.toJson(emp));
            } else {
                response.setStatus(HttpServletResponse.SC_NOT_FOUND);
                out.print("{\"error\":\"Employee not found\"}");
            }
        }
        out.flush();
    }
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        PrintWriter out = response.getWriter();
        
        try {
            Employee emp = gson.fromJson(request.getReader(), Employee.class);
            
            if (employeeDAO.addEmployee(emp)) {
                response.setStatus(HttpServletResponse.SC_CREATED);
                out.print("{\"message\":\"Employee added successfully\"}");
            } else {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                out.print("{\"error\":\"Failed to add employee\"}");
            }
        } catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            out.print("{\"error\":\"" + e.getMessage() + "\"}");
        }
        out.flush();
    }
    
    @Override
    protected void doPut(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        PrintWriter out = response.getWriter();
        
        try {
            Employee emp = gson.fromJson(request.getReader(), Employee.class);
            
            if (employeeDAO.updateEmployee(emp)) {
                out.print("{\"message\":\"Employee updated successfully\"}");
            } else {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                out.print("{\"error\":\"Failed to update employee\"}");
            }
        } catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            out.print("{\"error\":\"" + e.getMessage() + "\"}");
        }
        out.flush();
    }
    
    @Override
    protected void doDelete(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        PrintWriter out = response.getWriter();
        
        String pathInfo = request.getPathInfo();
        if (pathInfo == null || pathInfo.equals("/")) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            out.print("{\"error\":\"Employee ID required\"}");
        } else {
            String employeeId = pathInfo.substring(1);
            if (employeeDAO.deleteEmployee(employeeId)) {
                out.print("{\"message\":\"Employee deleted successfully\"}");
            } else {
                response.setStatus(HttpServletResponse.SC_NOT_FOUND);
                out.print("{\"error\":\"Failed to delete employee\"}");
            }
        }
        out.flush();
    }
}

