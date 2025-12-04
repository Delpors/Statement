package com.example.statement.employees;

import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller

public class EmployeeController {

    private final EmployeeService employeeService;

    public EmployeeController(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }


    @GetMapping("/employees")
    public String getAllEmployees(Model model, HttpSession session) {

        Long selectedInstId = (Long) session.getAttribute("selectedInstId");

        model.addAttribute("employees",
                employeeService.getAllEmployees(selectedInstId));
        return "employees";
    }

    @GetMapping("/employees/create")
    public String showCreateForm(Model model)
    {
        model.addAttribute("employee", new EmployeeEntity());
        return "createEmployee";
    }

    @PostMapping("/employees/create")
    public String createEmployee(@ModelAttribute EmployeeDTO employeeDTO, HttpSession session)
    {
        Long selectedInstId = (Long) session.getAttribute("selectedInstId");

        employeeService.createEmployee(employeeDTO, selectedInstId);
        return "redirect:/employees";
    }

    @GetMapping("/employees/edit/{id}")
    public String showEditForm(@PathVariable Long id, Model model)
    {
        EmployeeDTO employee = employeeService.getEmployeeDTOById(id);

        model.addAttribute("employee", employee);
        return "editEmployee";
    }

    @PostMapping("/employees/update/{employee_id}")
    public String updateEmployee(@ModelAttribute EmployeeDTO employeeDTO, HttpSession session)
    {
        Long selectedInstId = (Long) session.getAttribute("selectedInstId");

        employeeService.updateEmployee(employeeDTO, selectedInstId);
        return "redirect:/employees";
    }

    @GetMapping("/employees/{employee_id}")
    public String deleteEmployee(@PathVariable Long employee_id)
    {
        employeeService.deleteEmployee(employee_id);
        return "redirect:/employees";
    }
}
