package com.example.statement.controller;

import com.example.statement.dto.response.InstitutionResponse;
import com.example.statement.entity.InstitutionEntity;
import com.example.statement.entity.UserEntity;
import com.example.statement.service.IEmployeeService;
import com.example.statement.service.IInstitutionService;
import com.example.statement.service.IPayrollQueryService;
import com.example.statement.service.IUserService;
import com.example.statement.util.UserRole;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.security.core.Authentication;
import org.springframework.ui.Model;

import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class HomePageControllerTest {

    @Mock
    private IInstitutionService institutionService;

    @Mock
    private IEmployeeService employeeService;

    @Mock
    private IPayrollQueryService payrollQueryService;

    @Mock
    private IUserService userService;

    @Mock
    private Model model;

    @Mock
    private Authentication authentication;

    @InjectMocks
    private HomePageController homePageController;

    private MockHttpSession session;
    private UserEntity testUser;
    private InstitutionEntity testInstitution;
    private InstitutionResponse institutionResponse;

    @BeforeEach
    void setUp() {
        session = new MockHttpSession();

        testUser = new UserEntity();
        testUser.setId(1L);
        testUser.setUsername("testuser");
        testUser.setEmail("test@example.com");
        testUser.setRole(UserRole.USER);

        testInstitution = InstitutionEntity.builder()
                .id(100L)
                .institutionFullName("Test University")
                .institutionAbbrev("TU")
                .director("Dr. John Smith")
                .generalAccountant("Jane Doe")
                .accountant("Bob Johnson")
                .user(testUser)
                .build();

        institutionResponse = new InstitutionResponse(
                1L,
                "MKOU Kuzhnik",
                "MKOU",
                "Dr Rashidow",
                "Djema",
                "Amina"
        );
    }

    @Test
    void showInstitutions_ShouldReturnStartPage_WhenAuthenticationIsValid() {

        String username = "testuser";
        when(authentication.getName()).thenReturn(username);
        when(userService.getUserByUserName(username)).thenReturn(testUser);

        List<InstitutionResponse> institutions = Arrays.asList(institutionResponse);
        when(institutionService.getAllInstitutions(testUser)).thenReturn(institutions);

        String result = homePageController.showInstitutions(model, authentication);

        assertEquals("startPage", result);
        verify(model).addAttribute("institutions", institutions);
        verify(model).addAttribute(eq("institutionEntity"), any(InstitutionEntity.class));
        verify(userService).getUserByUserName(username);
        verify(institutionService).getAllInstitutions(testUser);
    }

    @Test
    void showInstitutions_ShouldHandleEmptyInstitutionsList() {

        String username = "testuser";
        when(authentication.getName()).thenReturn(username);
        when(userService.getUserByUserName(username)).thenReturn(testUser);

        List<InstitutionResponse> emptyList = List.of();
        when(institutionService.getAllInstitutions(testUser)).thenReturn(emptyList);

        String result = homePageController.showInstitutions(model, authentication);

        assertEquals("startPage", result);
        verify(model).addAttribute("institutions", emptyList);
        verify(model).addAttribute(eq("institutionEntity"), any(InstitutionEntity.class));
    }

    @Test
    void showInstitutions_ShouldThrowException_WhenUserNotFound() {

        String username = "unknownuser";
        when(authentication.getName()).thenReturn(username);
        when(userService.getUserByUserName(username))
                .thenThrow(new RuntimeException("User not found"));


        RuntimeException thrown = assertThrows(RuntimeException.class, () -> {
            homePageController.showInstitutions(model, authentication);
        });

        assertEquals("User not found", thrown.getMessage());
        verify(institutionService, never()).getAllInstitutions(any());
        verify(model, never()).addAttribute(eq("institutions"), any());
    }

    @Test
    void showInstitutions_ShouldThrowException_WhenAuthenticationIsNull() {

        assertThrows(NullPointerException.class, () -> {
            homePageController.showInstitutions(model, null);
        });
    }

    @Test
    void selectInstitution_ShouldStoreIdInSessionAndRedirectToHome() {

        Long institutionId = 100L;

        String result = homePageController.selectInstitution(institutionId, session);

        assertEquals("redirect:/home", result);
        assertEquals(institutionId, session.getAttribute("selectedInstId"));
    }

    @Test
    void selectInstitution_ShouldHandleNullId() {

        String result = homePageController.selectInstitution(null, session);

        assertEquals("redirect:/home", result);
        assertNull(session.getAttribute("selectedInstId"));
    }

    @Test
    void selectInstitution_ShouldOverrideExistingSessionAttribute() {

        Long initialId = 100L;
        Long newId = 200L;
        session.setAttribute("selectedInstId", initialId);

        String result = homePageController.selectInstitution(newId, session);

        assertEquals("redirect:/home", result);
        assertEquals(newId, session.getAttribute("selectedInstId"));
        assertNotEquals(initialId, session.getAttribute("selectedInstId"));
    }

    @Test
    void selectInstitution_ShouldHandleZeroId() {

        Long institutionId = 0L;

        String result = homePageController.selectInstitution(institutionId, session);

        assertEquals("redirect:/home", result);
        assertEquals(0L, session.getAttribute("selectedInstId"));
    }

    @Test
    void selectInstitution_ShouldThrowException_WhenSessionIsNull() {

        assertThrows(NullPointerException.class, () -> {
            homePageController.selectInstitution(100L, null);
        });
    }

    @Test
    void homePage_ShouldReturnHomeView_WithCorrectAttributes() {

        Long instId = 100L;
        Long expectedEmployeeCount = 25L;
        Long expectedPayrollCount = 10L;

        when(employeeService.getEmployeesCount(instId)).thenReturn(expectedEmployeeCount);
        when(payrollQueryService.getCount(instId)).thenReturn(expectedPayrollCount);

        String result = homePageController.homePage(model, instId);

        assertEquals("home", result);
        verify(model).addAttribute("EmployeeCount", expectedEmployeeCount);
        verify(model).addAttribute("PayrollCount", expectedPayrollCount);
        verify(employeeService).getEmployeesCount(instId);
        verify(payrollQueryService).getCount(instId);
    }

    @Test
    void homePage_ShouldHandleZeroCounts() {

        Long instId = 100L;
        Long expectedEmployeeCount = 0L;
        Long expectedPayrollCount = 0L;

        when(employeeService.getEmployeesCount(instId)).thenReturn(expectedEmployeeCount);
        when(payrollQueryService.getCount(instId)).thenReturn(expectedPayrollCount);

        String result = homePageController.homePage(model, instId);

        assertEquals("home", result);
        verify(model).addAttribute("EmployeeCount", 0L);
        verify(model).addAttribute("PayrollCount", 0L);
    }

    @Test
    void homePage_ShouldHandleLargeNumbers() {

        Long instId = 100L;
        Long expectedEmployeeCount = 1_000_000L;
        Long expectedPayrollCount = 500_000L;

        when(employeeService.getEmployeesCount(instId)).thenReturn(expectedEmployeeCount);
        when(payrollQueryService.getCount(instId)).thenReturn(expectedPayrollCount);

        String result = homePageController.homePage(model, instId);

        assertEquals("home", result);
        verify(model).addAttribute("EmployeeCount", expectedEmployeeCount);
        verify(model).addAttribute("PayrollCount", expectedPayrollCount);
    }

    @Test
    void homePage_ShouldThrowException_WhenEmployeeServiceThrowsException() {

        Long instId = 100L;
        RuntimeException expectedException = new RuntimeException("Database error");
        when(employeeService.getEmployeesCount(instId)).thenThrow(expectedException);

        RuntimeException thrown = assertThrows(RuntimeException.class, () -> {
            homePageController.homePage(model, instId);
        });

        assertEquals("Database error", thrown.getMessage());
        verify(payrollQueryService, never()).getCount(any());
        verify(model, never()).addAttribute(eq("PayrollCount"), any());
    }

    @Test
    void homePage_ShouldThrowException_WhenPayrollServiceThrowsException() {

        Long instId = 100L;
        when(employeeService.getEmployeesCount(instId)).thenReturn(25L);
        RuntimeException expectedException = new RuntimeException("Payroll service error");
        when(payrollQueryService.getCount(instId)).thenThrow(expectedException);

        RuntimeException thrown = assertThrows(RuntimeException.class, () -> {
            homePageController.homePage(model, instId);
        });

        assertEquals("Payroll service error", thrown.getMessage());
        verify(employeeService).getEmployeesCount(instId);
        verify(model).addAttribute("EmployeeCount", 25L);
        verify(model, never()).addAttribute(eq("PayrollCount"), any());
    }

    @Test
    void homePage_ShouldThrowException_WhenModelIsNull() {

        assertThrows(NullPointerException.class, () -> {
            homePageController.homePage(null, 100L);
        });
    }

    @Test
    void selectInstAndHomePage_ShouldWorkTogether() {

        Long instId = 100L;
        Long expectedEmployeeCount = 25L;
        Long expectedPayrollCount = 10L;

        when(employeeService.getEmployeesCount(instId)).thenReturn(expectedEmployeeCount);
        when(payrollQueryService.getCount(instId)).thenReturn(expectedPayrollCount);

        String selectResult = homePageController.selectInstitution(instId, session);

        assertEquals("redirect:/home", selectResult);
        assertEquals(instId, session.getAttribute("selectedInstId"));

        Long retrievedInstId = (Long) session.getAttribute("selectedInstId");
        String homeResult = homePageController.homePage(model, retrievedInstId);

        assertEquals("home", homeResult);
        verify(employeeService).getEmployeesCount(instId);
        verify(payrollQueryService).getCount(instId);
    }
}