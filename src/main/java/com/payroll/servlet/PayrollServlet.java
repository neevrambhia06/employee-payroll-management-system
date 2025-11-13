package com.payroll.servlet;

import com.google.gson.Gson;
import com.payroll.dao.PayrollDAO;
import com.payroll.model.Payroll;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

@WebServlet("/api/payroll/*")
public class PayrollServlet extends HttpServlet {
    private PayrollDAO payrollDAO;
    private Gson gson;
    
    @Override
    public void init() throws ServletException {
        payrollDAO = new PayrollDAO();
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
            // Get all payrolls
            out.print(gson.toJson(payrollDAO.getAllPayrolls()));
        } else {
            // Get payroll by employee ID
            String employeeId = pathInfo.substring(1);
            out.print(gson.toJson(payrollDAO.getPayrollByEmployee(employeeId)));
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
            Payroll payroll = gson.fromJson(request.getReader(), Payroll.class);
            
            // Calculate net salary
            double netSalary = payroll.getBaseSalary() + payroll.getOvertime() + 
                             payroll.getBonus() - payroll.getDeductions();
            payroll.setNetSalary(netSalary);
            
            if (payrollDAO.addPayroll(payroll)) {
                response.setStatus(HttpServletResponse.SC_CREATED);
                out.print("{\"message\":\"Payroll added successfully\"}");
            } else {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                out.print("{\"error\":\"Failed to add payroll\"}");
            }
        } catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            out.print("{\"error\":\"" + e.getMessage() + "\"}");
        }
        out.flush();
    }
}

