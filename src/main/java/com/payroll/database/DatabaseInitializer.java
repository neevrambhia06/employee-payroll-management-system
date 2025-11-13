package com.payroll.database;

import java.sql.Connection;
import java.sql.Statement;

public class DatabaseInitializer {
    
    public static void initialize() {
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement()) {
            
            // Employee table
            stmt.execute("CREATE TABLE IF NOT EXISTS employees (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "employee_id TEXT UNIQUE NOT NULL," +
                "first_name TEXT NOT NULL," +
                "last_name TEXT NOT NULL," +
                "email TEXT," +
                "phone TEXT," +
                "department TEXT," +
                "position TEXT," +
                "base_salary REAL," +
                "hire_date TEXT," +
                "status TEXT DEFAULT 'active'" +
                ")");
            
            // Departments table
            stmt.execute("CREATE TABLE IF NOT EXISTS departments (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "name TEXT UNIQUE NOT NULL," +
                "description TEXT" +
                ")");
            
            // Payroll table
            stmt.execute("CREATE TABLE IF NOT EXISTS payroll (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "employee_id TEXT NOT NULL," +
                "pay_period_start TEXT," +
                "pay_period_end TEXT," +
                "base_salary REAL," +
                "overtime REAL DEFAULT 0," +
                "bonus REAL DEFAULT 0," +
                "deductions REAL DEFAULT 0," +
                "net_salary REAL," +
                "created_date TEXT DEFAULT CURRENT_TIMESTAMP," +
                "FOREIGN KEY (employee_id) REFERENCES employees(employee_id)" +
                ")");
            
            // Attendance table
            stmt.execute("CREATE TABLE IF NOT EXISTS attendance (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "employee_id TEXT NOT NULL," +
                "date TEXT NOT NULL," +
                "check_in TEXT," +
                "check_out TEXT," +
                "hours_worked REAL DEFAULT 0," +
                "status TEXT DEFAULT 'present'," +
                "FOREIGN KEY (employee_id) REFERENCES employees(employee_id)" +
                ")");
            
            // Insert sample data
            insertSampleData(stmt);
            
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    private static void insertSampleData(Statement stmt) {
        try {
            // Check if employees already exist
            var rs = stmt.executeQuery("SELECT COUNT(*) as count FROM employees");
            rs.next();
            int count = rs.getInt("count");
            rs.close();
            
            // Only insert sample data if database is empty
            if (count == 0) {
                // Insert sample departments
                stmt.execute("INSERT INTO departments (name, description) VALUES " +
                    "('HR', 'Human Resources'), " +
                    "('IT', 'Information Technology'), " +
                    "('Finance', 'Finance Department'), " +
                    "('Sales', 'Sales Department'), " +
                    "('Marketing', 'Marketing Department'), " +
                    "('Operations', 'Operations Department')");
                
                // Insert sample employees
                stmt.execute("INSERT INTO employees " +
                    "(employee_id, first_name, last_name, email, phone, department, position, base_salary, hire_date) VALUES " +
                    "('EMP001', 'John', 'Doe', 'john.doe@company.com', '123-456-7890', 'IT', 'Software Engineer', 75000, '2023-01-15'), " +
                    "('EMP002', 'Jane', 'Smith', 'jane.smith@company.com', '123-456-7891', 'HR', 'HR Manager', 65000, '2023-02-01'), " +
                    "('EMP003', 'Mike', 'Johnson', 'mike.johnson@company.com', '123-456-7892', 'Finance', 'Accountant', 60000, '2023-03-10'), " +
                    "('EMP004', 'Sarah', 'Williams', 'sarah.williams@company.com', '123-456-7893', 'IT', 'Senior Developer', 95000, '2023-04-05'), " +
                    "('EMP005', 'David', 'Brown', 'david.brown@company.com', '123-456-7894', 'Sales', 'Sales Manager', 55000, '2023-05-12'), " +
                    "('EMP006', 'Emily', 'Davis', 'emily.davis@company.com', '123-456-7895', 'Marketing', 'Marketing Lead', 70000, '2023-06-20'), " +
                    "('EMP007', 'Robert', 'Miller', 'robert.miller@company.com', '123-456-7896', 'IT', 'DevOps Engineer', 80000, '2023-07-08'), " +
                    "('EMP008', 'Lisa', 'Wilson', 'lisa.wilson@company.com', '123-456-7897', 'Finance', 'Financial Analyst', 68000, '2023-08-15'), " +
                    "('EMP009', 'James', 'Moore', 'james.moore@company.com', '123-456-7898', 'Sales', 'Sales Executive', 52000, '2023-09-22'), " +
                    "('EMP010', 'Patricia', 'Taylor', 'patricia.taylor@company.com', '123-456-7899', 'Marketing', 'Content Manager', 66000, '2023-10-10'), " +
                    "('EMP011', 'Michael', 'Anderson', 'michael.anderson@company.com', '123-456-7900', 'IT', 'Full Stack Developer', 78000, '2023-11-05'), " +
                    "('EMP012', 'Jennifer', 'Thomas', 'jennifer.thomas@company.com', '123-456-7901', 'HR', 'Recruiter', 58000, '2023-11-18'), " +
                    "('EMP013', 'Christopher', 'Martinez', 'christopher.martinez@company.com', '123-456-7902', 'Finance', 'Tax Specialist', 72000, '2023-12-01'), " +
                    "('EMP014', 'Michelle', 'Rodriguez', 'michelle.rodriguez@company.com', '123-456-7903', 'Sales', 'Account Executive', 64000, '2024-01-08'), " +
                    "('EMP015', 'Daniel', 'Lee', 'daniel.lee@company.com', '123-456-7904', 'Marketing', 'Brand Manager', 73000, '2024-01-22'), " +
                    "('EMP016', 'Ashley', 'White', 'ashley.white@company.com', '123-456-7905', 'IT', 'Data Engineer', 87000, '2024-02-05'), " +
                    "('EMP017', 'Matthew', 'Harris', 'matthew.harris@company.com', '123-456-7906', 'HR', 'Compensation Analyst', 69000, '2024-02-18'), " +
                    "('EMP018', 'Jessica', 'Clark', 'jessica.clark@company.com', '123-456-7907', 'Finance', 'Financial Controller', 94000, '2024-03-01'), " +
                    "('EMP019', 'Joshua', 'Lewis', 'joshua.lewis@company.com', '123-456-7908', 'Sales', 'Business Development', 59000, '2024-03-15'), " +
                    "('EMP020', 'Amanda', 'Robinson', 'amanda.robinson@company.com', '123-456-7909', 'Marketing', 'Digital Strategist', 76000, '2024-03-28'), " +
                    "('EMP021', 'Andrew', 'Walker', 'andrew.walker@company.com', '123-456-7910', 'IT', 'Security Analyst', 82000, '2024-04-10'), " +
                    "('EMP022', 'Stephanie', 'Young', 'stephanie.young@company.com', '123-456-7911', 'HR', 'Training Specialist', 60000, '2024-04-23'), " +
                    "('EMP023', 'Ryan', 'King', 'ryan.king@company.com', '123-456-7912', 'Finance', 'Investment Analyst', 85000, '2024-05-05'), " +
                    "('EMP024', 'Nicole', 'Wright', 'nicole.wright@company.com', '123-456-7913', 'Sales', 'Territory Manager', 67000, '2024-05-18'), " +
                    "('EMP025', 'Brandon', 'Lopez', 'brandon.lopez@company.com', '123-456-7914', 'Marketing', 'Social Media Manager', 62000, '2024-06-01'), " +
                    "('EMP026', 'Rebecca', 'Hill', 'rebecca.hill@company.com', '123-456-7915', 'IT', 'Cloud Architect', 105000, '2024-06-14'), " +
                    "('EMP027', 'Jonathan', 'Scott', 'jonathan.scott@company.com', '123-456-7916', 'HR', 'Employee Relations', 71000, '2024-06-27'), " +
                    "('EMP028', 'Lauren', 'Green', 'lauren.green@company.com', '123-456-7917', 'Finance', 'Budget Analyst', 68000, '2024-07-10'), " +
                    "('EMP029', 'Nicholas', 'Adams', 'nicholas.adams@company.com', '123-456-7918', 'Sales', 'Channel Manager', 74000, '2024-07-23'), " +
                    "('EMP030', 'Samantha', 'Baker', 'samantha.baker@company.com', '123-456-7919', 'Marketing', 'Campaign Manager', 75000, '2024-08-05')");
                
                // Insert sample payroll data for 30 employees across 3 months
                stmt.execute("INSERT INTO payroll " +
                    "(employee_id, pay_period_start, pay_period_end, base_salary, overtime, bonus, deductions, net_salary) VALUES " +
                    // January 2024
                    "('EMP001', '2024-01-01', '2024-01-31', 75000, 2500, 5000, 8000, 74500), " +
                    "('EMP002', '2024-01-01', '2024-01-31', 65000, 1500, 3000, 6500, 63000), " +
                    "('EMP003', '2024-01-01', '2024-01-31', 60000, 1200, 2000, 6000, 57200), " +
                    "('EMP004', '2024-01-01', '2024-01-31', 95000, 4000, 8000, 9500, 97500), " +
                    "('EMP005', '2024-01-01', '2024-01-31', 55000, 1800, 5000, 5500, 59300), " +
                    "('EMP006', '2024-01-01', '2024-01-31', 70000, 2200, 6000, 7000, 71200), " +
                    "('EMP007', '2024-01-01', '2024-01-31', 80000, 3500, 7000, 8000, 82500), " +
                    "('EMP008', '2024-01-01', '2024-01-31', 68000, 1600, 4000, 6800, 66800), " +
                    "('EMP009', '2024-01-01', '2024-01-31', 52000, 1400, 4500, 5200, 52700), " +
                    "('EMP010', '2024-01-01', '2024-01-31', 66000, 2000, 5500, 6600, 66900), " +
                    "('EMP011', '2024-01-01', '2024-01-31', 78000, 2800, 6000, 7800, 79000), " +
                    "('EMP012', '2024-01-01', '2024-01-31', 58000, 1600, 3500, 5800, 58800), " +
                    "('EMP013', '2024-01-01', '2024-01-31', 72000, 2400, 5500, 7200, 72700), " +
                    "('EMP014', '2024-01-01', '2024-01-31', 64000, 1900, 4800, 6400, 64300), " +
                    "('EMP015', '2024-01-01', '2024-01-31', 73000, 2600, 6200, 7300, 74600), " +
                    "('EMP016', '2024-01-01', '2024-01-31', 87000, 3800, 7500, 8700, 89600), " +
                    "('EMP017', '2024-01-01', '2024-01-31', 69000, 2100, 5200, 6900, 69400), " +
                    "('EMP018', '2024-01-01', '2024-01-31', 94000, 4200, 8800, 9400, 97600), " +
                    "('EMP019', '2024-01-01', '2024-01-31', 59000, 1700, 4400, 5900, 59200), " +
                    "('EMP020', '2024-01-01', '2024-01-31', 76000, 3000, 6800, 7600, 77400), " +
                    "('EMP021', '2024-01-01', '2024-01-31', 82000, 3400, 7100, 8200, 84500), " +
                    "('EMP022', '2024-01-01', '2024-01-31', 60000, 1800, 4100, 6000, 59900), " +
                    "('EMP023', '2024-01-01', '2024-01-31', 85000, 3600, 7800, 8500, 88200), " +
                    "('EMP024', '2024-01-01', '2024-01-31', 67000, 2200, 5600, 6700, 68100), " +
                    "('EMP025', '2024-01-01', '2024-01-31', 62000, 2000, 4700, 6200, 62500), " +
                    "('EMP026', '2024-01-01', '2024-01-31', 105000, 4800, 10000, 10500, 109300), " +
                    "('EMP027', '2024-01-01', '2024-01-31', 71000, 2300, 5900, 7100, 71900), " +
                    "('EMP028', '2024-01-01', '2024-01-31', 68000, 2100, 5400, 6800, 68700), " +
                    "('EMP029', '2024-01-01', '2024-01-31', 74000, 2700, 6500, 7400, 74700), " +
                    "('EMP030', '2024-01-01', '2024-01-31', 75000, 2900, 6700, 7500, 76300), " +
                    // February 2024
                    "('EMP001', '2024-02-01', '2024-02-28', 75000, 2800, 5200, 8000, 75000), " +
                    "('EMP002', '2024-02-01', '2024-02-28', 65000, 1600, 3200, 6500, 63300), " +
                    "('EMP003', '2024-02-01', '2024-02-28', 60000, 1300, 2100, 6000, 57400), " +
                    "('EMP004', '2024-02-01', '2024-02-28', 95000, 4200, 8500, 9500, 98200), " +
                    "('EMP005', '2024-02-01', '2024-02-28', 55000, 1900, 5100, 5500, 56500), " +
                    "('EMP006', '2024-02-01', '2024-02-28', 70000, 2300, 6100, 7000, 71400), " +
                    "('EMP007', '2024-02-01', '2024-02-28', 80000, 3600, 7200, 8000, 82800), " +
                    "('EMP008', '2024-02-01', '2024-02-28', 68000, 1700, 4100, 6800, 67000), " +
                    "('EMP009', '2024-02-01', '2024-02-28', 52000, 1500, 4600, 5200, 53900), " +
                    "('EMP010', '2024-02-01', '2024-02-28', 66000, 2100, 5600, 6600, 67100), " +
                    "('EMP011', '2024-02-01', '2024-02-28', 78000, 2900, 6100, 7800, 79300), " +
                    "('EMP012', '2024-02-01', '2024-02-28', 58000, 1700, 3600, 5800, 58900), " +
                    "('EMP013', '2024-02-01', '2024-02-28', 72000, 2500, 5600, 7200, 72900), " +
                    "('EMP014', '2024-02-01', '2024-02-28', 64000, 2000, 4900, 6400, 65500), " +
                    "('EMP015', '2024-02-01', '2024-02-28', 73000, 2700, 6300, 7300, 74700), " +
                    "('EMP016', '2024-02-01', '2024-02-28', 87000, 3900, 7600, 8700, 90100), " +
                    "('EMP017', '2024-02-01', '2024-02-28', 69000, 2200, 5300, 6900, 69400), " +
                    "('EMP018', '2024-02-01', '2024-02-28', 94000, 4300, 8900, 9400, 97800), " +
                    "('EMP019', '2024-02-01', '2024-02-28', 59000, 1800, 4500, 5900, 59400), " +
                    "('EMP020', '2024-02-01', '2024-02-28', 76000, 3100, 6900, 7600, 77500), " +
                    "('EMP021', '2024-02-01', '2024-02-28', 82000, 3500, 7200, 8200, 84700), " +
                    "('EMP022', '2024-02-01', '2024-02-28', 60000, 1900, 4200, 6000, 60100), " +
                    "('EMP023', '2024-02-01', '2024-02-28', 85000, 3700, 7900, 8500, 88500), " +
                    "('EMP024', '2024-02-01', '2024-02-28', 67000, 2300, 5700, 6700, 68300), " +
                    "('EMP025', '2024-02-01', '2024-02-28', 62000, 2100, 4800, 6200, 62700), " +
                    "('EMP026', '2024-02-01', '2024-02-28', 105000, 4900, 10100, 10500, 110100), " +
                    "('EMP027', '2024-02-01', '2024-02-28', 71000, 2400, 6000, 7100, 72000), " +
                    "('EMP028', '2024-02-01', '2024-02-28', 68000, 2200, 5500, 6800, 68900), " +
                    "('EMP029', '2024-02-01', '2024-02-28', 74000, 2800, 6600, 7400, 75000), " +
                    "('EMP030', '2024-02-01', '2024-02-28', 75000, 3000, 6800, 7500, 76500), " +
                    // March 2024
                    "('EMP001', '2024-03-01', '2024-03-31', 75000, 2900, 5400, 8000, 75300), " +
                    "('EMP002', '2024-03-01', '2024-03-31', 65000, 1700, 3300, 6500, 63500), " +
                    "('EMP003', '2024-03-01', '2024-03-31', 60000, 1400, 2200, 6000, 57600), " +
                    "('EMP004', '2024-03-01', '2024-03-31', 95000, 4400, 8700, 9500, 98600), " +
                    "('EMP005', '2024-03-01', '2024-03-31', 55000, 2000, 5200, 5500, 56700), " +
                    "('EMP006', '2024-03-01', '2024-03-31', 70000, 2400, 6200, 7000, 71600), " +
                    "('EMP007', '2024-03-01', '2024-03-31', 80000, 3700, 7300, 8000, 83000), " +
                    "('EMP008', '2024-03-01', '2024-03-31', 68000, 1800, 4200, 6800, 67200), " +
                    "('EMP009', '2024-03-01', '2024-03-31', 52000, 1600, 4700, 5200, 54100), " +
                    "('EMP010', '2024-03-01', '2024-03-31', 66000, 2200, 5700, 6600, 67300), " +
                    "('EMP011', '2024-03-01', '2024-03-31', 78000, 3000, 6200, 7800, 79600), " +
                    "('EMP012', '2024-03-01', '2024-03-31', 58000, 1800, 3700, 5800, 59000), " +
                    "('EMP013', '2024-03-01', '2024-03-31', 72000, 2600, 5700, 7200, 73100), " +
                    "('EMP014', '2024-03-01', '2024-03-31', 64000, 2100, 5000, 6400, 65700), " +
                    "('EMP015', '2024-03-01', '2024-03-31', 73000, 2800, 6400, 7300, 74900), " +
                    "('EMP016', '2024-03-01', '2024-03-31', 87000, 4000, 7700, 8700, 90300), " +
                    "('EMP017', '2024-03-01', '2024-03-31', 69000, 2300, 5400, 6900, 69600), " +
                    "('EMP018', '2024-03-01', '2024-03-31', 94000, 4400, 9000, 9400, 98000), " +
                    "('EMP019', '2024-03-01', '2024-03-31', 59000, 1900, 4600, 5900, 59600), " +
                    "('EMP020', '2024-03-01', '2024-03-31', 76000, 3200, 7000, 7600, 77800), " +
                    "('EMP021', '2024-03-01', '2024-03-31', 82000, 3600, 7300, 8200, 84900), " +
                    "('EMP022', '2024-03-01', '2024-03-31', 60000, 2000, 4300, 6000, 60300), " +
                    "('EMP023', '2024-03-01', '2024-03-31', 85000, 3800, 8000, 8500, 88800), " +
                    "('EMP024', '2024-03-01', '2024-03-31', 67000, 2400, 5800, 6700, 68500), " +
                    "('EMP025', '2024-03-01', '2024-03-31', 62000, 2200, 4900, 6200, 62900), " +
                    "('EMP026', '2024-03-01', '2024-03-31', 105000, 5000, 10200, 10500, 110700), " +
                    "('EMP027', '2024-03-01', '2024-03-31', 71000, 2500, 6100, 7100, 72100), " +
                    "('EMP028', '2024-03-01', '2024-03-31', 68000, 2300, 5600, 6800, 69100), " +
                    "('EMP029', '2024-03-01', '2024-03-31', 74000, 2900, 6700, 7400, 75300), " +
                    "('EMP030', '2024-03-01', '2024-03-31', 75000, 3100, 6900, 7500, 76700)");
            }
        } catch (Exception e) {
            // Ignore if data already exists
        }
    }
}

