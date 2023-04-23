package tech.xavi.flatbot.controller;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import tech.xavi.flatbot.dto.LoginPayload;
import tech.xavi.flatbot.repository.AdRepository;

@Controller
@PropertySource("classpath:flatbot.properties")
@RequiredArgsConstructor
public class SiteController {

    @Value("${tech.xavi.flatbot.site.user}")
    private String USER;
    @Value("${tech.xavi.flatbot.site.pass}")
    private String PASSWORD;
    private final AdRepository adRepository;
    @GetMapping("/login")
    public String showLoginForm(Model model) {
        LoginPayload loginPayload = new LoginPayload();
        model.addAttribute("loginForm", loginPayload);
        return "login";
    }

    @PostMapping("/login")
    public String processLogin(@ModelAttribute("loginForm") LoginPayload loginPayload, HttpSession session){
        if (isValidUser(loginPayload)){
            return "panel";
        } else {
            return "redirect:/login";
        }
    }

    @GetMapping("/panel")
    public String panel(Model model, HttpSession session){
        model.addAttribute("adList",adRepository.findAll());
        return "panel";
    }

    private boolean isValidUser(LoginPayload loginPayload){
        return loginPayload.getUsername().equals(USER)
                && loginPayload.getPassword().equals(PASSWORD);
    }
}
