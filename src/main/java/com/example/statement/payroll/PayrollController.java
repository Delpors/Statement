package com.example.statement.payroll;

import com.example.statement.payroll_items.PayrollItemsDTO;
import jakarta.servlet.http.HttpSession;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDate;
import java.util.List;

@Controller
@RequestMapping("/payroll")
public class PayrollController {

    PayrollService payrollService;

    public PayrollController(PayrollService payrollService){
        this.payrollService = payrollService;
    }

    @GetMapping
    public String showPayroll(Model model, HttpSession session) {
        Long selectedInstId = (Long) session.getAttribute("selectedInstId");

        model.addAttribute("payroll",payrollService.getAllPayrolls(selectedInstId));

        return "payroll";
    }

    @GetMapping("/{payroll_id}")
    public String deletePayroll(@PathVariable Long payroll_id)
    {
        payrollService.deletePayroll(payroll_id);
        return "redirect:/payroll";
    }
    
    @GetMapping("/payrollItems/create")
    public String showPayrollItemsCreateForm(Model model, HttpSession session) {
        Long selectedInstId = (Long) session.getAttribute("selectedInstId");

        model.addAttribute("items",payrollService.createPayrollItems(selectedInstId));

        return "createPayrollItems";
    }

    @PostMapping("/payrollItems/create")
    public String createPayrollItems(@RequestBody List<PayrollItemsDTO> payrollItemsDTOS,
                                     HttpSession session){

        Long selectedInstId = (Long) session.getAttribute("selectedInstId");
        if (selectedInstId==null){
            throw new IllegalStateException("Организация не выбрана!");
        }

        System.out.println("Received DTOs: " + payrollItemsDTOS);
        if (payrollItemsDTOS != null && !payrollItemsDTOS.isEmpty()) {
            System.out.println("First DTO fields:");
            PayrollItemsDTO first = payrollItemsDTOS.getFirst();
            System.out.println("paymentDate: " + first.paymentDate());
            System.out.println("All fields: " + first);
        }

        LocalDate payrollData = payrollItemsDTOS.getFirst().paymentDate();
        System.out.println("Дата ведомости "+payrollData);

        payrollService.createOrUpdatePayroll(payrollData, payrollItemsDTOS, selectedInstId);

        return "redirect:/payroll?success";
    }

    @GetMapping("/payrollItems/{payrollId}")
    public String showPayrollItems(
            @PathVariable Long payrollId,
            @ModelAttribute PayrollRequestParams params,
            Model model, HttpSession session)
    {

        Pageable pageable = params.getPageable();

        setupPayrollData(model, payrollId, session, pageable);
        return "payrollItems";
    }

    @GetMapping("/payrollItems/edit/{payrollId}")
    public String showPayrollItemsEditForm(
            @PathVariable Long payrollId,
            @ModelAttribute PayrollRequestParams params,
            Model model, HttpSession session)
    {


        Pageable pageable = params.getPageable();
        setupPayrollData(model, payrollId, session, pageable);
        return "editPayrollItems";
    }

    @GetMapping("/payrollItems/delete/{id}")
    public String deletePayrollItem(@PathVariable("id") Long payrollItemId,
                                    HttpSession session){

        payrollService.deletePayrollItem(payrollItemId);

        LocalDate date = (LocalDate) session.getAttribute("currentPaymentDate");
        return "redirect:/payroll/payrollItems/edit/" + date;
    }

    @GetMapping("/report/create")
    public String showYearPayroll(Model model, HttpSession httpSession){


        return "payrollYear";
    }

    private void setupPayrollData(Model model, Long payrollId,
                                  HttpSession session, Pageable pageable){

        Long selectedInst = (Long) session.getAttribute("selectedInstId");
        Page<PayrollItemsDTO> payrollPage = payrollService.
                getPayrollItems(payrollId, selectedInst, pageable);

        model.addAttribute("payroll_items", payrollPage.getContent());
        model.addAttribute("currentPage", payrollPage.getNumber());
        model.addAttribute("totalPages", payrollPage.getTotalPages());
        model.addAttribute("totalItems", payrollPage.getTotalElements());
    }

    @ModelAttribute("params")
    public PayrollRequestParams setupParams(
            @RequestParam(defaultValue = "totalIssued") String sortBy,
            @RequestParam(defaultValue = "asc") String direction,
            @RequestParam(defaultValue = "0") Integer page,
            @RequestParam(defaultValue = "50") Integer size
    ){
        PayrollRequestParams params = new PayrollRequestParams();

        params.setSortBy(sortBy);
        params.setDirection(direction);
        params.setPage(page);
        params.setSize(size);

        return params;
    }
}
