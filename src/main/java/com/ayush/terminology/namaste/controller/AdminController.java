package com.ayush.terminology.namaste.controller;


import com.ayush.terminology.namaste.service.CSVImportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/v1/admin")
public class AdminController {

    @Autowired
    private CSVImportService csvImportService;

    /**
     * Upload NAMASTE CSV
     * POST /api/v1/admin/namaste/import
     */
    @PostMapping("/namaste/import")
    public ResponseEntity<String> importNamasteCSV(@RequestParam("file") MultipartFile file) {
        try {
            csvImportService.importNAMASTECsv(file);
            return ResponseEntity.ok("NAMASTE codes imported successfully");
        } catch (Exception e) {
            return ResponseEntity.status(400).body("Import failed: " + e.getMessage());
        }
    }
}

