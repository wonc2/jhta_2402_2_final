package org.example.jhta_2402_2_final.controller;

import lombok.RequiredArgsConstructor;
import org.example.jhta_2402_2_final.model.dto.Employee;
import org.example.jhta_2402_2_final.service.AdminMainService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@Controller
@RequestMapping("/adminMain")
public class AdminMainController {

    private final AdminMainService adminMainService;

    @GetMapping
    public String adminMainPage(Model model,
                                @RequestParam(name = "category", required = false) String category,
                                @RequestParam(name = "keyword", required = false) String keyword) {
        List<Employee> emp = (keyword != null && !keyword.isEmpty())
                ? adminMainService.searchEmp(category, keyword)
                : adminMainService.getAllEmp();
        model.addAttribute("emp", emp);
        return "/adminMainPage";
    }

    @PostMapping("/delete/{id}")
    public String deleteEmp(@PathVariable Long id) {
        adminMainService.deleteEmp(id);
        return "redirect:/adminMain";
    }

    @PostMapping("/update")
    public String updateEmp(@ModelAttribute Employee employee) {
        adminMainService.updateEmp(employee);
        return "redirect:/adminMain";
    }

    @PostMapping("/insert")
    public String insertEmp(@ModelAttribute Employee employee) {
        adminMainService.createEmp(employee);
        return "redirect:/adminMain";
    }

}
