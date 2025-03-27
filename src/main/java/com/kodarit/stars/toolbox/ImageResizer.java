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
        // Read the original image
        BufferedImage originalImage = ImageIO.read(imageFile.getInputStream());
        if (originalImage == null) {
            throw new IOException("Could not read image format");
        }
        
        // Use BufferedImage.TYPE_INT_ARGB to preserve transparency for PNGs
        int imageType = originalImage.getTransparency() == BufferedImage.OPAQUE ? 
                BufferedImage.TYPE_INT_RGB : BufferedImage.TYPE_INT_ARGB;
        
        // Create a new BufferedImage for the resized image
        BufferedImage resizedImage = new BufferedImage(width, height, imageType);

        // Draw the original image scaled to the new dimensions with improved quality
        Graphics2D g = resizedImage.createGraphics();
        g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);
        g.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g.drawImage(originalImage, 0, 0, width, height, null);
        g.dispose();

        // Convert the resized image to Base64 for display in browser
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        String formatName = getImageFormat(imageFile.getOriginalFilename());
        
        // Make sure we use proper format for writing the image
        ImageIO.write(resizedImage, formatName, outputStream);
        
        return "data:image/" + formatName + ";base64," + 
               Base64.getEncoder().encodeToString(outputStream.toByteArray());
    }
    
    /**
     * Resizes an image by a scale factor
     *
     * @param imageFile the image file to resize
     * @param scaleFactor the factor to scale by (0.5 = half size, 2.0 = double size)
     * @return Base64 encoded string of the resized image
     * @throws IOException if image processing fails
     */
    public String resizeImageByScale(MultipartFile imageFile, double scaleFactor) throws IOException {
        BufferedImage originalImage = ImageIO.read(imageFile.getInputStream());
        
        // Calculate new dimensions
        int newWidth = (int) (originalImage.getWidth() * scaleFactor);
        int newHeight = (int) (originalImage.getHeight() * scaleFactor);
        
        return resizeImage(imageFile, newWidth, newHeight);
    }
    
    /**
     * Extracts image format from filename
     */
    private String getImageFormat(String filename) {
        if (filename == null) return "png"; // default
        
        String extension = "";
        int i = filename.lastIndexOf('.');
        if (i > 0 && i < filename.length() - 1) {
            extension = filename.substring(i + 1).toLowerCase();
        }
        
        // Check if the extension is a supported image format
        switch (extension) {
            case "jpg":
            case "jpeg":
                return "jpeg";
            case "png":
                return "png";
            case "gif":
                return "gif";
            case "bmp":
                return "bmp";
            default:
                return "jpeg"; // default to jpeg if unknown
        }
    }
}
