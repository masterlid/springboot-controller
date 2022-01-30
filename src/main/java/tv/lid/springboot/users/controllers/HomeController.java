package tv.lid.springboot.users.controllers;

import java.util.Map;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class HomeController extends HtmlController {
    @GetMapping("/")
    public ModelAndView home(final Map<String, Object> model) {
        return new ModelAndView("layouts/home", model);
    }
}
