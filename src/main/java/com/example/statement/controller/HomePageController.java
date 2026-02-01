package com.example.statement.controller;

import com.example.statement.entity.InstitutionEntity;
import com.example.statement.repository.EmployeeRepository;
import com.example.statement.repository.PayrollRepository;
import com.example.statement.service.EmployeeService;
import com.example.statement.service.InstitutionService;
import com.example.statement.service.PayrollOrchestratorService;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class HomePageController {

    final InstitutionService institutionService;
    final EmployeeService employeeService;
    final PayrollOrchestratorService payrollService;

    HomePageController(InstitutionService institutionService,
                       PayrollRepository payrollRepository, EmployeeService employeeService, PayrollOrchestratorService payrollService)
    {
        this.institutionService = institutionService;
        this.employeeService = employeeService;
        this.payrollService = payrollService;
    }

    @GetMapping("/")
    public String showInstitutions(Model model){

        model.addAttribute("institutions", institutionService.getAllInstitutions());
        model.addAttribute("institutionEntity", new InstitutionEntity());
        return "startPage";
    }

    @PostMapping("/")
    public String selectInstitution(@RequestParam Long institutionId, HttpSession session) {

        session.setAttribute("selectedInstId", institutionId);
        return "redirect:/home";
    }

    @GetMapping("/home")
    public String homePage(Model model, HttpSession httpSession){

        Long institutionId = (Long) httpSession.getAttribute("selectedInstId");
        InstitutionEntity institution = institutionService.getInstitutionEntityById(institutionId);

        model.addAttribute("EmployeeCount", employeeService.getEmployeesCount(institution));
        model.addAttribute("PayrollCount", payrollService.getPayrollCount(institution));

        return "home";
    }
}
