package com.kodarit.stars.toolbox;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class ToolboxErrorController implements ErrorController {
    @ExceptionHandler(exception = Exception.class, produces = "text/html")
    @RequestMapping("/error")
    public String errorPage(HttpServletRequest request, Model model) {
        model.addAttribute("pageCss", "error.css");
        Object status = request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);
        var errorMessage = request.getAttribute(RequestDispatcher.ERROR_MESSAGE);

        var title = switch (status) {
            case Integer i when i == 404 -> "Not Found";
            case Integer i when i == 500 -> "Internal Server Error";
            case Integer i -> "Error " + i;
            case null, default -> "Unknown Error";
        };
        var message = switch (status) {
            case Integer i when i == 404 -> "The page you were looking for does not exist";
            case Integer i when i == 500 -> "Our code seems to be broken, please let us know";
            case null, default -> errorMessage != null ? errorMessage : "An unknown error has occurred";
        };

        model.addAttribute("errorTitle", title);
        model.addAttribute("errorMessage", message);
        return "error";
    }
}
