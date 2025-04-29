package com.kodarit.stars.toolbox.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.util.DefaultIndenter;
import com.fasterxml.jackson.core.util.DefaultPrettyPrinter;
import com.fasterxml.jackson.core.util.Separators;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class JsonToolController {
    private final ObjectMapper objectMapper = JsonMapper.builder()
            .enable(DeserializationFeature.FAIL_ON_TRAILING_TOKENS)
            .defaultPrettyPrinter(new DefaultPrettyPrinter()
                    .withSeparators(Separators.createDefaultInstance()
                            .withObjectFieldValueSpacing(Separators.Spacing.AFTER))
                    .withArrayIndenter(DefaultIndenter.SYSTEM_LINEFEED_INSTANCE))
            .build();

    @GetMapping("/json_tool")
    public String get(Model model) {
        model.addAttribute("pageCss", "json_tool.css");
        return "json_tool";
    }

    @PostMapping("/json_tool")
    public String post(Model model, @RequestParam("mode") String mode, @RequestParam("json") String inputJson) {
        model.addAttribute("pageCss", "json_tool.css");

        boolean minify;
        switch (mode) {
            case "minify" -> minify = true;
            case "prettify" -> minify = false;
            default -> {
                model.addAttribute("jsonContent", inputJson);
                model.addAttribute("jsonError", "Unexpected mode set");
                return "json_tool";
            }
        }

        try {
            var jsonTree = objectMapper.readTree(inputJson);
            var formatted = minify
                    ? objectMapper.writeValueAsString(jsonTree)
                    : objectMapper.writer().withDefaultPrettyPrinter().writeValueAsString(jsonTree);
            model.addAttribute("jsonContent", formatted);
        } catch (JsonProcessingException e) {
            model.addAttribute("jsonContent", inputJson);
            model.addAttribute("jsonError", e.getOriginalMessage());
        }

        return "json_tool";
    }
}
