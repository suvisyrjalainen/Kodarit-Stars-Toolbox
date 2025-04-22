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
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

/**
 * Controller for handling image resizing requests.
 * Processes image upload, validates input, and sets the resized image data for display.
 */
@Controller
public class ImageResizerController {

    private static final Logger LOGGER = LoggerFactory.getLogger(ImageResizerController.class);
    private final ImageResizer imageResizer;
    private static final long MAX_FILE_SIZE = 10 * 1024 * 1024; // 10MB limit

    @Autowired
    public ImageResizerController(ImageResizer imageResizer) {
        this.imageResizer = imageResizer;
    }

    /**
     * Handles the image resizing request.
     *
     * Validates the uploaded file, dimensions, and file size.
     * If validation passes, resizes the image and stores it in session and model.
     *
     * @param imageFile the image file to resize; may be null or empty.
     * @param width the target width in pixels
     * @param height the target height in pixels
     * @param model the UI model to update
     * @param session HTTP session for storing image data
     * @return a redirect to the image resizer page on success; otherwise, returns the image resizer view with an error message
     */
    @PostMapping("/image-resizer/process")
    public String resizeImage(@RequestParam(name = "imageFile", required = false) MultipartFile imageFile,
                              @RequestParam("width") int width,
                              @RequestParam("height") int height,
                              Model model,
                              HttpSession session) {
        try {
            if (imageFile == null || imageFile.isEmpty()) {
                model.addAttribute("errorMessage", "Please select an image file");
                return "image_resizer";
            }

            // Check file size
            if (imageFile.getSize() > MAX_FILE_SIZE) {
                model.addAttribute("errorMessage", "File size exceeds the maximum limit of 10MB");
                return "image_resizer";
            }

            // Validate dimensions
            if (width <= 0 || height <= 0 || width > 5000 || height > 5000) {
                model.addAttribute("errorMessage", "Invalid dimensions. Width and height must be between 1 and 5000 pixels");
                return "image_resizer";
            }

            LOGGER.info("Processing image: {} (size: {} bytes, dimensions: {}x{})",
                    imageFile.getOriginalFilename(), imageFile.getSize(), width, height);

            String resizedImageBase64 = imageResizer.resizeImage(imageFile, width, height);
            model.addAttribute("resizedImage", resizedImageBase64);
            session.setAttribute("resizedImage", resizedImageBase64);
            session.setAttribute("originalWidth", width);
            session.setAttribute("originalHeight", height);
            LOGGER.info("Image resized successfully");

            return "redirect:/image-resizer";

        } catch (IOException e) {
            LOGGER.error("Error reading image file", e);
            model.addAttribute("errorMessage", "Error reading image file: " + e.getMessage());
        } catch (OutOfMemoryError e) {
            LOGGER.error("Out of memory error while processing image", e);
            model.addAttribute("errorMessage", "Image is too large to process. Try a smaller image or reduce the dimensions.");
        } catch (Exception e) {
            LOGGER.error("Unexpected error processing image", e);
            model.addAttribute("errorMessage", "Error processing image: " + e.getMessage());
        }

        return "image_resizer";
    }
}
