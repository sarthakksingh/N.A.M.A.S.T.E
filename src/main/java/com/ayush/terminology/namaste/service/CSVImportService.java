package com.ayush.terminology.namaste.service;




import com.ayush.terminology.namaste.model.CodeSystem;
import com.ayush.terminology.namaste.model.Concept;
import com.ayush.terminology.namaste.repo.CodeSystemRepository;
import com.ayush.terminology.namaste.repo.ConceptRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.Optional;

@Service
public class CSVImportService {

    @Autowired
    private CodeSystemRepository codeSystemRepository;

    @Autowired
    private ConceptRepository conceptRepository;

    /**
     * Import NAMASTE codes from CSV
     * CSV format: code,displayName,description
     */
    public void importNAMASTECsv(MultipartFile file) throws Exception {

        Optional<CodeSystem> codeSystem = codeSystemRepository.findByName("NAMASTE");
        CodeSystem system = codeSystem.orElseGet(() -> {
            CodeSystem newSystem = new CodeSystem();
            newSystem.setName("NAMASTE");
            newSystem.setVersion("1.0");
            newSystem.setIsActive(true);
            return codeSystemRepository.save(newSystem);
        });


        BufferedReader reader = new BufferedReader(
                new InputStreamReader(file.getInputStream()));

        String line;
        boolean firstLine = true; // skip header
        while ((line = reader.readLine()) != null) {
            if (firstLine) {
                firstLine = false;
                continue;
            }

            String[] parts = line.split(",");
            if (parts.length >= 2) {
                String code = parts[0].trim();
                String displayName = parts[1].trim();
                String description = parts.length > 2 ? parts[2].trim() : "";

                Concept concept = new Concept();
                concept.setCode(code);
                concept.setDisplayName(displayName);
                concept.setDescription(description);
                concept.setCodeSystem(system);
                concept.setIsActive(true);

                conceptRepository.save(concept);
            }
        }
    }
}
