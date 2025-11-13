╔══════════════════════════════════════════════════════════════╗
║                                                              ║
║         EMPLOYEE PAYROLL MANAGEMENT SYSTEM                   ║
║                                                              ║
╚══════════════════════════════════════════════════════════════╝

━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━

                           HOW TO RUN

  For FIRST TIME / NEW USERS:
  Double-click:  START.bat  (recommended)
  
  For SUBSEQUENT RUNS:
  Double-click:  RUN.bat
  
  Then open your browser to: http://localhost:8080
  
  Note: START.bat automatically downloads all dependencies on first run!

━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━

                         REQUIREMENTS

  • Java 8 or higher (Download from java.com)
  • Internet connection (only for first run)

━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━

                          FEATURES

  ✓ Add, Edit, Delete Employees
  ✓ Search (name/ID) + Filter by Department
  ✓ Generate Payroll with automatic calculations
  ✓ Edit Payroll (update amounts/dates)
  ✓ Delete Payroll records
  ✓ Search Payroll by Employee ID
  ✓ Filter Payroll by Date Range
  ✓ Charts & Analytics (Department, Trends, Top Employees, Components)
  ✓ Beautiful Modern UI
  ✓ SQLite Database (auto-created)

━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━

                      TROUBLESHOOTING

  Port 8080 already in use?
  → Close other applications using that port

  Java not found?
  → Install Java from java.com

  Need to reset database?
  → Stop server, delete payroll.db, run RUN.bat

━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━

                    PROJECT STRUCTURE

  src/main/java/com/payroll/
    ├── database/    (Database connection & setup)
    ├── model/       (Employee, Payroll models)
    ├── dao/         (Data access objects)
    └── SimpleServer.java  (Main server + REST API)

  webapp/
    ├── index.html   (UI + Modals + Charts)
    ├── css/style.css
    └── js/app.js    (Logic: CRUD, filters, charts)

━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━

Enjoy your Payroll Management System!

━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━


