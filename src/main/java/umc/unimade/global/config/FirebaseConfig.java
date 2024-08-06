package umc.unimade.global.config;

import org.springframework.beans.factory.annotation.Value;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import jakarta.annotation.PostConstruct;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import java.io.IOException;
import java.io.InputStream;
import java.util.logging.Level;
import java.util.logging.Logger;

@Configuration
public class FirebaseConfig {

    @Value("${firebase.keyPath}")
    private String keyPath;
    private static final Logger logger = Logger.getLogger(FirebaseConfig.class.getName());

    @PostConstruct
    public void init() {
        try (InputStream serviceAccount = new ClassPathResource(keyPath).getInputStream()) {
            FirebaseOptions options = new FirebaseOptions.Builder()
                    .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                    .build();

            FirebaseApp.initializeApp(options);

        } catch (IOException e) {
            logger.log(Level.SEVERE, "Failed to initialize Firebase", e);
        }
    }
}