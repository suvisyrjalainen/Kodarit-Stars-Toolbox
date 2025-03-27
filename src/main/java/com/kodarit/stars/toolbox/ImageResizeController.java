package com.kodarit.stars.toolbox;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.ui.Model;
import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Base64;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ImageResizeController {

    @GetMapping("/resize-image")
    public String showForm() {
        return "image-resizer";
    }

    @PostMapping("/resize-image")
    public String resizeImage(@RequestParam MultipartFile imageFile,
                              @RequestParam int width,
                              @RequestParam int height,
                              Model model) {
        if (width <= 0 || height <= 0) {
            model.addAttribute("errorMessage", "Width and height must be positive.");
            return "image-resizer";
        }
        try {
            BufferedImage inputImage = ImageIO.read(imageFile.getInputStream());
            if (inputImage == null) {
                model.addAttribute("errorMessage", "Invalid image file.");
                return "image-resizer";
            }
            BufferedImage outputImage = new BufferedImage(width, height, inputImage.getType());
            Graphics2D g2d = outputImage.createGraphics();
            g2d.drawImage(inputImage, 0, 0, width, height, null);
            g2d.dispose();

            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ImageIO.write(outputImage, "png", baos);
            String base64 = Base64.getEncoder().encodeToString(baos.toByteArray());
            model.addAttribute("resizedImage", "data:image/png;base64," + base64);

        } catch (IOException e) {
            model.addAttribute("errorMessage", "Error resizing image: " + e.getMessage());
        }
        return "image-resizer";
    }
}
