package com.erp.Config;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import java.io.ByteArrayInputStream;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

@Configuration
public class FirebaseConfig {

    @Value("${firebase.type}")
    private String type;

    @Value("${firebase.project-id}")
    private String projectId;

    @Value("${firebase.client-email}")
    private String clientEmail;

    @Value("${firebase.private-key}")
    private String privateKey;

    @Value("${firebase.client-id}")
    private String clientId;

    @Value("${firebase.private-key-id}")
    private String privateKeyId;

    @PostConstruct
    public void initializeFirebase() {
        try {
            // Build minimal credentials map for FCM
            Map<String, Object> credentials = new HashMap<>();
            credentials.put("type", type);
            credentials.put("project_id", projectId);
            credentials.put("client_email", clientEmail);
            credentials.put("client_id", clientId);
            credentials.put("private_key_id", privateKeyId);
            credentials.put("private_key", privateKey.replace("\\n", "\n"));

            // Convert map to JSON for Firebase SDK
            String json = new com.fasterxml.jackson.databind.ObjectMapper()
                    .writeValueAsString(credentials);

            GoogleCredentials googleCredentials = GoogleCredentials.fromStream(
                    new ByteArrayInputStream(json.getBytes(StandardCharsets.UTF_8))
            );

            FirebaseOptions options = FirebaseOptions.builder()
                    .setCredentials(googleCredentials)
                    .setProjectId(projectId)
                    .build();

            if (FirebaseApp.getApps().isEmpty()) {
                FirebaseApp.initializeApp(options);
            }

        } catch (Exception e) {
            throw new IllegalStateException("Firebase initialization failed", e);
        }
    }
}
