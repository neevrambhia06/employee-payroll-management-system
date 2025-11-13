package com.payroll.dao;

import com.payroll.database.DatabaseConnection;
import com.payroll.model.Payroll;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PayrollDAO {
    
    public List<Payroll> getAllPayrolls() {
        List<Payroll> payrolls = new ArrayList<>();
        String sql = "SELECT * FROM payroll ORDER BY created_date DESC";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {
            
            while (rs.next()) {
                Payroll pay = new Payroll();
                pay.setId(rs.getInt("id"));
                pay.setEmployeeId(rs.getString("employee_id"));
                pay.setPayPeriodStart(rs.getString("pay_period_start"));
                pay.setPayPeriodEnd(rs.getString("pay_period_end"));
                pay.setBaseSalary(rs.getDouble("base_salary"));
                pay.setOvertime(rs.getDouble("overtime"));
                pay.setBonus(rs.getDouble("bonus"));
                pay.setDeductions(rs.getDouble("deductions"));
                pay.setNetSalary(rs.getDouble("net_salary"));
                pay.setCreatedDate(rs.getString("created_date"));
                payrolls.add(pay);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return payrolls;
    }
    
    public boolean addPayroll(Payroll payroll) {
        String sql = "INSERT INTO payroll " +
            "(employee_id, pay_period_start, pay_period_end, base_salary, overtime, bonus, deductions, net_salary) " +
            "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, payroll.getEmployeeId());
            pstmt.setString(2, payroll.getPayPeriodStart());
            pstmt.setString(3, payroll.getPayPeriodEnd());
            pstmt.setDouble(4, payroll.getBaseSalary());
            pstmt.setDouble(5, payroll.getOvertime());
            pstmt.setDouble(6, payroll.getBonus());
            pstmt.setDouble(7, payroll.getDeductions());
            pstmt.setDouble(8, payroll.getNetSalary());
            
            pstmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    public List<Payroll> getPayrollByEmployee(String employeeId) {
        List<Payroll> payrolls = new ArrayList<>();
        String sql = "SELECT * FROM payroll WHERE employee_id = ? ORDER BY created_date DESC";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, employeeId);
            ResultSet rs = pstmt.executeQuery();
            
            while (rs.next()) {
                Payroll pay = new Payroll();
                pay.setId(rs.getInt("id"));
                pay.setEmployeeId(rs.getString("employee_id"));
                pay.setPayPeriodStart(rs.getString("pay_period_start"));
                pay.setPayPeriodEnd(rs.getString("pay_period_end"));
                pay.setBaseSalary(rs.getDouble("base_salary"));
                pay.setOvertime(rs.getDouble("overtime"));
                pay.setBonus(rs.getDouble("bonus"));
                pay.setDeductions(rs.getDouble("deductions"));
                pay.setNetSalary(rs.getDouble("net_salary"));
                pay.setCreatedDate(rs.getString("created_date"));
                payrolls.add(pay);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return payrolls;
    }

    public boolean updatePayroll(Payroll payroll) {
        String sql = "UPDATE payroll SET employee_id = ?, pay_period_start = ?, pay_period_end = ?, " +
                     "base_salary = ?, overtime = ?, bonus = ?, deductions = ?, net_salary = ? WHERE id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, payroll.getEmployeeId());
            pstmt.setString(2, payroll.getPayPeriodStart());
            pstmt.setString(3, payroll.getPayPeriodEnd());
            pstmt.setDouble(4, payroll.getBaseSalary());
            pstmt.setDouble(5, payroll.getOvertime());
            pstmt.setDouble(6, payroll.getBonus());
            pstmt.setDouble(7, payroll.getDeductions());
            pstmt.setDouble(8, payroll.getNetSalary());
            pstmt.setInt(9, payroll.getId());

            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean deletePayroll(int id) {
        String sql = "DELETE FROM payroll WHERE id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}

