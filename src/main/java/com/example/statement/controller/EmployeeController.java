package com.example.statement.controller;

import com.example.statement.dto.request.EmployeeRequest;
import com.example.statement.dto.response.EmployeeResponse;
import com.example.statement.entity.EmployeeEntity;
import com.example.statement.service.EmployeeService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
public class EmployeeController {

    private final EmployeeService employeeService;

    @GetMapping("/employees")
    public String getAllEmployees(
            Model model,
            @SessionAttribute ("selectedInstId") Long selectedInstId) {

        if (selectedInstId == null) {
            throw new IllegalArgumentException("Учреждение не выбрано.");
        }

        model.addAttribute("employees",
                employeeService.getAllActiveEmployeesByInstitutionId(selectedInstId));
        return "employees";
    }

    @GetMapping("/employees/create")
    public String showCreateForm(Model model)
    {
        model.addAttribute("employee", new EmployeeEntity());
        return "createEmployee";
    }

    @PostMapping("/employees/create")
    public String createEmployee(
            @ModelAttribute EmployeeRequest request,
            @SessionAttribute ("selectedInstId") Long selectedInstId)
    {
        if (selectedInstId == null) {
            throw new IllegalArgumentException("Учреждение не выбрано.");
        }

        employeeService.createEmployeeForInstitution(request, selectedInstId);
        return "redirect:/employees";
    }

    @GetMapping("/employees/edit/{id}")
    public String showEditForm(@PathVariable Long id, Model model)
    {
        EmployeeResponse employee = employeeService.getEmployeeById(id);

        model.addAttribute("employee", employee);
        return "editEmployee";
    }

    @PostMapping("/employees/update/{id}")
    public String updateEmployee(
            @PathVariable("id") Long id,
            @ModelAttribute EmployeeRequest request,
            @SessionAttribute ("selectedInstId") Long selectedInstId)
    {
        if (selectedInstId == null) {
            throw new IllegalArgumentException("Учреждение не выбрано.");
        }

        employeeService.updateEmployee(id,request, selectedInstId);
        return "redirect:/employees";
    }

    @DeleteMapping("/employees/{id}")
    public String deleteEmployee(@PathVariable Long id)
    {
        employeeService.deleteEmployee(id);
        return "redirect:/employees";
    }
}
