package dev.dobicinaitis.scavengerhunt.controllers;

import dev.dobicinaitis.scavengerhunt.services.QuestService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
@AllArgsConstructor
public class HtmlPageController {

    private QuestService service;

    @GetMapping(value = {"/", "/index"})
    public String index(Model model) {
        model.addAttribute("info", service.getInfo());
        model.addAttribute("message", service.getInfo().getIntro());
        return "index";
    }

    @GetMapping(value = {"/{code}"})
    public String index(@PathVariable String code, Model model) {
        model.addAttribute("info", service.getInfo());
        model.addAttribute("message", service.getInfo().getIntro());
        model.addAttribute("code", code);
        return "index";
    }
}
