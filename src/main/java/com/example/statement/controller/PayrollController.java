package com.example.statement.controller;

import com.example.statement.dto.request.PayrollItemRequest;
import com.example.statement.dto.request.PayrollPageableParams;
import com.example.statement.dto.response.PayrollItemsResponse;
import com.example.statement.service.manager.IPayrollCommandService;
import com.example.statement.service.query.IPayrollQueryService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/payroll")
public class PayrollController {

    private final IPayrollCommandService payrollCommandService;
    private final IPayrollQueryService payrollQueryService;

    @GetMapping
    public String showPayroll(Model model, HttpSession session)
    {
        Long instId = (Long) session.getAttribute("selectedInstId");

        model.addAttribute("payroll", payrollQueryService.getAllPayrolls(instId));

        return "payroll";
    }

    @GetMapping("/{payroll_id}")
    public String deletePayroll(@PathVariable Long payrollId)
    {
        payrollCommandService.deletePayroll(payrollId);
        return "redirect:/payroll";
    }
    
    @GetMapping("/payrollItems/create")
    public String showPayrollItemsCreateForm(Model model, HttpSession session)
    {
        Long instId = (Long) session.getAttribute("selectedInstId");

        model.addAttribute("items", payrollQueryService.getIEmployeeItems(instId));

        return "createPayrollItems";
    }

    @PostMapping("/payrollItems/create")
    public String createPayrollItems(@RequestBody List<PayrollItemRequest> requests,
                                     HttpSession session)
    {
        Long instId = (Long) session.getAttribute("selectedInstId");
        if (instId==null){
            throw new IllegalStateException("Организация не выбрана!");
        }

        System.out.println("Платежная ведомость за " + requests.getFirst().month());

        payrollCommandService.createOrUpdatePayroll(requests, instId);
        return "redirect:/payroll?success";
    }

    @GetMapping("/payrollItems/{payrollId}")
    public String showPayrollItems(
            @PathVariable Long payrollId,
            @ModelAttribute PayrollPageableParams params,
            Model model, HttpSession session)
    {

        Pageable pageable = params.getPageable();

        setupPayrollData(model, payrollId, session, pageable);
        return "payrollItems";
    }

    @GetMapping("/payrollItems/edit/{payrollId}")
    public String showPayrollItemsEditForm(
            @PathVariable Long payrollId,
            @ModelAttribute PayrollPageableParams params,
            Model model, HttpSession session)
    {

        Pageable pageable = params.getPageable();
        setupPayrollData(model, payrollId, session, pageable);
        return "editPayrollItems";
    }

    @GetMapping("/payrollItems/delete/{id}")
    public String deletePayrollItem(@PathVariable("id") Long payrollItemId,
                                    HttpSession session)
    {

        payrollCommandService.deletePayrollItem(payrollItemId);

        LocalDate date = (LocalDate) session.getAttribute("currentPaymentDate");
        return "redirect:/payroll/payrollItems/edit/" + date;
    }

    private void setupPayrollData(Model model, Long payrollId,
                                  HttpSession session, Pageable pageable)
    {

        Long selectedInst = (Long) session.getAttribute("selectedInstId");

        Page<PayrollItemsResponse> payrollPage = payrollQueryService.getPayrollItems(payrollId, selectedInst, pageable);

        model.addAttribute("payroll_items", payrollPage.getContent());
        model.addAttribute("currentPage", payrollPage.getNumber());
        model.addAttribute("totalPages", payrollPage.getTotalPages());
        model.addAttribute("totalItems", payrollPage.getTotalElements());
    }

    @ModelAttribute("params")
    public PayrollPageableParams setupParams(
            @RequestParam(defaultValue = "totalIssued") String sortBy,
            @RequestParam(defaultValue = "asc") String direction,
            @RequestParam(defaultValue = "0") Integer page,
            @RequestParam(defaultValue = "50") Integer size
    ){
        PayrollPageableParams params = new PayrollPageableParams();

        params.setSortBy(sortBy);
        params.setDirection(direction);
        params.setPage(page);
        params.setSize(size);

        return params;
    }
}
