package com.example.statement.controller;

import com.example.statement.dto.response.InstitutionResponse;
import com.example.statement.service.IInstitutionService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

@ControllerAdvice
@RequiredArgsConstructor
public class GlobalController {

    private final IInstitutionService institutionService;

    @ModelAttribute
    public void addInstitutionToModel(HttpSession session, Model model){

        Long selectedInstId = (Long) session.getAttribute("selectedInstId");

        if (selectedInstId != null) {
            InstitutionResponse institutionDTO = institutionService.getInstitutionDTOById(selectedInstId);
            model.addAttribute("selectedInstitution", institutionDTO);
        }
    }
}
