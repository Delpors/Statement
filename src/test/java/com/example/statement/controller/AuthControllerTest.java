package com.example.statement.controller;

import com.example.statement.dto.request.RegisterRequest;
import com.example.statement.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.validation.BindingResult;
import org.springframework.web.servlet.view.InternalResourceViewResolver;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class AuthControllerTest {

    @Mock
    private UserService userService;

    @InjectMocks
    private AuthController authController;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {

        InternalResourceViewResolver viewResolver = new InternalResourceViewResolver();
        viewResolver.setPrefix("/WEB-INF/views/");
        viewResolver.setSuffix(".jsp");

        mockMvc = MockMvcBuilders
                .standaloneSetup(authController)
                .setViewResolvers(viewResolver)
                .build();
    }

    @Test
    void showRegistrationForm_ShouldReturnRegisterPage() throws Exception {
        mockMvc.perform(get("/register"))
                .andExpect(status().isOk())
                .andExpect(view().name("register"))
                .andExpect(model().attributeExists("user"));
    }

    @Test
    void registerUser_WithValidData_ShouldCreateUserAndRedirect() throws Exception {
        RegisterRequest request = createValidRegisterRequest();

        when(userService.isUsernameExists(request.getUserName())).thenReturn(false);

        mockMvc.perform(post("/register")
                        .param("userName", request.getUserName())
                        .param("password", request.getPassword())
                        .param("confirmPassword", request.getConfirmPassword())
                        .param("email", request.getEmail()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/login?registered=true"));

        verify(userService, times(1)).createUser(any(RegisterRequest.class));
        verify(userService, times(1)).isUsernameExists(request.getUserName());
    }

    @Test
    void registerUser_WithPasswordsMismatch_ShouldReturnRegisterPageWithError() throws Exception {
        RegisterRequest request = createValidRegisterRequest();
        request.setConfirmPassword("differentPassword");

        mockMvc.perform(post("/register")
                        .param("userName", request.getUserName())
                        .param("password", request.getPassword())
                        .param("confirmPassword", request.getConfirmPassword())
                        .param("email", request.getEmail()))
                .andExpect(status().isOk())
                .andExpect(view().name("register"))
                .andExpect(model().attributeExists("registrationRequest"))
                .andExpect(model().hasErrors());

        verify(userService, never()).createUser(any());
    }

    @Test
    void registerUser_WithExistingUsername_ShouldReturnRegisterPageWithError() throws Exception {
        RegisterRequest request = createValidRegisterRequest();

        when(userService.isUsernameExists(request.getUserName())).thenReturn(true);

        mockMvc.perform(post("/register")
                        .param("userName", request.getUserName())
                        .param("password", request.getPassword())
                        .param("confirmPassword", request.getConfirmPassword())
                        .param("email", request.getEmail()))
                .andExpect(status().isOk())
                .andExpect(view().name("register"))
                .andExpect(model().hasErrors());

        verify(userService, never()).createUser(any());
        verify(userService, times(1)).isUsernameExists(request.getUserName());
    }

    @Test
    void registerUser_WithEmptyFields_ShouldReturnRegisterPageWithErrors() throws Exception {
        mockMvc.perform(post("/register")
                        .param("userName", "")
                        .param("password", "")
                        .param("confirmPassword", "")
                        .param("email", ""))
                .andExpect(status().isOk())
                .andExpect(view().name("register"))
                .andExpect(model().hasErrors());

        verify(userService, never()).createUser(any());
    }

    @Test
    void registerUser_WithInvalidEmail_ShouldReturnRegisterPageWithErrors() throws Exception {
        RegisterRequest request = createValidRegisterRequest();
        request.setEmail("invalid-email");

        mockMvc.perform(post("/register")
                        .param("userName", request.getUserName())
                        .param("password", request.getPassword())
                        .param("confirmPassword", request.getConfirmPassword())
                        .param("email", request.getEmail()))
                .andExpect(status().isOk())
                .andExpect(view().name("register"))
                .andExpect(model().hasErrors());

        verify(userService, never()).createUser(any());
    }

    @Test
    void loginPage_WithoutParameters_ShouldReturnLoginPage() throws Exception {
        mockMvc.perform(get("/login"))
                .andExpect(status().isOk())
                .andExpect(view().name("login"))
                .andExpect(model().attributeDoesNotExist("error"))
                .andExpect(model().attributeDoesNotExist("message"));
    }

    @Test
    void loginPage_WithErrorParameter_ShouldAddErrorMessage() throws Exception {
        mockMvc.perform(get("/login").param("error", "true"))
                .andExpect(status().isOk())
                .andExpect(view().name("login"))
                .andExpect(model().attribute("error", "Неверное имя пользователя или пароль"))
                .andExpect(model().attributeDoesNotExist("message"));
    }

    @Test
    void loginPage_WithLogoutParameter_ShouldAddLogoutMessage() throws Exception {
        mockMvc.perform(get("/login").param("logout", "true"))
                .andExpect(status().isOk())
                .andExpect(view().name("login"))
                .andExpect(model().attribute("message", "Вы успешно вышли из системы"))
                .andExpect(model().attributeDoesNotExist("error"));
    }

    @Test
    void loginPage_WithRegisteredParameter_ShouldAddRegistrationSuccessMessage() throws Exception {
        mockMvc.perform(get("/login").param("registered", "true"))
                .andExpect(status().isOk())
                .andExpect(view().name("login"))
                .andExpect(model().attribute("message", "Регистрация успешна! Войдите в систему"))
                .andExpect(model().attributeDoesNotExist("error"));
    }

    @Test
    void loginPage_WithMultipleParameters_ShouldAddBothMessages() throws Exception {
        mockMvc.perform(get("/login")
                        .param("error", "true")
                        .param("logout", "true"))
                .andExpect(status().isOk())
                .andExpect(view().name("login"))
                .andExpect(model().attribute("error", "Неверное имя пользователя или пароль"))
                .andExpect(model().attribute("message", "Вы успешно вышли из системы"));
    }

    @Test
    void loginPage_WithAllParameters_ShouldAddAllMessages() throws Exception {
        mockMvc.perform(get("/login")
                        .param("error", "true")
                        .param("logout", "true")
                        .param("registered", "true"))
                .andExpect(status().isOk())
                .andExpect(view().name("login"))
                .andExpect(model().attribute("error", "Неверное имя пользователя или пароль"))
                .andExpect(model().attribute("message", "Вы успешно вышли из системы"));
    }

    private RegisterRequest createValidRegisterRequest() {
        RegisterRequest request = new RegisterRequest();
        request.setUserName("testuser");
        request.setPassword("password123");
        request.setConfirmPassword("password123");
        request.setEmail("test@example.com");
        return request;
    }
}