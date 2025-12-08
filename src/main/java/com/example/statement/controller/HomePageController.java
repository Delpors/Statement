package com.example.statement.controller;

import com.example.statement.entity.InstitutionEntity;
import com.example.statement.service.InstitutionService;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class HomePageController {

    InstitutionService institutionService;

    HomePageController(InstitutionService institutionService){
        this.institutionService = institutionService;
    }

    @GetMapping("/")
    public String showInstitutions(Model model){

        model.addAttribute("institutions", institutionService.getAllInstitutions());
        model.addAttribute("institutionEntity", new InstitutionEntity()); // объект для формы
        return "startPage";
    }

    @PostMapping("/")
    public String selectInstitution(@RequestParam Long institutionId, HttpSession session) {

        session.setAttribute("selectedInstId", institutionId);
        return "redirect:/home";
    }

    @GetMapping("/home")
    public String homePage(){

        return "home";
    }

}
