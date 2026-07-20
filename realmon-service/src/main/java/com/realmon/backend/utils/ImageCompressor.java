package com.realmon.backend.utils;

import net.coobird.thumbnailator.Thumbnails;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class ImageCompressor {

    private static final long MAX_SIZE = 300 * 1024; // 300KB

    public static File compressIfNeeded(MultipartFile multipartFile) throws IOException {
        BufferedImage image = ImageIO.read(multipartFile.getInputStream());
        if (image == null) {
            throw new IOException("Unsupported or invalid image data");
        }

        File originalJpeg = convertToJpeg(image);
        long originalSize = originalJpeg.length();

        if (originalSize <= MAX_SIZE) {
            return originalJpeg;
        }

        File compressedFile = null;
        boolean completed = false;
        try {
            double quality = 0.95;
            compressedFile = File.createTempFile("compressed-", ".jpg");

            while (quality > 0.1) {
                Thumbnails.of(originalJpeg)
                        .scale(1.0)
                        .outputFormat("jpg")
                        .outputQuality(quality)
                        .toFile(compressedFile);

                if (compressedFile.length() <= MAX_SIZE) {
                    break;
                }

                quality -= 0.05;
            }

            completed = true;
            return compressedFile;
        } finally {
            deleteTemporaryFile(originalJpeg);
            if (!completed) {
                deleteTemporaryFile(compressedFile);
            }
        }
    }

    private static File convertToJpeg(BufferedImage image) throws IOException {
        File jpegFile = File.createTempFile("converted-", ".jpg");
        boolean completed = false;
        try {
            if (!ImageIO.write(image, "jpg", jpegFile)) {
                throw new IOException("JPEG writer is unavailable");
            }
            completed = true;
            return jpegFile;
        } finally {
            if (!completed) {
                deleteTemporaryFile(jpegFile);
            }
        }
    }

    private static void deleteTemporaryFile(File file) {
        if (file != null && file.exists() && !file.delete()) {
            file.deleteOnExit();
        }
    }
}
