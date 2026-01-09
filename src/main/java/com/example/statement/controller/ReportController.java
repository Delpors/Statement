package com.example.statement.controller;

import com.example.statement.dto.request.PayrollPageableParams;
import com.example.statement.dto.respons.PayrollItemsResponse;
import com.example.statement.dto.respons.PayrollSummaryResponse;
import com.example.statement.repository.PayrollItemsRepository;
import com.example.statement.service.PayrollOrchestratorService;
import com.example.statement.service.manager.PayrollCommandService;
import jakarta.servlet.http.HttpSession;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@Controller
@RequestMapping("/report")
public class ReportController {

    private final PayrollOrchestratorService payrollOrchestratorService;

    public ReportController(PayrollItemsRepository payrollItemsRepository,
                            PayrollCommandService payrollCommandService,
                            PayrollOrchestratorService payrollOrchestratorService) {
        this.payrollOrchestratorService = payrollOrchestratorService;
    }

    @GetMapping("/year")
    public String yearlySalaryReport(
            @RequestParam(required = false) Integer year,
            Model model,
            HttpSession httpSession) {

        Long institutionId = (Long) httpSession.getAttribute("selectedInstId");

        PayrollPageableParams pageableParams = new PayrollPageableParams();
        pageableParams.setSize(120);

        Page<PayrollSummaryResponse> response = payrollOrchestratorService.getEmployeesSalary(year, institutionId, pageableParams.getPageable());

        model.addAttribute("items", response.getContent());
        model.addAttribute("totalPages", response.getTotalPages());
        model.addAttribute("totalElements", response.getTotalElements());
        model.addAttribute("currentPage", response.getNumber());

        return "payrollYear";
    }
}
