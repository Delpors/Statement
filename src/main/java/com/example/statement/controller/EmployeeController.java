package com.example.statement.controller;

import com.example.statement.dto.request.EmployeeRequest;
import com.example.statement.dto.respons.EmployeeResponse;
import com.example.statement.entity.EmployeeEntity;
import com.example.statement.service.EmployeeService;
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
    public String createEmployee(@ModelAttribute EmployeeRequest request, HttpSession session)
    {
        Long selectedInstId = (Long) session.getAttribute("selectedInstId");

        employeeService.createEmployee(request, selectedInstId);
        return "redirect:/employees";
    }

    @GetMapping("/employees/edit/{id}")
    public String showEditForm(@PathVariable Long id, Model model)
    {
        EmployeeResponse employee = employeeService.getEmployeeDTOById(id);

        model.addAttribute("employee", employee);
        return "editEmployee";
    }

    @PostMapping("/employees/update/{employee_id}")
    public String updateEmployee(@ModelAttribute EmployeeRequest request, HttpSession session)
    {
        Long selectedInstId = (Long) session.getAttribute("selectedInstId");

        employeeService.updateEmployee(request, selectedInstId);
        return "redirect:/employees";
    }

    @GetMapping("/employees/{employee_id}")
    public String deleteEmployee(@PathVariable Long employee_id)
    {
        employeeService.deleteEmployee(employee_id);
        return "redirect:/employees";
    }
}
