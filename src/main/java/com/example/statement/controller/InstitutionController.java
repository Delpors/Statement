package com.example.statement.controller;

import com.example.statement.dto.request.InstitutionRequest;
import com.example.statement.dto.response.InstitutionResponse;
import com.example.statement.entity.InstitutionEntity;
import com.example.statement.service.IInstitutionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/institutions")
@RequiredArgsConstructor
public class InstitutionController {

    private final IInstitutionService institutionService;

    @GetMapping("/create")
    public String showCreatInstForm(Model model){

        model.addAttribute("institution", new InstitutionEntity());
        return "createInstitution";
    }

    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Long id, Model model){

        InstitutionResponse institutionDTO = institutionService.getInstitutionDTOById(id);
        model.addAttribute("institution", institutionDTO);

        return "editInstitution";
    }

    @PostMapping("/create")
    public String createInstitution(@ModelAttribute InstitutionRequest request, BindingResult bindingResult){

        institutionService.createInstitution(request);
        return "redirect:/institutions";
    }

    @PostMapping("/update/{id}")
    public String updateInstitution(@PathVariable Long id, @ModelAttribute InstitutionRequest request){

        institutionService.updateInstitution(id, request);
        return "redirect:/institutions";
    }

    @GetMapping()
    public String getAllInstitutions(Model model){
        model.addAttribute
                ("institutions",
                        institutionService.getAllInstitutions());
        return "institutions";
    }

    @GetMapping("/{id}")
    public String deleteInstitution(@PathVariable Long id){

        institutionService.deleteInstitution(id);
        return "redirect:/institutions";
    }
}
