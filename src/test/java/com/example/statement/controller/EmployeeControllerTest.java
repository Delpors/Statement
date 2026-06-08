package com.example.statement.controller;

import com.example.statement.dto.request.EmployeeRequest;
import com.example.statement.dto.response.EmployeeResponse;
import com.example.statement.service.IEmployeeService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.servlet.view.InternalResourceViewResolver;

import java.math.BigDecimal;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class EmployeeControllerTest {

    @Mock
    private IEmployeeService employeeService;

    @InjectMocks
    private EmployeeController employeeController;

    private MockMvc mockMvc;
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {

        InternalResourceViewResolver viewResolver = new InternalResourceViewResolver();
        viewResolver.setPrefix("/WEB-INF/views/");
        viewResolver.setSuffix(".jsp");

        mockMvc = MockMvcBuilders
                .standaloneSetup(employeeController)
                .setViewResolvers(viewResolver)
                .build();

        objectMapper = new ObjectMapper();
    }

    @Test
    void getAllEmployees_WhenInstIdExists_ShouldReturnEmployeesPage() throws Exception {
        Long instId = 1L;

        mockMvc.perform(get("/employees/list")
                        .sessionAttr("selectedInstId", instId))
                .andExpect(status().isOk())
                .andExpect(view().name("employees"))
                .andExpect(model().attributeExists("employees"));

        verify(employeeService, times(1))
                .getAllActiveEmployeesByInstitutionId(instId);
    }

    @Test
    void getAllEmployees_WhenInstIdIsNull_ShouldRedirectToRoot() throws Exception {
        mockMvc.perform(get("/employees/list"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/"));

        verify(employeeService, never())
                .getAllActiveEmployeesByInstitutionId(any());
    }

    @Test
    void showCreateForm_ShouldReturnCreateEmployeePage() throws Exception {
        mockMvc.perform(get("/employees/create"))
                .andExpect(status().isOk())
                .andExpect(view().name("createEmployee"))
                .andExpect(model().attributeExists("employee"));
    }

    @Test
    void createEmployee_WhenInstIdExists_ShouldCreateAndRedirect() throws Exception {
        Long instId = 1L;
        EmployeeRequest request = new EmployeeRequest(
                "Иван", "Иванов", "Иванович",
                "Разработчик", BigDecimal.valueOf(5000),
                BigDecimal.valueOf(100000), "4070281012345678",
                "ivan@example.com"
        );

        mockMvc.perform(post("/employees/create")
                        .sessionAttr("selectedInstId", instId)
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .param("name", request.name())
                        .param("surName", request.surName())
                        .param("lastname", request.lastname())
                        .param("position", request.position())
                        .param("nonTaxable", request.nonTaxable().toString())
                        .param("salary", request.salary().toString())
                        .param("bankAccount", request.bankAccount())
                        .param("email", request.email()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/employees/list"));

        verify(employeeService, times(1))
                .createEmployeeForInstitution(request, instId);
    }

    @Test
    void createEmployee_WhenInstIdIsNull_ShouldRedirectToRoot() throws Exception {
        EmployeeRequest request = new EmployeeRequest(
                "Иван", "Иванов", "Иванович",
                "Разработчик", BigDecimal.valueOf(5000),
                BigDecimal.valueOf(100000), "4070281012345678",
                "ivan@example.com"
        );

        mockMvc.perform(post("/employees/create")
                        .param("name", request.name())
                        .param("surName", request.surName())
                        .param("lastname", request.lastname())
                        .param("position", request.position())
                        .param("nonTaxable", request.nonTaxable().toString())
                        .param("salary", request.salary().toString())
                        .param("bankAccount", request.bankAccount())
                        .param("email", request.email()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/"));

        verify(employeeService, never())
                .createEmployeeForInstitution(any(), any());
    }

    @Test
    void showEditForm_ShouldReturnEditEmployeePage() throws Exception {
        Long employeeId = 10L;
        EmployeeResponse response = new EmployeeResponse(
                employeeId, 1L, "Иванов", "Иван", "Иванович",
                "Разработчик", BigDecimal.valueOf(5000), BigDecimal.valueOf(100000),
                "4070281012345678", "ivan@example.com", true, null
        );

        when(employeeService.getEmployeeById(employeeId)).thenReturn(response);

        mockMvc.perform(get("/employees/edit/{id}", employeeId))
                .andExpect(status().isOk())
                .andExpect(view().name("editEmployee"))
                .andExpect(model().attribute("employee", response));

        verify(employeeService, times(1)).getEmployeeById(employeeId);
    }

    @Test
    void updateEmployee_WhenInstIdExists_ShouldUpdateAndRedirect() throws Exception {
        Long employeeId = 10L;
        Long instId = 1L;
        EmployeeRequest request = new EmployeeRequest(
                "Петр", "Петров", "Петрович",
                "Старший разработчик", BigDecimal.valueOf(7000),
                BigDecimal.valueOf(150000), "4070281098765432",
                "petr@example.com"
        );

        mockMvc.perform(post("/employees/update/{id}", employeeId)
                        .sessionAttr("selectedInstId", instId)
                        .param("name", request.name())
                        .param("surName", request.surName())
                        .param("lastname", request.lastname())
                        .param("position", request.position())
                        .param("nonTaxable", request.nonTaxable().toString())
                        .param("salary", request.salary().toString())
                        .param("bankAccount", request.bankAccount())
                        .param("email", request.email()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/employees/list"));

        verify(employeeService, times(1))
                .updateEmployee(eq(employeeId), eq(request), eq(instId));
    }

    @Test
    void updateEmployee_WhenInstIdIsNull_ShouldRedirectToRoot() throws Exception {
        Long employeeId = 10L;
        EmployeeRequest request = new EmployeeRequest(
                "Петр", "Петров", "Петрович",
                "Разработчик", BigDecimal.valueOf(5000),
                BigDecimal.valueOf(100000), "4070281098765432",
                "petr@example.com"
        );

        mockMvc.perform(post("/employees/update/{id}", employeeId)
                        .param("name", request.name())
                        .param("surName", request.surName())
                        .param("lastname", request.lastname())
                        .param("position", request.position())
                        .param("nonTaxable", request.nonTaxable().toString())
                        .param("salary", request.salary().toString())
                        .param("bankAccount", request.bankAccount())
                        .param("email", request.email()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/"));

        verify(employeeService, never())
                .updateEmployee(anyLong(), any(), anyLong());
    }

    @Test
    void deleteEmployee_ShouldDeleteAndRedirect() throws Exception {
        Long employeeId = 10L;

        mockMvc.perform(delete("/employees/{id}", employeeId))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/employees/list"));

        verify(employeeService, times(1)).deleteEmployee(employeeId);
    }
}