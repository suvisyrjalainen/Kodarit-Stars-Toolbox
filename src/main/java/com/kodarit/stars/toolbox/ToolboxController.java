package com.kodarit.stars.toolbox;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ToolboxController {
    @GetMapping("/")
    public String index() {
        return "index";
    }
}
