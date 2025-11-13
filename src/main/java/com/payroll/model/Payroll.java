package com.payroll.model;

public class Payroll {
    private int id;
    private String employeeId;
    private String payPeriodStart;
    private String payPeriodEnd;
    private double baseSalary;
    private double overtime;
    private double bonus;
    private double deductions;
    private double netSalary;
    private String createdDate;
    
    public Payroll() {}
    
    // Getters and Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    
    public String getEmployeeId() { return employeeId; }
    public void setEmployeeId(String employeeId) { this.employeeId = employeeId; }
    
    public String getPayPeriodStart() { return payPeriodStart; }
    public void setPayPeriodStart(String payPeriodStart) { this.payPeriodStart = payPeriodStart; }
    
    public String getPayPeriodEnd() { return payPeriodEnd; }
    public void setPayPeriodEnd(String payPeriodEnd) { this.payPeriodEnd = payPeriodEnd; }
    
    public double getBaseSalary() { return baseSalary; }
    public void setBaseSalary(double baseSalary) { this.baseSalary = baseSalary; }
    
    public double getOvertime() { return overtime; }
    public void setOvertime(double overtime) { this.overtime = overtime; }
    
    public double getBonus() { return bonus; }
    public void setBonus(double bonus) { this.bonus = bonus; }
    
    public double getDeductions() { return deductions; }
    public void setDeductions(double deductions) { this.deductions = deductions; }
    
    public double getNetSalary() { return netSalary; }
    public void setNetSalary(double netSalary) { this.netSalary = netSalary; }
    
    public String getCreatedDate() { return createdDate; }
    public void setCreatedDate(String createdDate) { this.createdDate = createdDate; }
}

