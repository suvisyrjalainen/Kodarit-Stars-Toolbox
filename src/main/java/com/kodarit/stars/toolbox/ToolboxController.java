package com.kodarit.stars.toolbox;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ToolboxController {
    // Set up a basic mapping that maps a path (/) to a template file.
    // The method name is irrelevant but should be reasonable
    // We can do more advanced logic in these methods later,
    // but for now we just return the name of a template.
    @GetMapping("/")
    public String index() {
        return "index";
    }

    @GetMapping("/test")
    public String test() {
        return "test";
    }

    @GetMapping("/suvintest")
    public String suvintest() {
        return "suvin_test";
    }

    @GetMapping("/data_url_converter")
    public String data_url_converter() {
        return "data_url_converter";
    }

    @GetMapping("/base_converter")
    public String baseConverter(Model model) {
        model.addAttribute("pageCss", "base_converter.css");
        model.addAttribute("pageJs", "base_converter.js");
        return "base_converter";
    }
}
