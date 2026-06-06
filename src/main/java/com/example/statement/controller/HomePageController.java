package com.example.statement.controller;

import com.example.statement.entity.InstitutionEntity;
import com.example.statement.entity.UserEntity;
import com.example.statement.service.IEmployeeService;
import com.example.statement.service.IInstitutionService;
import com.example.statement.service.IPayrollQueryService;
import com.example.statement.service.IUserService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
public class HomePageController {

    private final IInstitutionService institutionService;
    private final IEmployeeService employeeService;
    private final IPayrollQueryService payrollQueryService;
    private final IUserService userService;

    @GetMapping("/")
    public String showInstitutions(Model model, Authentication authentication) {

        UserEntity user = userService.getUserByUserName(authentication.getName());

        model.addAttribute("institutions", institutionService.getAllInstitutions(user));
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
                           @SessionAttribute("selectedInstId") Long instId){

        model.addAttribute("EmployeeCount", employeeService.getEmployeesCount(instId));
        model.addAttribute("PayrollCount", payrollQueryService.getCount(instId));

        return "home";
    }
}
