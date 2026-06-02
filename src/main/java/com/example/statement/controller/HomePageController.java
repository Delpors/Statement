package com.example.statement.controller;

import com.example.statement.entity.InstitutionEntity;
import com.example.statement.service.IEmployeeService;
import com.example.statement.service.IInstitutionService;
import com.example.statement.service.IPayrollQueryService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
public class HomePageController {

    private final IInstitutionService institutionService;
    private final IEmployeeService employeeService;
    private final IPayrollQueryService payrollQueryService;

    @GetMapping("/")
    public String showInstitutions(Model model){

        model.addAttribute("institutions", institutionService.getAllInstitutions());
        model.addAttribute("institutionEntity", new InstitutionEntity());
        return "startPage";
    }

    @PostMapping("/")
    public String selectInstitution(@RequestParam Long id, HttpSession session) {
        session.setAttribute("selectedInstId", id);
        return "redirect:/home";
    }

    @GetMapping("/home")
    public String homePage(Model model,
                           @SessionAttribute("selectedInstId") Long institutionId){

        InstitutionEntity institution = institutionService.getInstitutionEntityById(institutionId);

        model.addAttribute("EmployeeCount", employeeService.getEmployeesCount(institutionId));
        model.addAttribute("PayrollCount", payrollQueryService.getCount(institution));

        return "home";
    }
}
