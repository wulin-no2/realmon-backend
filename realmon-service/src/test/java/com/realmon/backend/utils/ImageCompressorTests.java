package com.realmon.backend.utils;

import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockMultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Random;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ImageCompressorTests {

    @Test
    void largeImageDoesNotLeaveIntermediateJpegBehind() throws Exception {
        Set<Path> convertedFilesBefore = convertedTemporaryFiles();
        BufferedImage image = new BufferedImage(900, 900, BufferedImage.TYPE_INT_RGB);
        Random random = new Random(42);
        for (int y = 0; y < image.getHeight(); y++) {
            for (int x = 0; x < image.getWidth(); x++) {
                image.setRGB(x, y, random.nextInt(0x1000000));
            }
        }

        ByteArrayOutputStream output = new ByteArrayOutputStream();
        assertTrue(ImageIO.write(image, "jpg", output));
        MockMultipartFile upload = new MockMultipartFile(
                "image", "large.jpg", "image/jpeg", output.toByteArray());

        File compressed = ImageCompressor.compressIfNeeded(upload);
        try {
            assertTrue(compressed.getName().startsWith("compressed-"));
            assertEquals(convertedFilesBefore, convertedTemporaryFiles());
        } finally {
            Files.deleteIfExists(compressed.toPath());
        }
    }

    private Set<Path> convertedTemporaryFiles() throws Exception {
        Path temporaryDirectory = Path.of(System.getProperty("java.io.tmpdir"));
        try (Stream<Path> paths = Files.list(temporaryDirectory)) {
            return paths
                    .filter(path -> path.getFileName().toString().startsWith("converted-"))
                    .collect(Collectors.toSet());
        }
    }
}
