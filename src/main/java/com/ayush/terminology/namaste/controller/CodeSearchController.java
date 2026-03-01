package com.ayush.terminology.namaste.controller;

import com.ayush.terminology.namaste.dto.AiSearchRequest;
import com.ayush.terminology.namaste.dto.SearchResponseDTO;
import com.ayush.terminology.namaste.dto.SuggestionDTO;
import com.ayush.terminology.namaste.service.AuditService;
import com.ayush.terminology.namaste.service.ConceptService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@RestController
@RequestMapping("/api/v1/codes")
@CrossOrigin(origins = "*")
public class CodeSearchController {

    @Autowired
    private ConceptService conceptService;

    @Autowired
    private AuditService auditService;

    @Autowired
    private RestTemplate restTemplate;

    @Value("${ml.service.url}")
    private String mlServiceUrl;

    /**
     * Normal search
     * GET /api/v1/codes/search?q=diabetes&limit=10
     */
    @GetMapping("/search")
    public ResponseEntity<SearchResponseDTO> searchCodes(
            @RequestParam(name = "q") String query,
            @RequestParam(name = "limit", required = false) Integer limit,
            @RequestHeader(name = "X-User-Id", required = false) String userId) {

        try {

            auditService.logAction(
                    userId,
                    null,
                    "SEARCH",
                    "/codes/search",
                    "Query: " + query
            );

            SearchResponseDTO response = conceptService.search(query, limit);

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            return ResponseEntity.status(500).build();
        }
    }

    /**
     * Translate a code
     * GET /api/v1/codes/translate?code=N456&from=NAMASTE
     */
    @GetMapping("/translate")
    public ResponseEntity<SuggestionDTO> translate(
            @RequestParam(name = "code") String code,
            @RequestParam(name = "from") String fromSystem,
            @RequestHeader(name = "X-User-Id", required = false) String userId) {

        try {

            auditService.logAction(
                    userId,
                    null,
                    "TRANSLATE",
                    "/codes/translate",
                    code + " from " + fromSystem
            );

            SuggestionDTO result =
                    conceptService.translateCode(code, fromSystem, "ALL");

            return ResponseEntity.ok(result);

        } catch (Exception e) {
            return ResponseEntity.status(404).build();
        }
    }

    /**
     * AI search endpoint
     * POST /api/v1/codes/ai-search
     */
    @PostMapping("/ai-search")
    public ResponseEntity<SuggestionDTO> aiSearch(
            @RequestBody AiSearchRequest request,
            @RequestHeader(name = "X-User-Id", required = false) String userId) {

        try {

            if (!request.isAiEnabled()) {
                return ResponseEntity.badRequest().build();
            }

            auditService.logAction(
                    userId,
                    null,
                    "AI_SEARCH",
                    "/codes/ai-search",
                    request.getText()
            );

            // Call ML microservice
            Map<String, String> body = Map.of(
                    "text", request.getText()
            );

            ResponseEntity<Map> response =
                    restTemplate.postForEntity(mlServiceUrl, body, Map.class);

            Map<String, Object> result = response.getBody();

            if (result == null) {
                return ResponseEntity.status(500).build();
            }

            String predictedCode =
                    (String) result.get("predicted_code");

            Number confidenceNumber =
                    (Number) result.get("confidence");

            if (predictedCode == null || confidenceNumber == null) {
                return ResponseEntity.status(500).build();
            }

            // Map ML code to full concept
            SuggestionDTO mapped =
                    conceptService.translateCode(predictedCode, "NAMASTE", "ALL");

            if (mapped == null) {
                return ResponseEntity.status(404).build();
            }

            // Inject confidence score
            mapped.setConfidenceScore(confidenceNumber.doubleValue());

            return ResponseEntity.ok(mapped);

        } catch (Exception e) {
            return ResponseEntity.status(500).build();
        }
    }
}