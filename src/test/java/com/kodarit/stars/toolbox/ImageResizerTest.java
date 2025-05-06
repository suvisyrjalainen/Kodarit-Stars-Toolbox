package com.kodarit.stars.toolbox;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class ImageResizerTest {

    @Autowired
    private ImageResizer imageResizer;

    @Test
    void returnsPngWhenFilenameIsNull() {
        assertEquals("png", imageResizer.getImageFormat(null));
    }

    @Test
    void returnsJpegForJpgExtension() {
        assertEquals("jpeg", imageResizer.getImageFormat("photo.jpg"));
    }

    @Test
    void returnsJpegForJpegExtension() {
        assertEquals("jpeg", imageResizer.getImageFormat("photo.jpeg"));
    }

    @Test
    void returnsPngForPngExtension() {
        assertEquals("png", imageResizer.getImageFormat("image.png"));
    }

    @Test
    void returnsGifForGifExtension() {
        assertEquals("gif", imageResizer.getImageFormat("animation.gif"));
    }

    @Test
    void returnsBmpForBmpExtension() {
        assertEquals("bmp", imageResizer.getImageFormat("bitmap.bmp"));
    }

    @Test
    void returnsJpegForUnknownExtension() {
        assertEquals("jpeg", imageResizer.getImageFormat("file.tiff"));
    }

    @Test
    void returnsJpegForFilenameWithoutExtension() {
        assertEquals("jpeg", imageResizer.getImageFormat("filename"));
    }

    @Test
    void returnsJpegForFilenameEndingWithDot() {
        assertEquals("jpeg", imageResizer.getImageFormat("filename."));
    }

    @Test
    void isCaseInsensitiveWithExtensions() {
        assertEquals("jpeg", imageResizer.getImageFormat("file.JpG"));
        assertEquals("png", imageResizer.getImageFormat("file.PnG"));
        assertEquals("gif", imageResizer.getImageFormat("file.GIF"));
        assertEquals("bmp", imageResizer.getImageFormat("file.BMp"));
    }
}
