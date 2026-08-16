package com.example.statement.controller;

import com.example.statement.dto.request.EmployeeRequest;
import com.example.statement.dto.response.EmployeeResponse;
import com.example.statement.entity.EmployeeEntity;
import com.example.statement.service.IEmployeeService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
public class EmployeeController {

    private final IEmployeeService employeeService;

    @GetMapping("/employees/list")
    public String getAllEmployees(
            Model model,
            @SessionAttribute (value = "selectedInstId", required = false) Long instId) {

        if (instId == null) {
            return "redirect:/";
        }

        model.addAttribute("employees",
                employeeService.getAllActiveEmployeesByInstitutionId(instId));
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
            @ModelAttribute @Valid EmployeeRequest request,
            @SessionAttribute (value = "selectedInstId", required = false) Long instId)
    {
        if (instId == null) {
            return "redirect:/";
        }

        employeeService.createEmployeeForInstitution(request, instId);
        return "redirect:/employees/list";
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
            @ModelAttribute @Valid EmployeeRequest request,
            @SessionAttribute (value = "selectedInstId", required = false) Long instId)
    {
        if (instId == null) {
            return "redirect:/";
        }

        employeeService.updateEmployee(id,request, instId);
        return "redirect:/employees/list";
    }

    @DeleteMapping("/employees/{id}")
    public String deleteEmployee(@PathVariable Long id)
    {
        employeeService.deleteEmployee(id);
        return "redirect:/employees/list";
    }
}
