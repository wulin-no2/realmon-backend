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
        // Convert to BufferedImage
        BufferedImage image = ImageIO.read(multipartFile.getInputStream());

        // First write to JPEG to unify format
        File originalJpeg = convertToJpeg(image);
        long originalSize = originalJpeg.length();

        if (originalSize <= MAX_SIZE) {
            return originalJpeg;
        }

        // Try compressing iteratively
        double quality = 0.95;
        File compressedFile = File.createTempFile("compressed-", ".jpg");

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

        return compressedFile;
    }

    private static File convertToJpeg(BufferedImage image) throws IOException {
        File jpegFile = File.createTempFile("converted-", ".jpg");
        ImageIO.write(image, "jpg", jpegFile);
        return jpegFile;
    }
}
