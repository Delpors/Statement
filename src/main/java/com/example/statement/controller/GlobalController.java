package com.example.statement.controller;

import com.example.statement.dto.respons.InstitutionResponse;
import com.example.statement.service.InstitutionService;
import jakarta.servlet.http.HttpSession;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@ControllerAdvice
public class GlobalController {

    InstitutionService institutionService;

    public GlobalController(InstitutionService institutionService){
        this.institutionService = institutionService;
    }

    @ModelAttribute
    private void addInstitutionToModel(HttpSession session, Model model) {

        Long selectedInstId = (Long) session.getAttribute("selectedInstId");

        if (selectedInstId != null) {
            InstitutionResponse institutionDTO = institutionService.getInstitutionDTOById(selectedInstId);
            model.addAttribute("selectedInstitution", institutionDTO);
        }
    }

    @ExceptionHandler({IllegalMonitorStateException.class, IllegalArgumentException.class})
    public String handleBusinessExceptions(Exception e, RedirectAttributes redirectAttributes){

        redirectAttributes.addFlashAttribute("error", e.getMessage());
        return "redirect:payroll?error";
    }
}
