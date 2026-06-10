package com.example.statement.controller;

import com.example.statement.dto.response.InstitutionResponse;
import com.example.statement.service.IInstitutionService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.ui.Model;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class GlobalControllerTest {

    @Mock
    private IInstitutionService institutionService;

    @Mock
    private Model model;

    @InjectMocks
    private GlobalController globalController;

    private MockHttpSession session;

    @BeforeEach
    void setUp() {
        session = new MockHttpSession();
    }

    @Test
    void addInstToModel_WhenSelectedInstIdExists() {

        Long instId = 100L;
        session.setAttribute("selectedInstId", instId);

        InstitutionResponse expectedInstitution = new InstitutionResponse(
                100L,
                "Test University",
                "TU",
                "Dr. John Smith",
                "Jane Doe",
                "Bob Johnson"
        );

        when(institutionService.getInstitutionDTOById(instId))
                .thenReturn(expectedInstitution);

        globalController.addInstitutionToModel(session, model);

        verify(institutionService).getInstitutionDTOById(instId);
        verify(model).addAttribute("selectedInstitution", expectedInstitution);
        verifyNoMoreInteractions(institutionService, model);
    }

    @Test
    void addInstToModel_WhenSelectedInstIdIsNull() {

        session.setAttribute("selectedInstId", null);

        globalController.addInstitutionToModel(session, model);

        verify(institutionService, never()).getInstitutionDTOById(any());
        verify(model, never()).addAttribute(eq("selectedInstitution"), any());
    }

    @Test
    void addInstToModel_WhenSelectedInstIdNotInSession() {

        globalController.addInstitutionToModel(session, model);

        verify(institutionService, never()).getInstitutionDTOById(any());
        verify(model, never()).addAttribute(eq("selectedInstitution"), any());
    }

    @Test
    void addInstToModel_WhenSelectedInstIdIsZero() {

        Long instId = 0L;
        session.setAttribute("selectedInstId", instId);

        InstitutionResponse expectedInstitution = new InstitutionResponse(
                0L,
                "Default Institution",
                "DI",
                "Default Director",
                "Default Accountant",
                "Default General"
        );

        when(institutionService.getInstitutionDTOById(instId))
                .thenReturn(expectedInstitution);

        globalController.addInstitutionToModel(session, model);

        verify(institutionService).getInstitutionDTOById(instId);
        verify(model).addAttribute("selectedInstitution", expectedInstitution);
    }

    @Test
    void addInstToModel_WhenServiceThrowsException() {

        Long instId = 999L;
        session.setAttribute("selectedInstId", instId);

        RuntimeException expectedException = new RuntimeException("Institution not found");
        when(institutionService.getInstitutionDTOById(instId))
                .thenThrow(expectedException);

        RuntimeException thrown = assertThrows(RuntimeException.class, () -> {
            globalController.addInstitutionToModel(session, model);
        });

        assertEquals("Institution not found", thrown.getMessage());
        verify(institutionService).getInstitutionDTOById(instId);
        verify(model, never()).addAttribute(eq("selectedInstitution"), any());
    }

    @Test
    void addInstToModel_ShouldHandleNullModel() {

        Long instId = 100L;
        session.setAttribute("selectedInstId", instId);

        assertThrows(NullPointerException.class, () -> {
            globalController.addInstitutionToModel(session, null);
        });
    }

    @Test
    void addInstToModel_ShouldHandleNullSession() {

        assertThrows(NullPointerException.class, () -> {
            globalController.addInstitutionToModel(null, model);
        });
    }

    @Test
    void addInstToModel_WhenInstHasNullFields() {

        Long instId = 100L;
        session.setAttribute("selectedInstId", instId);

        InstitutionResponse institutionWithNulls = new InstitutionResponse(
                100L,
                null,
                null,
                null,
                null,
                null
        );

        when(institutionService.getInstitutionDTOById(instId))
                .thenReturn(institutionWithNulls);

        globalController.addInstitutionToModel(session, model);

        verify(institutionService).getInstitutionDTOById(instId);
        verify(model).addAttribute("selectedInstitution", institutionWithNulls);
    }
}