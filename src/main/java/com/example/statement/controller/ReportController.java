package com.example.statement.controller;

import com.example.statement.dto.request.PayrollPageableParams;
import com.example.statement.dto.respons.ReportResponse;
import com.example.statement.repository.PayrollItemsRepository;
import com.example.statement.service.PayrollOrchestratorService;
import com.example.statement.service.manager.PayrollCommandService;
import jakarta.servlet.http.HttpSession;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/report")
public class ReportController {

    private final PayrollOrchestratorService payrollOrchestratorService;

    public ReportController(PayrollItemsRepository payrollItemsRepository,
                            PayrollCommandService payrollCommandService,
                            PayrollOrchestratorService payrollOrchestratorService) {
        this.payrollOrchestratorService = payrollOrchestratorService;
    }

    @GetMapping
    public String reportList(Model model){
        model.addAttribute("reportsList", ReportResponse.Report.values());
        model.addAttribute("reportResponse", new ReportResponse());

        return "reportsList";
    }

    @GetMapping("/year")
    public String yearlySalaryReport(
            @RequestParam(required = false) Integer year,
            Model model,
            HttpSession httpSession) {

        Long institutionId = (Long) httpSession.getAttribute("selectedInstId");

        PayrollPageableParams pageableParams = new PayrollPageableParams();
        pageableParams.setSize(120);

        Page<ReportResponse> response = payrollOrchestratorService.getEmployeesSalary(year, institutionId, pageableParams.getPageable());

        model.addAttribute("items", response.getContent());
        model.addAttribute("totalPages", response.getTotalPages());
        model.addAttribute("totalElements", response.getTotalElements());
        model.addAttribute("currentPage", response.getNumber());

        return "payrollYear";
    }

    @PostMapping("/select")
    public String showReportSelector(@RequestParam ("report") String report){
        if (report.equals("EMPL_YEAR_PAY")) {
            return "payrollYear";
        }
        return null;
    }
}
