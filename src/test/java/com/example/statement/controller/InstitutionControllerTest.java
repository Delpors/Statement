package com.example.statement.controller;

import com.example.statement.dto.request.InstitutionRequest;
import com.example.statement.dto.respons.InstitutionResponse;
import com.example.statement.entity.InstitutionEntity;
import com.example.statement.service.InstitutionService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.ui.Model;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
public class InstitutionControllerTest {
    @Mock
    private Model model;

    @Mock
    private InstitutionService service;

    @InjectMocks
    private InstitutionController controller;

    private final InstitutionRequest request = new InstitutionRequest(
            "МКОУ Кужникская СОШ","МКОУ Кужникская СОШ",
            "Рашидов С.Г.","Рашидов Г.Р.","Рашидов Г.Р."
    );

    @Test
    void showCreateInstFormTest(){

        String viewName = controller.showCreatInstForm(model);

        assertEquals("createInstitution", viewName);
        verify(model).addAttribute(eq("institution"), any(InstitutionEntity.class));
    }

    @Test
    void showEditForm(){
        InstitutionResponse response = new InstitutionResponse(
                1L, "МКОУ Кужникская СОШ","МКОУ Кужникская СОШ",
                "Рашидов С.Г.","Рашидов Г.Р.","Рашидов Г.Р."
        );

        when(service.getInstitutionDTOById(1L)).thenReturn(response);
        String viewName = controller.showEditForm(1L, model);

        assertEquals("editInstitution", viewName);
        verify(model).addAttribute("institution", response);

    }

    @Test
    void createInstitution(){

        doNothing().when(service).createInstitution(request);
        String viewName = controller.createInstitution(request);

        assertEquals("redirect:/institutions", viewName);
    }

    @Test
    void updateInstitutionTest(){
        doNothing().when(service).updateInstitution(1L,request);
        String viewName = controller.updateInstitution(1L,request);

        assertEquals("redirect:/institutions", viewName);

    }
}
