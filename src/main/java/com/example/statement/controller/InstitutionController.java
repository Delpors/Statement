package com.example.statement.controller;

import com.example.statement.dto.request.InstitutionRequest;
import com.example.statement.dto.respons.InstitutionResponse;
import com.example.statement.entity.InstitutionEntity;
import com.example.statement.service.InstitutionService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class InstitutionController {
    InstitutionService institutionService;

    public InstitutionController(InstitutionService institutionService){
        this.institutionService = institutionService;
    }


    @GetMapping("/institutions/create")
    public String showCreatInstForm(Model model){

        model.addAttribute("institution", new InstitutionEntity());
        return "createInstitution";
    }

    @GetMapping("/institutions/edit/{id}")
    public String showEditForm(@PathVariable Long id, Model model){

        InstitutionResponse institutionDTO = institutionService.getInstitutionDTOById(id);
        model.addAttribute("institution", institutionDTO);

        return "editInstitution";
    }

    @PostMapping("/institutions/create")
    public String createInstitution(@ModelAttribute InstitutionRequest request){

        institutionService.createInstitution(request);
        return "redirect:/institutions";
    }

    @PostMapping("/institutions/update/{id}")
    public String updateInstitution(@PathVariable Long id, @ModelAttribute InstitutionRequest request){
        System.out.println("Id из UEL " + id);
        institutionService.updateInstitution(id, request);
        return "redirect:/institutions";
    }

    @GetMapping("/institutions")
    public String getAllInstitutions(Model model){
        model.addAttribute
                ("institutions",
                        institutionService.getAllInstitutions());
        return "institutions";
    }

    @GetMapping("/institutions/{id}")
    public String deleteInstitution(@PathVariable Long id){

        institutionService.deleteInstitution(id);
        return "redirect:/institutions";
    }
}
