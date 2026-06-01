package com.example.statement.controller;

import com.example.statement.dto.request.PayrollPageableParams;
import com.example.statement.dto.response.ReportResponse;
import com.example.statement.dto.response.TaxesResponse;
import com.example.statement.service.TaxCalculate;
import com.example.statement.service.query.IPayrollQueryService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Controller
@RequestMapping("/report")
@RequiredArgsConstructor
public class ReportController {

    private final TaxCalculate calculate;
    private final IPayrollQueryService payrollQueryService;

    @GetMapping
    public String reportList(Model model){
        model.addAttribute("reportsList", ReportResponse.Report.values());
        model.addAttribute("reportResponse", new ReportResponse());

        return "reportsList";
    }

    @GetMapping("/yearSalary")
    public String yearlySalaryReport(
            @RequestParam(required = false) Integer year,
            Model model,
            HttpSession httpSession) {

        Long institutionId = (Long) httpSession.getAttribute("selectedInstId");

        PayrollPageableParams pageableParams = new PayrollPageableParams();
        pageableParams.setSize(120);

        Page<ReportResponse> response = payrollQueryService.getEmployeesYearSalary(year, institutionId, pageableParams.getPageable());

        model.addAttribute("items", response.getContent());
        model.addAttribute("totalPages", response.getTotalPages());
        model.addAttribute("totalElements", response.getTotalElements());
        model.addAttribute("currentPage", response.getNumber());

        return "payrollYear";
    }

    @GetMapping("/taxes")
    public String yearSalaryTaxes(@RequestParam(required = false) Integer year,
                                  Model model, HttpSession httpSession){

        Long institutionId = (Long) httpSession.getAttribute("selectedInstId");
        Map<Integer, TaxesResponse> yearTaxes = calculate.getAllFromYearTaxes(year, institutionId);
        TaxesResponse taxes = yearTaxes.get(0);

        model.addAttribute("yearTaxes", yearTaxes);
        model.addAttribute("totalInkomTax", taxes.getTotalInkomTax());
        model.addAttribute("totalUnionFee", taxes.getTotalUnionFee());
        model.addAttribute("totalPfrTax", taxes.getTotalPfrTax());
        model.addAttribute("totalFssTax", taxes.getTotalFssTax());
        model.addAttribute("grandTotal", taxes.getGrandTotal());

        return "salaryTaxes";
    }

    @PostMapping("/select")
    public String showReportSelector(@RequestParam ("report") String report){
        if (report.equals("EMPL_YEAR_PAY")) {
            return "payrollYear";
        }else if (report.equals("TAXES")){
            return "salaryTaxes";
        }
        return null;
    }
}
