const API_BASE = window.location.origin;

// Keep last-loaded data to filter client-side
let EMPLOYEES_CACHE = [];
let PAYROLLS_CACHE = [];
let CURRENT_EDIT_PAYROLL_ID = null;

// Navigation
function showSection(sectionId) {
    // Hide all sections including home
    document.querySelectorAll('.section').forEach(section => {
        section.classList.remove('active');
    });
    document.querySelectorAll('.nav-btn').forEach(btn => {
        btn.classList.remove('active');
    });
    
    // Show the selected section
    const targetSection = document.getElementById(sectionId);
    if (targetSection) {
        targetSection.classList.add('active');
    }
    
    // Update nav button state
    if (event && event.target) {
        event.target.classList.add('active');
    }
    
    // Show container if not home
    const container = document.querySelector('.container');
    if (sectionId === 'home') {
        if (container) container.style.display = 'none';
    } else {
        if (container) container.style.display = 'block';
    }
}

// Render employees to table
function renderEmployees(employees) {
    const tbody = document.getElementById('employeeTableBody');
    if (!tbody) return;
    tbody.innerHTML = '';
    employees.forEach(emp => {
        const row = document.createElement('tr');
        row.innerHTML = `
            <td>${emp.employeeId}</td>
            <td>${emp.firstName} ${emp.lastName}</td>
            <td>${emp.email}</td>
            <td>${emp.department}</td>
            <td>${emp.position}</td>
            <td>₹${Number(emp.baseSalary || 0).toFixed(2)}</td>
            <td>
                <button class="btn btn-edit" onclick="editEmployee('${emp.employeeId}')">Edit</button>
                <button class="btn btn-danger" onclick="deleteEmployee('${emp.employeeId}')">Delete</button>
            </td>
        `;
        tbody.appendChild(row);
    });
}

// Apply employee filters
function applyEmployeeFilters() {
    let filtered = [...EMPLOYEES_CACHE];
    const q = (document.getElementById('employeeSearch')?.value || '').trim().toLowerCase();
    const dept = (document.getElementById('deptFilter')?.value || '').trim();

    if (q) {
        filtered = filtered.filter(e => (
            (e.firstName + ' ' + e.lastName).toLowerCase().includes(q) ||
            (e.employeeId || '').toLowerCase().includes(q)
        ));
    }
    if (dept) {
        filtered = filtered.filter(e => (e.department || '') === dept);
    }

    renderEmployees(filtered);
}

// Render payrolls
function renderPayroll(payrolls) {
    const tbody = document.getElementById('payrollTableBody');
    if (!tbody) return;
    tbody.innerHTML = '';
    payrolls.forEach(pay => {
        const row = document.createElement('tr');
        row.innerHTML = `
            <td>${pay.employeeId}</td>
            <td>${pay.payPeriodStart}</td>
            <td>${pay.payPeriodEnd}</td>
            <td>₹${Number(pay.baseSalary || 0).toFixed(2)}</td>
            <td>₹${Number(pay.overtime || 0).toFixed(2)}</td>
            <td>₹${Number(pay.bonus || 0).toFixed(2)}</td>
            <td>₹${Number(pay.deductions || 0).toFixed(2)}</td>
            <td>₹${Number(pay.netSalary || 0).toFixed(2)}</td>
            <td>
                <button class="btn btn-edit" onclick="editPayroll(${pay.id})">Edit</button>
                <button class="btn btn-danger" onclick="deletePayroll(${pay.id})">Delete</button>
            </td>
        `;
        tbody.appendChild(row);
    });
}

// Apply payroll filters
function applyPayrollFilters() {
    let filtered = [...PAYROLLS_CACHE];
    const q = (document.getElementById('payrollSearch')?.value || '').trim().toLowerCase();
    const start = (document.getElementById('payrollStart')?.value || '').trim();
    const end = (document.getElementById('payrollEnd')?.value || '').trim();

    if (q) {
        filtered = filtered.filter(p => (p.employeeId || '').toLowerCase().includes(q));
    }
    if (start) {
        filtered = filtered.filter(p => (p.payPeriodStart || '') >= start);
    }
    if (end) {
        filtered = filtered.filter(p => (p.payPeriodEnd || '') <= end);
    }

    renderPayroll(filtered);
}

// Load employees
async function loadEmployees() {
    try {
        const response = await fetch(`${API_BASE}/api/employees/`);
        const employees = await response.json();
        EMPLOYEES_CACHE = employees || [];
        renderEmployees(EMPLOYEES_CACHE);

        // Wire up filters (once)
        const searchEl = document.getElementById('employeeSearch');
        const deptEl = document.getElementById('deptFilter');
        if (searchEl && !searchEl._wired) {
            searchEl.addEventListener('input', applyEmployeeFilters);
            searchEl._wired = true;
        }
        if (deptEl && !deptEl._wired) {
            deptEl.addEventListener('change', applyEmployeeFilters);
            deptEl._wired = true;
        }
    } catch (error) {
        console.error('Error loading employees:', error);
    }
}

// Load payroll
async function loadPayroll() {
    try {
        const response = await fetch(`${API_BASE}/api/payroll/`);
        const payrolls = await response.json();
        PAYROLLS_CACHE = payrolls || [];
        renderPayroll(PAYROLLS_CACHE);

        // Wire filters
        const s = document.getElementById('payrollSearch');
        const ds = document.getElementById('payrollStart');
        const de = document.getElementById('payrollEnd');
        if (s && !s._wired) { s.addEventListener('input', applyPayrollFilters); s._wired = true; }
        if (ds && !ds._wired) { ds.addEventListener('change', applyPayrollFilters); ds._wired = true; }
        if (de && !de._wired) { de.addEventListener('change', applyPayrollFilters); de._wired = true; }
    } catch (error) {
        console.error('Error loading payroll:', error);
    }
}

// Edit payroll
function editPayroll(id) {
    const p = PAYROLLS_CACHE.find(x => x.id === id);
    if (!p) return;

    document.getElementById('payrollEmpId').value = p.employeeId;
    document.getElementById('periodStart').value = p.payPeriodStart;
    document.getElementById('periodEnd').value = p.payPeriodEnd;
    document.getElementById('payrollBaseSalary').value = p.baseSalary;
    document.getElementById('overtime').value = p.overtime;
    document.getElementById('bonus').value = p.bonus;
    document.getElementById('deductions').value = p.deductions;

    CURRENT_EDIT_PAYROLL_ID = id;
    document.getElementById('payrollModal').style.display = 'block';
}

// Load reports
async function loadReports() {
    try {
        const [employeesResponse, payrollResponse] = await Promise.all([
            fetch(`${API_BASE}/api/employees/`),
            fetch(`${API_BASE}/api/payroll/`)
        ]);
        
        const employees = await employeesResponse.json();
        const payrolls = await payrollResponse.json();
        
        document.getElementById('totalEmployees').textContent = employees.length;
        document.getElementById('totalPayrolls').textContent = payrolls.length;
        
        const totalAmount = payrolls.reduce((sum, pay) => sum + pay.netSalary, 0);
        document.getElementById('totalPayrollAmount').textContent = `₹${totalAmount.toFixed(2)}`;
        
        // Create charts
        createDepartmentChart(employees, payrolls);
        createTrendChart(payrolls);
        createEmployeeChart(employees, payrolls);
        createComponentChart(payrolls);
    } catch (error) {
        console.error('Error loading reports:', error);
    }
}

// Create Department Chart
function createDepartmentChart(employees, payrolls) {
    const deptTotals = {};
    
    payrolls.forEach(pay => {
        const emp = employees.find(e => e.employeeId === pay.employeeId);
        if (emp && emp.department) {
            deptTotals[emp.department] = (deptTotals[emp.department] || 0) + pay.netSalary;
        }
    });
    
    const ctx = document.getElementById('deptChart');
    if (ctx && typeof Chart !== 'undefined') {
        new Chart(ctx, {
            type: 'bar',
            data: {
                labels: Object.keys(deptTotals),
                datasets: [{
                    label: 'Payroll Amount (₹)',
                    data: Object.values(deptTotals),
                    backgroundColor: [
                        'rgba(79, 70, 229, 0.8)',
                        'rgba(147, 51, 234, 0.8)',
                        'rgba(0, 212, 255, 0.8)',
                        'rgba(34, 197, 94, 0.8)',
                        'rgba(251, 146, 60, 0.8)',
                        'rgba(239, 68, 68, 0.8)'
                    ],
                    borderRadius: 8
                }]
            },
            options: {
                responsive: true,
                maintainAspectRatio: true,
                plugins: {
                    legend: {
                        display: false
                    }
                }
            }
        });
    }
}

// Create Trend Chart
function createTrendChart(payrolls) {
    const monthlyTotals = {};
    
    payrolls.forEach(pay => {
        const month = pay.payPeriodStart ? pay.payPeriodStart.substring(0, 7) : 'Unknown';
        monthlyTotals[month] = (monthlyTotals[month] || 0) + pay.netSalary;
    });
    
    const ctx = document.getElementById('trendChart');
    if (ctx && typeof Chart !== 'undefined') {
        new Chart(ctx, {
            type: 'line',
            data: {
                labels: Object.keys(monthlyTotals).sort(),
                datasets: [{
                    label: 'Monthly Payroll (₹)',
                    data: Object.values(monthlyTotals).sort((a, b) => a - b),
                    borderColor: 'rgba(79, 70, 229, 1)',
                    backgroundColor: 'rgba(79, 70, 229, 0.1)',
                    tension: 0.4,
                    fill: true
                }]
            },
            options: {
                responsive: true,
                maintainAspectRatio: true,
                plugins: {
                    legend: {
                        display: false
                    }
                }
            }
        });
    }
}

// Create Employee Chart
function createEmployeeChart(employees, payrolls) {
    const empTotals = {};
    
    payrolls.forEach(pay => {
        const emp = employees.find(e => e.employeeId === pay.employeeId);
        if (emp) {
            empTotals[`${emp.firstName} ${emp.lastName}`] = (empTotals[`${emp.firstName} ${emp.lastName}`] || 0) + pay.netSalary;
        }
    });
    
    const sortedEmps = Object.entries(empTotals)
        .sort((a, b) => b[1] - a[1])
        .slice(0, 5);
    
    const ctx = document.getElementById('employeeChart');
    if (ctx && typeof Chart !== 'undefined') {
        new Chart(ctx, {
            type: 'bar',
            data: {
                labels: sortedEmps.map(e => e[0]),
                datasets: [{
                    label: 'Total Payroll (₹)',
                    data: sortedEmps.map(e => e[1]),
                    backgroundColor: 'rgba(147, 51, 234, 0.8)',
                    borderRadius: 8
                }]
            },
            options: {
                responsive: true,
                maintainAspectRatio: true,
                indexAxis: 'y',
                plugins: {
                    legend: {
                        display: false
                    }
                }
            }
        });
    }
}

// Create Component Chart
function createComponentChart(payrolls) {
    const totals = {
        'Base Salary': 0,
        'Overtime': 0,
        'Bonus': 0,
        'Deductions': 0
    };
    
    payrolls.forEach(pay => {
        totals['Base Salary'] += pay.baseSalary;
        totals['Overtime'] += pay.overtime;
        totals['Bonus'] += pay.bonus;
        totals['Deductions'] += pay.deductions;
    });
    
    const ctx = document.getElementById('componentChart');
    if (ctx && typeof Chart !== 'undefined') {
        new Chart(ctx, {
            type: 'doughnut',
            data: {
                labels: Object.keys(totals),
                datasets: [{
                    data: Object.values(totals),
                    backgroundColor: [
                        'rgba(79, 70, 229, 0.8)',
                        'rgba(147, 51, 234, 0.8)',
                        'rgba(0, 212, 255, 0.8)',
                        'rgba(239, 68, 68, 0.8)'
                    ]
                }]
            },
            options: {
                responsive: true,
                maintainAspectRatio: true,
                plugins: {
                    legend: {
                        position: 'bottom'
                    }
                }
            }
        });
    }
}

// Modal functions
function showAddEmployeeModal() {
    document.getElementById('employeeModal').style.display = 'block';
    document.getElementById('employeeForm').reset();
}

function closeModal(modalId) {
    document.getElementById(modalId).style.display = 'none';
}

function showAddPayrollModal() {
    CURRENT_EDIT_PAYROLL_ID = null;
    document.getElementById('payrollModal').style.display = 'block';
    document.getElementById('payrollForm').reset();
}

// Handle employee submit
async function handleEmployeeSubmit(event) {
    event.preventDefault();
    
    const employee = {
        employeeId: document.getElementById('empId').value,
        firstName: document.getElementById('firstName').value,
        lastName: document.getElementById('lastName').value,
        email: document.getElementById('email').value,
        phone: document.getElementById('phone').value,
        department: document.getElementById('department').value,
        position: document.getElementById('position').value,
        baseSalary: parseFloat(document.getElementById('baseSalary').value),
        hireDate: document.getElementById('hireDate').value,
        status: 'active'
    };
    
    try {
        const response = await fetch(`${API_BASE}/api/employees/`, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(employee)
        });
        
        if (response.ok) {
            alert('Employee added successfully!');
            closeModal('employeeModal');
            loadEmployees();
        } else {
            const error = await response.json();
            alert('Error: ' + (error.error || 'Failed to add employee'));
        }
    } catch (error) {
        alert('Error: ' + error.message);
    }
}

// Handle payroll submit (create or update)
async function handlePayrollSubmit(event) {
    event.preventDefault();
    
    const payroll = {
        id: CURRENT_EDIT_PAYROLL_ID || 0,
        employeeId: document.getElementById('payrollEmpId').value,
        payPeriodStart: document.getElementById('periodStart').value,
        payPeriodEnd: document.getElementById('periodEnd').value,
        baseSalary: parseFloat(document.getElementById('payrollBaseSalary').value),
        overtime: parseFloat(document.getElementById('overtime').value),
        bonus: parseFloat(document.getElementById('bonus').value),
        deductions: parseFloat(document.getElementById('deductions').value)
    };
    
    try {
        const isUpdate = !!CURRENT_EDIT_PAYROLL_ID;
        const response = await fetch(`${API_BASE}/api/payroll/`, {
            method: isUpdate ? 'PUT' : 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(payroll)
        });
        
        if (response.ok) {
            alert(isUpdate ? 'Payroll updated successfully!' : 'Payroll added successfully!');
            closeModal('payrollModal');
            CURRENT_EDIT_PAYROLL_ID = null;
            loadPayroll();
            loadReports();
        } else {
            const error = await response.json();
            alert('Error: ' + (error.error || 'Failed to save payroll'));
        }
    } catch (error) {
        alert('Error: ' + error.message);
    }
}

// Delete employee
async function deleteEmployee(employeeId) {
    if (!confirm('Are you sure you want to delete this employee?')) {
        return;
    }
    
    try {
        const response = await fetch(`${API_BASE}/api/employees/${employeeId}`, {
            method: 'DELETE'
        });
        
        if (response.ok) {
            alert('Employee deleted successfully!');
            loadEmployees();
        } else {
            alert('Error deleting employee');
        }
    } catch (error) {
        alert('Error: ' + error.message);
    }
}

// Delete payroll
async function deletePayroll(id) {
    if (!confirm('Are you sure you want to delete this payroll record?')) return;
    try {
        const response = await fetch(`${API_BASE}/api/payroll/${id}`, { method: 'DELETE' });
        if (response.ok) {
            alert('Payroll deleted successfully!');
            loadPayroll();
            loadReports();
        } else {
            const err = await response.json().catch(()=>({error:'Failed'}));
            alert('Error: ' + (err.error || 'Failed to delete payroll'));
        }
    } catch (e) {
        alert('Error: ' + e.message);
    }
}

// Edit employee (placeholder)
async function editEmployee(employeeId) {
    try {
        const response = await fetch(`${API_BASE}/api/employees/${employeeId}`);
        const employee = await response.json();
        
        // Populate form
        document.getElementById('empId').value = employee.employeeId;
        document.getElementById('firstName').value = employee.firstName;
        document.getElementById('lastName').value = employee.lastName;
        document.getElementById('email').value = employee.email;
        document.getElementById('phone').value = employee.phone;
        document.getElementById('department').value = employee.department;
        document.getElementById('position').value = employee.position;
        document.getElementById('baseSalary').value = employee.baseSalary;
        document.getElementById('hireDate').value = employee.hireDate;
        
        // Change form to update mode
        document.getElementById('employeeForm').onsubmit = async (e) => {
            e.preventDefault();
            
            const updatedEmployee = {
                employeeId: document.getElementById('empId').value,
                firstName: document.getElementById('firstName').value,
                lastName: document.getElementById('lastName').value,
                email: document.getElementById('email').value,
                phone: document.getElementById('phone').value,
                department: document.getElementById('department').value,
                position: document.getElementById('position').value,
                baseSalary: parseFloat(document.getElementById('baseSalary').value),
                hireDate: document.getElementById('hireDate').value,
                status: 'active'
            };
            
            try {
                const updateResponse = await fetch(`${API_BASE}/api/employees/`, {
                    method: 'PUT',
                    headers: {
                        'Content-Type': 'application/json'
                    },
                    body: JSON.stringify(updatedEmployee)
                });
                
                if (updateResponse.ok) {
                    alert('Employee updated successfully!');
                    closeModal('employeeModal');
                    loadEmployees();
                } else {
                    alert('Error updating employee');
                }
            } catch (error) {
                alert('Error: ' + error.message);
            }
        };
        
        document.getElementById('employeeModal').style.display = 'block';
    } catch (error) {
        alert('Error: ' + error.message);
    }
}

// Auto-refresh on section switch
window.addEventListener('load', () => {
    // Check if we're on home page - if so, don't load employees
    const homeSection = document.getElementById('home');
    if (!homeSection.classList.contains('active')) {
        loadEmployees();
    }
});

// Override showSection to handle home navigation
const originalShowSection = showSection;
showSection = function(sectionId) {
    originalShowSection(sectionId);
    
    if (sectionId === 'employees') {
        loadEmployees();
    } else if (sectionId === 'payroll') {
        loadPayroll();
    } else if (sectionId === 'reports') {
        loadReports();
    }
    
    // If returning to home, hide container
    if (sectionId === 'home') {
        const container = document.querySelector('.container');
        if (container) {
            container.style.display = 'none';
        }
        // Remove active state from all nav buttons
        document.querySelectorAll('.nav-btn').forEach(btn => {
            btn.classList.remove('active');
        });
    }
};

