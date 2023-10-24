package com.petralib.auth;

import com.petralib.auth.security.model.LoginModel;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.ui.Model;

@Controller("/auth")
public class LoginController {

    @GetMapping(value = "/login")
    public String getLoginForm(Model model){
        model.addAttribute("login", new LoginModel());
        return "login";
    }

    @PostMapping("/login")
    public String greetingSubmit(@ModelAttribute LoginModel loginModel, Model model) {
        model.addAttribute("login", loginModel);

        return "projects";
    }
}
