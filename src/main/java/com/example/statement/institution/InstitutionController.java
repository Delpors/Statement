package com.example.statement.institution;

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

        InstitutionDTO institutionDTO = institutionService.getInstitutionDTOById(id);
        model.addAttribute("institution", institutionDTO);

        return "editInstitution";
    }

    @PostMapping("/institutions/create")
    public String createInstitution(@ModelAttribute InstitutionDTO institutionDTO){

        institutionService.createOrUpdateInstitution(institutionDTO);
        return "redirect:/institutions";
    }

    @PostMapping("/institutions/update/{id}")
    public String updateInstitution(@ModelAttribute InstitutionDTO institutionDTO){
        institutionService.createOrUpdateInstitution(institutionDTO);
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
