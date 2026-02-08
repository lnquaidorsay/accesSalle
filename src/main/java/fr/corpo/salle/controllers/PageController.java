package fr.corpo.salle.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class PageController {

    @GetMapping("/auth")
    public String authPage(Model model) {
        model.addAttribute("isAuthenticated", false);
        return "auth";
    }
}
