package com.employee.Controllers;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.ObjectUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.employee.DTO.EmployeeDto;
import com.employee.Entities.Attendance;
import com.employee.Entities.Employee;
import com.employee.Entities.Leave;
import com.employee.Entities.Organization;
import com.employee.Entities.Payroll;
import com.employee.Repositories.OrganizationRepository;
import com.employee.ServiceImpl.LeaveService;
import com.employee.ServiceImpl.PayrollService;
import com.employee.Sevices.AttendanceService;
import com.employee.Sevices.EmployeeService;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;

@Controller
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private EmployeeService employeeService;

    @Autowired
    private AttendanceService attendanceService;

    @Autowired
    private OrganizationRepository orgRepo;

    @Autowired
    private PayrollService payrollService;

    @GetMapping("/")
    public String dashboard(HttpSession session, Model model) {

        if (session.getAttribute("loggedInUser") == null) {
            return "redirect:/login";
        }
        model.addAttribute("showSidebar", true);
        model.addAttribute("paramValue", "dashboard");
        return "dashboard";
    }

    @GetMapping("/employees")
    public String employees(Model model, HttpSession session) {
        Organization organization = (Organization) session.getAttribute("loggedInUser");
        List<Employee> employees = employeeService.fondByOrganization(organization);
        model.addAttribute("showSidebar", true);
        model.addAttribute("paramValue", "employee");
        model.addAttribute("employees", employees);
        return "employees";
    }

    @GetMapping("/add-employee")
    public String addEmployee(Model model) {
        model.addAttribute("showSidebar", true);
        model.addAttribute("paramValue", "addEmployee");
        model.addAttribute("employeeDto", new EmployeeDto());
        return "add_employee";
    }


    @GetMapping("/attendance")
    public String attendancePage(@RequestParam(required = false) String date,
                                Model model,
                                HttpSession session) {

        if (session.getAttribute("loggedInUser") == null) {
            return "redirect:/login";
        }

        Organization org = (Organization) session.getAttribute("loggedInUser");

        LocalDate selectedDate = (date != null)
                ? LocalDate.parse(date)
                : LocalDate.now();

        List<Employee> employees = employeeService.fondByOrganization(org);

        List<Attendance> attendanceList =
                attendanceService.getAttendanceByDateAndOrganization(selectedDate, org);

   

        // 🔥 Map बनवत
        Map<String, String> attendanceMap = new HashMap<>();

        for (Attendance a : attendanceList) {
            attendanceMap.put(a.getEmpId(), a.getStatus());
        }

        model.addAttribute("employees", employees);
        model.addAttribute("attendanceMap", attendanceMap);
        model.addAttribute("selectedDate", selectedDate);
        model.addAttribute("today", LocalDate.now());   // 🔥 IMPORTANT
        model.addAttribute("showSidebar", true);
        model.addAttribute("paramValue", "attendance");
        return "employee_attendance";
    }

    @GetMapping("/delete-emp/{id}")
    public String deleteEmployee(@PathVariable Long id, HttpSession session, Model model) {

        if (session.getAttribute("loggedInUser") == null) {
            return "redirect:/login";
        }

        employeeService.deleteById(id);
        model.addAttribute("success", "Employee deleted successfully");
        return "redirect:/admin/employees";
    }

    @GetMapping("/update-emp/{id}")
    public String updateEmployee(@PathVariable Long id, HttpSession session, Model model) {

        if (session.getAttribute("loggedInUser") == null) {
            return "redirect:/login";
        }

        Employee employee = employeeService.fondByOrganization((Organization) session.getAttribute("loggedInUser"))
                .stream()
                .filter(e -> e.getId().equals(id))
                .findFirst()
                .orElse(null);

        if (employee == null) {
            model.addAttribute("error", "Employee not found");
            model.addAttribute("showSidebar", true);
            model.addAttribute("paramValue", "employee");
            return "redirect:/admin/employees";
        }

        model.addAttribute("employee", employee);
        model.addAttribute("showSidebar", true);
        model.addAttribute("paramValue", "employee");
        return "update_employee";
    }

    @PostMapping("/update-employee")
    public String updateEmployee(@Valid @ModelAttribute("employee") Employee employee, BindingResult result, HttpSession session, RedirectAttributes redirectAttributes, Model model) {

        if (result.hasErrors()) {
            model.addAttribute("showSidebar", true);
            model.addAttribute("paramValue", "empolyee");
            return "update_employee";
        }

        Organization org = (Organization) session.getAttribute("loggedInUser");

        Employee existingEmp = employeeService.fondByOrganization(org)
                .stream()
                .filter(e -> e.getId().equals(employee.getId()))
                .findFirst()
                .orElse(null);

        if (existingEmp == null) {
            model.addAttribute("error", "Employee not found");
            return "redirect:/admin/employees";
        }


        Boolean eBoolean = employeeService.updateEmployee(employee);

        if (ObjectUtils.isEmpty(eBoolean)) {
            model.addAttribute("error", "Failed to update employee");
            model.addAttribute("showSidebar", true);
            model.addAttribute("paramValue", "empolyee");
            return "update_employee";
        }
        redirectAttributes.addFlashAttribute("success", "Employee updated successfully");
        return "redirect:/admin/employees";
    }


    @GetMapping("/profile")
    public String profilePage(Model model, HttpSession session) {

        Organization org = (Organization) session.getAttribute("loggedInUser");

        model.addAttribute("org", org);
        model.addAttribute("showSidebar", true);

        return "profile";
    }

    @PostMapping("/update-profile")
    public String updateProfile(Organization updatedOrg, HttpSession session, Model model) {

        Organization org = (Organization) session.getAttribute("loggedInUser");

        model.addAttribute("org", org);

        org.setOrgName(updatedOrg.getOrgName());
        org.setEmail(updatedOrg.getEmail());
        org.setContact(updatedOrg.getContact());
        org.setType(updatedOrg.getType());
        org.setAddress(updatedOrg.getAddress());

        orgRepo.save(org);

        session.setAttribute("loggedInUser", org);
        model.addAttribute("showSidebar", true);
        model.addAttribute("success", "Profile updated successfully");
        return "redirect:/admin/profile";
    }

    @PostMapping("/update-password")
    public String changePassword(@RequestParam String oldPassword,
                                 @RequestParam String newPassword,
                                 @RequestParam String confirmPassword,
                                 HttpSession session,
                                 Model model) {

        Organization org = (Organization) session.getAttribute("loggedInUser");

        model.addAttribute("org", org);

        if (!org.getPassword().equals(oldPassword)) {
            model.addAttribute("error", "Old password incorrect");
            model.addAttribute("showSidebar", true);
            return "profile";
        }

        if (!newPassword.equals(confirmPassword)) {
            model.addAttribute("error", "Passwords do not match");
            model.addAttribute("showSidebar", true);
            return "profile";
        }

        org.setPassword(newPassword);
        orgRepo.save(org);
        model.addAttribute("showSidebar", true);
        model.addAttribute("success", "Password updated successfully");
        return "redirect:/admin/profile";
    }


     @Autowired
    private LeaveService leaveService;

    @GetMapping("/leave")
    public String leavePage(Model model, HttpSession session) {

        if (session.getAttribute("loggedInUser") == null) {
            return "redirect:/login";
        }

        Organization org = (Organization) session.getAttribute("loggedInUser");

        List<Leave> leaves = leaveService.getAllLeaves(org);

        model.addAttribute("leaves", leaves);
        model.addAttribute("showSidebar", true);
        model.addAttribute("paramValue", "leave");
        return "employee_leave";
    }

    @PostMapping("/leave/apply")
    public String applyLeave(@RequestParam String empId,
                             @RequestParam String name,
                             @RequestParam String department,
                             @RequestParam String fromDate,
                             @RequestParam String toDate,
                             @RequestParam String reason,
                             HttpSession session,
                            Model model) {

        Organization org = (Organization) session.getAttribute("loggedInUser");

        Leave leave = new Leave();
        leave.setEmpId(empId);
        leave.setName(name);
        leave.setDepartment(department);
        leave.setFromDate(LocalDate.parse(fromDate));
        leave.setToDate(LocalDate.parse(toDate));
        leave.setReason(reason);
        leave.setOrganization(org);

        leaveService.applyLeave(leave);
        model.addAttribute("paramValue", "leave");
        return "redirect:/admin/leave";
    }

    @GetMapping("/leave/status")
    public String updateStatus(@RequestParam Long id,
                              @RequestParam String status, Model model) {

        leaveService.updateStatus(id, status);
        model.addAttribute("paramValue", "leave");
        return "redirect:/admin/leave";
    }


     @GetMapping("/payroll")
    public String payrollPage(@RequestParam(required = false) String month,
                              Model model,
                              HttpSession session) {

        if (session.getAttribute("loggedInUser") == null) {
            return "redirect:/login";
        }

        Organization org = (Organization) session.getAttribute("loggedInUser");

        String selectedMonth = (month != null) ? month : LocalDate.now().toString().substring(0,7);

        List<Employee> employees = employeeService.fondByOrganization(org);
        List<Payroll> payrollList = payrollService.getByMonth(selectedMonth, org);

        Map<String, Payroll> payrollMap = new HashMap<>();
        for (Payroll p : payrollList) {
            payrollMap.put(p.getEmpId(), p);
        }

        model.addAttribute("employees", employees);
        model.addAttribute("payrollMap", payrollMap);
        model.addAttribute("selectedMonth", selectedMonth);
        model.addAttribute("showSidebar", true);
        model.addAttribute("paramValue", "payroll");
        return "payroll";
    }

    @PostMapping("/payroll/save")
    public String savePayroll(@RequestParam List<String> empId,
                             @RequestParam List<Double> basic,
                             @RequestParam List<Double> allowance,
                             @RequestParam List<Double> deduction,
                             @RequestParam String month,
                             HttpSession session,
                             Model model) {

        Organization org = (Organization) session.getAttribute("loggedInUser");

        List<Employee> employees = employeeService.fondByOrganization(org);

        payrollService.savePayroll(empId, basic, allowance, deduction, month, org, employees);

        model.addAttribute("paramValue", "payroll");

        return "redirect:/admin/payroll?month=" + month;
    }
   


    // ======================= Proccessing =========================

    @PostMapping("/do-add-employee") 
    public String doAddEmployee(@Valid @ModelAttribute("employeeDto") EmployeeDto employeeDto, BindingResult result, Model model, RedirectAttributes rAttributes, HttpSession session) {
        
        if (result.hasErrors()) {
            model.addAttribute("showSidebar", true);
            model.addAttribute("paramValue", "addEmployee");
            return "add_employee";
        }

        Organization organization = (Organization) session.getAttribute("loggedInUser");

        try {
            employeeService.register(organization, employeeDto);
            rAttributes.addFlashAttribute("success", "Employee Added Successful!");
            model.addAttribute("showSidebar", true);
            model.addAttribute("paramValue", "employee");
            return "redirect:/admin/employees";

        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
            model.addAttribute("showSidebar", true);
            model.addAttribute("paramValue", "addEmployee");
            return "add_employee";
        }
    }


    @PostMapping("/attendance/save")
public String saveAttendance(@RequestParam(required = false) List<String> empId,
                            @RequestParam(required = false) List<String> status,
                            @RequestParam String date,
                            HttpSession session,
                            Model model) {
    
    LocalDate selectedDate = LocalDate.parse(date);
    Organization org = (Organization) session.getAttribute("loggedInUser");
    
    // Only allow saving for today's date
    if (!selectedDate.equals(LocalDate.now())) {
        model.addAttribute("error", "Attendance can only be marked for today's date");
        model.addAttribute("paramValue", "attendance");
        return "redirect:/admin/attendance?date=" + date;
    }
    
    // Check if attendance already marked for today
    List<Attendance> existingAttendance = 
        attendanceService.getAttendanceByDateAndOrganization(LocalDate.now(), org);
    
    if (!existingAttendance.isEmpty()) {
        model.addAttribute("error", "Attendance already marked for today");
        model.addAttribute("paramValue", "attendance");
        return "redirect:/admin/attendance?date=" + date;
    }
    
    // Check if we have data to save
    if (empId != null && status != null && empId.size() > 0) {
        for (int i = 0; i < empId.size(); i++) {
            attendanceService.markOrUpdateAttendance(
                empId.get(i),
                status.get(i),
                selectedDate,
                org
            );
        }
    }
    model.addAttribute("paramValue", "attendance");
    return "redirect:/admin/attendance?date=" + date;
}
}
