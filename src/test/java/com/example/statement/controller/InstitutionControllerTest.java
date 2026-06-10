package com.example.statement.controller;

import com.example.statement.dto.request.InstitutionRequest;
import com.example.statement.dto.response.InstitutionResponse;
import com.example.statement.entity.InstitutionEntity;
import com.example.statement.entity.UserEntity;
import com.example.statement.service.IInstitutionService;
import com.example.statement.service.IUserService;
import com.example.statement.util.UserRole;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;
import org.springframework.ui.Model;

import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class InstitutionControllerTest {

    @Mock
    private IInstitutionService institutionService;

    @Mock
    private IUserService userService;

    @Mock
    private Model model;

    @Mock
    private Authentication authentication;

    @InjectMocks
    private InstitutionController institutionController;

    private UserEntity testUser;
    private InstitutionRequest testRequest;
    private InstitutionResponse testResponse;

    @BeforeEach
    void setUp() {
        testUser = new UserEntity();
        testUser.setId(1L);
        testUser.setUsername("testuser");
        testUser.setEmail("test@example.com");
        testUser.setRole(UserRole.USER);

        testRequest = new InstitutionRequest(
                "New University",
                "NU",
                "Dr. New Director",
                "New Accountant",
                "New General"
        );

        testResponse = new InstitutionResponse(
                100L,
                "Test University",
                "TU",
                "Dr. John Smith",
                "Jane Doe",
                "Bob Johnson"
        );
    }

    @Test
    void showCreatInstForm_ShouldReturnCreateInstView() {

        String result = institutionController.showCreatInstForm(model);

        assertEquals("createInstitution", result);
        verify(model).addAttribute(eq("institution"), any(InstitutionEntity.class));
    }

    @Test
    void showCreatInstForm_ShouldThrowException() {

        assertThrows(NullPointerException.class, () -> {
            institutionController.showCreatInstForm(null);
        });
    }

    @Test
    void showEditForm_ShouldReturnEditInstView() {

        Long institutionId = 100L;
        when(institutionService.getInstitutionDTOById(institutionId)).thenReturn(testResponse);

        String result = institutionController.showEditForm(institutionId, model);

        assertEquals("editInstitution", result);
        verify(institutionService).getInstitutionDTOById(institutionId);
        verify(model).addAttribute("institution", testResponse);
    }

    @Test
    void showEditForm_ShouldThrowException() {

        Long institutionId = 999L;
        when(institutionService.getInstitutionDTOById(institutionId))
                .thenThrow(new RuntimeException("Institution not found"));

        RuntimeException thrown = assertThrows(RuntimeException.class, () -> {
            institutionController.showEditForm(institutionId, model);
        });

        assertEquals("Institution not found", thrown.getMessage());
        verify(model, never()).addAttribute(eq("institution"), any());
    }

    @Test
    void showEditForm_ShouldThrowException_WhenModelIsNull() {

        Long institutionId = 100L;

        assertThrows(NullPointerException.class, () -> {
            institutionController.showEditForm(institutionId, null);
        });
    }

    @Test
    void createInst_ShouldCreateInst() {

        String username = "testuser";
        when(authentication.getName()).thenReturn(username);
        when(userService.getUserByUserName(username)).thenReturn(testUser);
        doNothing().when(institutionService).createInstitution(testRequest, testUser);

        String result = institutionController.createInstitution(testRequest, authentication);

        assertEquals("redirect:/institutions", result);
        verify(authentication).getName();
        verify(userService).getUserByUserName(username);
        verify(institutionService).createInstitution(testRequest, testUser);
    }

    @Test
    void createInst_ShouldThrowException_WhenUserNotFound() {

        String username = "unknownuser";
        when(authentication.getName()).thenReturn(username);
        when(userService.getUserByUserName(username))
                .thenThrow(new RuntimeException("User not found"));

        RuntimeException thrown = assertThrows(RuntimeException.class, () -> {
            institutionController.createInstitution(testRequest, authentication);
        });

        assertEquals("User not found", thrown.getMessage());
        verify(institutionService, never()).createInstitution(any(), any());
    }

    @Test
    void createInstitution_ShouldThrowException_WhenAuthenticationIsNull() {

        assertThrows(NullPointerException.class, () -> {
            institutionController.createInstitution(testRequest, null);
        });
    }


    @Test
    void createInst_ShouldHandleInstServiceException() {

        String username = "testuser";
        when(authentication.getName()).thenReturn(username);
        when(userService.getUserByUserName(username)).thenReturn(testUser);
        doThrow(new RuntimeException("Database error"))
                .when(institutionService).createInstitution(testRequest, testUser);

        RuntimeException thrown = assertThrows(RuntimeException.class, () -> {
            institutionController.createInstitution(testRequest, authentication);
        });

        assertEquals("Database error", thrown.getMessage());
    }

    @Test
    void updateInst_ShouldUpdateInst() {

        Long institutionId = 100L;
        String username = "testuser";
        when(authentication.getName()).thenReturn(username);
        when(userService.getUserByUserName(username)).thenReturn(testUser);
        doNothing().when(institutionService).updateInstitution(institutionId, testRequest, testUser);

        String result = institutionController.updateInstitution(institutionId, testRequest, authentication);

        assertEquals("redirect:/institutions", result);
        verify(authentication).getName();
        verify(userService).getUserByUserName(username);
        verify(institutionService).updateInstitution(institutionId, testRequest, testUser);
    }

    @Test
    void updateInst_ShouldThrowException() {

        Long institutionId = 999L;
        String username = "testuser";
        when(authentication.getName()).thenReturn(username);
        when(userService.getUserByUserName(username)).thenReturn(testUser);
        doThrow(new RuntimeException("Institution not found"))
                .when(institutionService).updateInstitution(institutionId, testRequest, testUser);

        RuntimeException thrown = assertThrows(RuntimeException.class, () -> {
            institutionController.updateInstitution(institutionId, testRequest, authentication);
        });

        assertEquals("Institution not found", thrown.getMessage());
    }

    @Test
    void updateInst_ShouldThrowException_WhenUserNotFound() {

        Long institutionId = 100L;
        String username = "unknownuser";
        when(authentication.getName()).thenReturn(username);
        when(userService.getUserByUserName(username))
                .thenThrow(new RuntimeException("User not found"));

        RuntimeException thrown = assertThrows(RuntimeException.class, () -> {
            institutionController.updateInstitution(institutionId, testRequest, authentication);
        });

        assertEquals("User not found", thrown.getMessage());
        verify(institutionService, never()).updateInstitution(any(), any(), any());
    }

    @Test
    void getAllInst_ShouldReturnInstView() {

        String username = "testuser";
        List<InstitutionResponse> institutions = Arrays.asList(testResponse);

        when(authentication.getName()).thenReturn(username);
        when(userService.getUserByUserName(username)).thenReturn(testUser);
        when(institutionService.getAllInstitutions(testUser)).thenReturn(institutions);

        String result = institutionController.getAllInstitutions(model, authentication);

        assertEquals("institutions", result);
        verify(authentication).getName();
        verify(userService).getUserByUserName(username);
        verify(institutionService).getAllInstitutions(testUser);
        verify(model).addAttribute("institutions", institutions);
    }

    @Test
    void getAllInst_ShouldHandleEmptyList() {

        String username = "testuser";
        List<InstitutionResponse> emptyList = List.of();

        when(authentication.getName()).thenReturn(username);
        when(userService.getUserByUserName(username)).thenReturn(testUser);
        when(institutionService.getAllInstitutions(testUser)).thenReturn(emptyList);

        String result = institutionController.getAllInstitutions(model, authentication);

        assertEquals("institutions", result);
        verify(model).addAttribute("institutions", emptyList);
    }

    @Test
    void getAllInst_ShouldThrowException_WhenUserNotFound() {

        String username = "unknownuser";
        when(authentication.getName()).thenReturn(username);
        when(userService.getUserByUserName(username))
                .thenThrow(new RuntimeException("User not found"));

        RuntimeException thrown = assertThrows(RuntimeException.class, () -> {
            institutionController.getAllInstitutions(model, authentication);
        });

        assertEquals("User not found", thrown.getMessage());
        verify(institutionService, never()).getAllInstitutions(any());
    }

    @Test
    void getAllInst_ShouldThrowException_WhenAuthenticationIsNull() {

        assertThrows(NullPointerException.class, () -> {
            institutionController.getAllInstitutions(model, null);
        });
    }

    @Test
    void getAllInstitutions_ShouldThrowException_WhenModelIsNull() {

        String username = "testuser";
        when(authentication.getName()).thenReturn(username);
        when(userService.getUserByUserName(username)).thenReturn(testUser);

        assertThrows(NullPointerException.class, () -> {
            institutionController.getAllInstitutions(null, authentication);
        });
    }

    @Test
    void deleteInst_ShouldDeleteInst() {

        Long institutionId = 100L;
        doNothing().when(institutionService).deleteInstitution(institutionId);

        String result = institutionController.deleteInstitution(institutionId);

        assertEquals("redirect:/institutions", result);
        verify(institutionService).deleteInstitution(institutionId);
    }

    @Test
    void deleteInst_ShouldThrowException_WhenInstNotFound() {

        Long institutionId = 999L;
        doThrow(new RuntimeException("Institution not found"))
                .when(institutionService).deleteInstitution(institutionId);

        RuntimeException thrown = assertThrows(RuntimeException.class, () -> {
            institutionController.deleteInstitution(institutionId);
        });

        assertEquals("Institution not found", thrown.getMessage());
    }

    @Test
    void deleteInst_ShouldHandleServiceException() {

        Long institutionId = 100L;
        doThrow(new RuntimeException("Database error"))
                .when(institutionService).deleteInstitution(institutionId);

        RuntimeException thrown = assertThrows(RuntimeException.class, () -> {
            institutionController.deleteInstitution(institutionId);
        });

        assertEquals("Database error", thrown.getMessage());
    }

    @Test
    void showEditForm_ShouldHandleInstWithNullFields() {

        Long institutionId = 100L;
        InstitutionResponse responseWithNulls = new InstitutionResponse(
                100L, null, null, null, null, null
        );
        when(institutionService.getInstitutionDTOById(institutionId)).thenReturn(responseWithNulls);

        String result = institutionController.showEditForm(institutionId, model);

        assertEquals("editInstitution", result);
        verify(model).addAttribute("institution", responseWithNulls);
    }

/*    @Test
    void createInst_ShouldHandleRequestWithNullFields() {

        String username = "testuser";
        InstitutionRequest emptyRequest = new InstitutionRequest();

        when(authentication.getName()).thenReturn(username);
        when(userService.getUserByUserName(username)).thenReturn(testUser);
        doNothing().when(institutionService).createInstitution(emptyRequest, testUser);

        String result = institutionController.createInstitution(emptyRequest, authentication);

        assertEquals("redirect:/institutions", result);
        verify(institutionService).createInstitution(emptyRequest, testUser);
    }*/


}