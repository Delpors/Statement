package com.example.statement.controller;

import com.example.statement.dto.request.EmployeeRequest;
import com.example.statement.dto.respons.EmployeeResponse;
import com.example.statement.entity.EmployeeEntity;
import com.example.statement.service.EmployeeService;
import jakarta.servlet.http.HttpSession;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.ui.Model;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@ExtendWith(MockitoExtension.class)
public class EmployeeControllerTest {
    @Mock
    private Model model;

    @Mock
    private HttpSession session;

    @Mock
    private EmployeeService employeeService;

    @InjectMocks
    private EmployeeController employeeController;

    @Test
    void getAllEmployeesTest(){
        Long instId = 1L;
        Long emplId = 1L;
        LocalDate localDate = LocalDate.now();
        List<EmployeeResponse> employees = List.of(
                new EmployeeResponse(
                        emplId, instId,"Рашидов","Сабир",
                        "Гаджикеримович","Бухгалтер",
                        new BigDecimal("1400"), new BigDecimal("8000"),
                        "1111","@mail",false, localDate
                )
        );

        when(session.getAttribute("selectedInstId")).thenReturn(instId);
        when(employeeService.getAllEmployees(instId)).thenReturn(employees);

        String viewName = employeeController.getAllEmployees(model, session);

        assertEquals("employees", viewName);
        verify(session).getAttribute("selectedInstId");
        verify(employeeService).getAllEmployees(instId);
        verify(model).addAttribute("employees", employees);

    }

    @Test
    void showCreateFormTest(){
        String viewName = employeeController.showCreateForm(model);

        assertEquals("createEmployee",viewName);
        verify(model).addAttribute(eq("employee"), any(EmployeeEntity.class));
    }

    @Test
    void createEmployeeTest(){
        Long instId = 1L;

        EmployeeRequest employees = new EmployeeRequest(

                "Рашидов","Сабир","Гаджикеримович",
                "Бухгалтер",new BigDecimal("1400"),
                new BigDecimal("8000"),"1111","@mail"
        );

        when(session.getAttribute("selectedInstId")).thenReturn(instId);
        doNothing().when(employeeService).createEmployee(employees,instId);

        String viewName = employeeController.createEmployee(employees,session);

        assertEquals("redirect:/employees", viewName);
    }

    @Test
    void showEditFormTest(){

        EmployeeResponse employee = new EmployeeResponse(
                1L, 1L,"Рашидов","Сабир","Гаджикеримович",
                "Бухгалтер", new BigDecimal("1400"), new BigDecimal("8000"),
                "1111","@mail",false, LocalDate.now()
        );

        when(employeeService.getEmployeeDTOById(1L)).thenReturn(employee);
        String viewName = employeeController.showEditForm(1L,model);

        assertEquals("editEmployee", viewName);
        verify(employeeService).getEmployeeDTOById(1L);
        verify(model).addAttribute("employee",employee);
    }

    @Test
    void updateEmployeeTest(){
        Long instId = 1L;
        EmployeeRequest request = new EmployeeRequest(
                "Рашидов","Сабир","Гаджикеримович",
                "Бухгалтер",new BigDecimal("1400"),
                new BigDecimal("8000"),"1111","@mail"
        );

        when(session.getAttribute("selectedInstId")).thenReturn(instId);
        doNothing().when(employeeService).updateEmployee(request, instId);

        String viewName = employeeController.updateEmployee(request, session);
        assertEquals("redirect:/employees", viewName);

        verify(session).getAttribute("selectedInstId");
        verify(employeeService).updateEmployee(request,instId);
    }

    @Test
    void deleteEmployeeTest(){
        doNothing().when(employeeService).deleteEmployee(1L);

        String viewName = employeeController.deleteEmployee(1L);

        assertEquals("redirect:/employees", viewName);
        verify(employeeService).deleteEmployee(1L);
    }

}
