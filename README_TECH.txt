======================================================
EMPLOYEE PAYROLL MANAGEMENT SYSTEM - TECH OVERVIEW
======================================================

Have we used Maven?
- No (not required to run this project).
- We initially scaffolded a Maven config but removed it to keep things super simple.
- The app runs with a single command via RUN.bat using plain Java + 2 jars.

Technology Stack
- Language: Java 11+
- Web Server: Java built-in HttpServer (com.sun.net.httpserver)
- Database: SQLite (file: payroll.db)
- JDBC Driver: sqlite-jdbc (org.xerial)
- JSON: Gson (com.google.code.gson)
- Frontend: HTML5, CSS3, Vanilla JavaScript
- Charts: Chart.js (via CDN)

How to Run
1) Double-click RUN.bat
2) Open http://localhost:8080

Key Responsibilities by Directory/File

src/main/java/com/payroll/
- SimpleServer.java
  - Starts the HTTP server on port 8080
  - Initializes SQLite database (via DatabaseInitializer)
  - Serves API endpoints:
    - /api/employees [GET, POST, PUT, DELETE]
    - /api/payroll   [GET, POST, PUT]
  - Serves static files from webapp/

src/main/java/com/payroll/database/
- DatabaseConnection.java
  - Creates and returns a singleton Connection to SQLite (jdbc:sqlite:payroll.db)

- DatabaseInitializer.java
  - Creates tables: employees, departments, payroll, attendance
  - Inserts sample data ONLY when the database is empty
  - Expanded to 30 employees and 90 payroll rows (3 months)

src/main/java/com/payroll/dao/
- EmployeeDAO.java
  - CRUD operations for employees (getAll, getById, add, update, delete)

- PayrollDAO.java
  - Payroll operations (getAll, getByEmployee, add)
  - updatePayroll(payroll) to update existing payroll records

src/main/java/com/payroll/model/
- Employee.java
  - POJO for employee data
- Payroll.java
  - POJO for payroll data

webapp/
- index.html
  - Full UI: sticky navbar, hero landing, employees, payroll, reports
  - Modals for Add/Edit Employee & Add/Edit Payroll
  - Stats + Charts area with 4 charts (Chart.js)

- css/style.css
  - Full theme: glassmorphism, gradients, responsive layout
  - Tables, modals, buttons, charts styling

- js/app.js
  - Navigation & section handling
  - Employee: load, search (name/ID), filter (department), add, edit, delete
  - Payroll: load, search (employee ID), date-range filters, add, edit (PUT)
  - Reports: totals + Chart.js charts (department, trends, top employees, components)

Other top-level files
- RUN.bat
  - Downloads jars if missing
  - Compiles only required Java files (no servlets/maven needed)
  - Copies web assets and starts SimpleServer

- README.txt
  - Simple user-facing run instructions and features

Database File
- payroll.db
  - Created on first run
  - Data persists across restarts
  - To reset: stop server, delete payroll.db, run RUN.bat

Notes
- No external app server needed (Tomcat/Jetty not required)
- No build tool needed (Maven/Gradle not required)
- Everything runs with stock Java + jars
