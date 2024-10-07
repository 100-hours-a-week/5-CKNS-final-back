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
            log.info("========= 파이어베이스 초기화 시작 ==========");
            log.info(serviceAccountFile);

            FirebaseOptions options = FirebaseOptions.builder()
                    .setCredentials(GoogleCredentials.fromStream(getFirebaseInfo()))
                    .setDatabaseUrl(databaseUrl)
                    .build();

            FirebaseApp.initializeApp(options);
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }
    private InputStream getFirebaseInfo() throws IOException {
        Resource resource = new ClassPathResource(serviceAccountFile);
        if (resource.exists()) {
            log.info("====== 리소스 getinputstream ======");
            log.info(resource.getInputStream().toString());
            return resource.getInputStream();
        }
        throw new RuntimeException("firebase 키가 존재하지 않습니다.");
    }
}
