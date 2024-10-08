package com.example.travelday.global.firebase;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.util.ResourceUtils;

import java.io.*;

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
            log.info("========= 파이어베이스 초기화 시작==========");
            log.info(serviceAccountFile);

            ClassPathResource resource = new ClassPathResource("TravelDayFirebaseService.json");
            InputStream serviceAccount = new FileInputStream(resource.getFile());
            log.info("========== 파일 읽기 ==========");
            log.info(serviceAccount.toString());

            FirebaseOptions options = FirebaseOptions.builder()
                    .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                    .setDatabaseUrl(databaseUrl)
                    .build();

            if (FirebaseApp.getApps().isEmpty()) {
                throw new IllegalStateException("FirebaseApp not initialized yet.");
            }

            FirebaseApp.initializeApp(options);
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }
}
