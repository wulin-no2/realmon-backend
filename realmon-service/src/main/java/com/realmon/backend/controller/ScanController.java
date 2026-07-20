package com.realmon.backend.controller;

import com.realmon.backend.service.INaturalistScanService;
import com.realmon.backend.service.InatTokenService;
import com.realmon.common.model.dto.ScanResultDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.Collections;
import java.util.List;

@RestController
@RequestMapping("/api/scan")
@RequiredArgsConstructor
@Slf4j
public class ScanController {

    private static final long MAX_IMAGE_SIZE = 10L * 1024 * 1024;

    private final INaturalistScanService iNaturalistScanService;
    private final InatTokenService inatTokenService;

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<List<ScanResultDTO>> scanImage(@RequestParam("image") MultipartFile image) {
        if (image.isEmpty()) {
            return ResponseEntity.badRequest().body(Collections.emptyList());
        }
        if (image.getSize() > MAX_IMAGE_SIZE) {
            return ResponseEntity.status(HttpStatus.PAYLOAD_TOO_LARGE).body(Collections.emptyList());
        }
        if (!MediaType.IMAGE_JPEG_VALUE.equals(image.getContentType())
                && !MediaType.IMAGE_PNG_VALUE.equals(image.getContentType())) {
            return ResponseEntity.status(HttpStatus.UNSUPPORTED_MEDIA_TYPE).body(Collections.emptyList());
        }
        try {
            List<ScanResultDTO> results = iNaturalistScanService.identifySpecies(image);
            return ResponseEntity.ok(results);
        } catch (Exception e) {
            log.error("Scan error", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Collections.emptyList());
        }
    }


}
