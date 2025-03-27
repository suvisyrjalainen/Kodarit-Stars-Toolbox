package com.kodarit.stars.toolbox.controller;

import com.kodarit.stars.toolbox.ImageResizer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;

@Controller
public class ImageResizerController {

    private static final Logger logger = LoggerFactory.getLogger(ImageResizerController.class);
    private final ImageResizer imageResizer;
    private static final long MAX_FILE_SIZE = 10 * 1024 * 1024; // 10MB limit

    @Autowired
    public ImageResizerController(ImageResizer imageResizer) {
        this.imageResizer = imageResizer;
    }

    @GetMapping("/tools/image-resizer")
    public String showImageResizerPage() {
        return "image-resizer";
    }

    @PostMapping("/tools/image-resizer/process")
    public String resizeImage(@RequestParam("imageFile") MultipartFile imageFile,
                             @RequestParam("width") int width,
                             @RequestParam("height") int height,
                             Model model) {
        try {
            if (imageFile.isEmpty()) {
                model.addAttribute("errorMessage", "Please select an image file");
                return "image-resizer";
            }
            
            // Check file size
            if (imageFile.getSize() > MAX_FILE_SIZE) {
                model.addAttribute("errorMessage", "File size exceeds the maximum limit of 10MB");
                return "image-resizer";
            }
            
            // Validate dimensions
            if (width <= 0 || height <= 0 || width > 5000 || height > 5000) {
                model.addAttribute("errorMessage", "Invalid dimensions. Width and height must be between 1 and 5000 pixels");
                return "image-resizer";
            }
            
            logger.info("Processing image: {} (size: {} bytes, dimensions: {}x{})", 
                    imageFile.getOriginalFilename(), imageFile.getSize(), width, height);
            
            String resizedImageBase64 = imageResizer.resizeImage(imageFile, width, height);
            model.addAttribute("resizedImage", resizedImageBase64);
            logger.info("Image resized successfully");
            
        } catch (IOException e) {
            logger.error("Error reading image file", e);
            model.addAttribute("errorMessage", "Error reading image file: " + e.getMessage());
        } catch (OutOfMemoryError e) {
            logger.error("Out of memory error while processing image", e);
            model.addAttribute("errorMessage", "Image is too large to process. Try a smaller image or reduce the dimensions.");
        } catch (Exception e) {
            logger.error("Unexpected error processing image", e);
            model.addAttribute("errorMessage", "Error processing image: " + e.getMessage());
        }
        
        return "image-resizer";
    }
}
