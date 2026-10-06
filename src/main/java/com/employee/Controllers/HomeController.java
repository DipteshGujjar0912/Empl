package com.employee.Controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.employee.DTO.OrganizationDto;
import com.employee.Entities.Organization;
import com.employee.Exception.EmailNotFoundException;
import com.employee.Exception.InvalidPasswordException;
import com.employee.Sevices.OrganizationService;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;


@Controller
public class HomeController {

    @Autowired
    private OrganizationService organizationService;

    @GetMapping("/home")
    public String index(Model model) {
        model.addAttribute("showSidebar", false);
        return "index";
    }

    @GetMapping("/")
    public String home(Model model) {
        model.addAttribute("showSidebar", false);
        return "index";
    }

    @GetMapping("/about") 
    public String about(Model model) {
        model.addAttribute("showSidebar", false);
        return "about";
    }

    @GetMapping("/contact") 
    public String contact(Model model) {
        model.addAttribute("showSidebar", false);
        return "contact";
    }

    @GetMapping("/login") 
    public String login(Model model) {
        model.addAttribute("showSidebar", false);
        return "login";
    }

    @GetMapping("/register") 
    public String register(Model model) {
        model.addAttribute("showSidebar", false);
        model.addAttribute("org", new OrganizationDto());
        return "register";
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/";
    }


    // ========================= Processing =========================

    @PostMapping("/do-login")
    public String doLogin(
            @RequestParam(required = false) String email,
            @RequestParam(required = false) String password,
            HttpSession session,
            Model model) {

        System.out.println("Email is : "+email);
        System.out.println("Password is : "+password);
        if (email == null || email.isBlank() && password.isBlank() || password == null) {
            model.addAttribute("error", "All fields are required");
            return "login";
        }

        try {
            Organization org = organizationService.login(email, password);

            session.setAttribute("loggedInUser", org);
            return "redirect:/admin/";

        } catch (EmailNotFoundException e) {
            model.addAttribute("error", e.getMessage());
            return "login";
        } catch (InvalidPasswordException e) {
            model.addAttribute("error", e.getMessage());
            return "login";
        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
            return "login";
        }

        // return "login";
        // session.setAttribute("loggedInUser", "org");
        // return "redirect:/admin/";
    }

    @PostMapping("/do-register")
    public String doRegister(
            @Valid @ModelAttribute("org") OrganizationDto dto,
            BindingResult result,
            Model model) {

        // password match check
        if (!dto.getPassword().equals(dto.getConfirmPassword())) {
            model.addAttribute("error", "Passwords do not match");
            return "register";
        }

        if (result.hasErrors()) {
            return "register";
        }

        try {
            organizationService.register(dto);
            model.addAttribute("success", "Registration Successful!");
            return "login";

        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
            return "register";
        }
    }
}
