package com.kodarit.stars.toolbox;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Base64;

@Service
public class ImageResizer {

    /**
     * Resizes an image to the specified width and height
     *
     * @param imageFile the image file to resize
     * @param width the target width
     * @param height the target height
     * @return Base64 encoded string of the resized image
     * @throws IOException if image processing fails
     */
    public String resizeImage(MultipartFile imageFile, int width, int height) throws IOException {
        // Read the original image using try-with-resources
        BufferedImage originalImage;
        try (var inputStream = imageFile.getInputStream()) {
            originalImage = ImageIO.read(inputStream);
            if (originalImage == null) {
                throw new IOException("Could not read image format");
            }
        }

        // Use BufferedImage.TYPE_INT_ARGB to preserve transparency for PNGs
        int imageType = originalImage.getTransparency() == BufferedImage.OPAQUE ?
                BufferedImage.TYPE_INT_RGB : BufferedImage.TYPE_INT_ARGB;

        // Create a new BufferedImage for the resized image
        BufferedImage resizedImage = new BufferedImage(width, height, imageType);

        // Draw the original image scaled to the new dimensions with improved quality
        Graphics2D graphics = resizedImage.createGraphics();
        graphics.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);
        graphics.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        graphics.drawImage(originalImage, 0, 0, width, height, null);
        graphics.dispose();

        // Convert the resized image to Base64 for display in browser
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        String formatName = getImageFormat(imageFile.getOriginalFilename());

        // Make sure we use proper format for writing the image
        ImageIO.write(resizedImage, formatName, outputStream);

        return "data:image/" + formatName + ";base64," +
               Base64.getEncoder().encodeToString(outputStream.toByteArray());
    }

    /**
     * Extracts image format from filename
     */
    public String getImageFormat(String filename) {
        if (filename == null) return "png"; // default

        String extension = "";
        int i = filename.lastIndexOf('.');
        if (i > 0 && i < filename.length() - 1) {
            extension = filename.substring(i + 1).toLowerCase();
        }

        // Use switch expression
        return switch (extension) {
            case "jpg", "jpeg" -> "jpeg";
            case "png" -> "png";
            case "gif" -> "gif";
            case "bmp" -> "bmp";
            default -> "jpeg"; // default to jpeg if unknown
        };
    }
}
