package com.example.travelday.global.firebase;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;

import java.io.IOException;
import java.io.InputStream;

@Slf4j
@Configuration
public class FirebaseInitializer {

    @Value("${firebase.service-account-file}")
    private String serviceAccountFile;

    @Value("${firebase.database-url}")
    private String databaseUrl;

    @PostConstruct
    public void initialize() {
        try {
            InputStream serviceAccount =
                    new ClassPathResource(serviceAccountFile).getInputStream();

            FirebaseOptions options = new FirebaseOptions.Builder()
//                    .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                    .setCredentials(GoogleCredentials.fromStream(new ClassPathResource(serviceAccountFile).getInputStream()))
                    .setDatabaseUrl(databaseUrl)
                    .build();

            log.info("파일 스트링값으로 표시");
            log.info(serviceAccount.toString());

            if (FirebaseApp.getApps().isEmpty()) {
                FirebaseApp.initializeApp(options);
                log.info("파이어베이스 성공");
            }
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }
}
