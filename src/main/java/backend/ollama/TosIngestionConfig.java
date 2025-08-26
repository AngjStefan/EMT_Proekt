package backend.ollama;

import dev.langchain4j.data.document.loader.FileSystemDocumentLoader;
import dev.langchain4j.store.embedding.EmbeddingStoreIngestor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;

import java.nio.file.Path;

@Configuration
public class TosIngestionConfig {

    /**
     * Ingests terms-of-service.txt from the classpath at app startup.
     * Reuses the EmbeddingStoreIngestor bean you already configured in EmbeddingConfig.
     */
    @Bean
    public CommandLineRunner ingestTermsOfService(
            EmbeddingStoreIngestor ingestor,
            @Value("classpath:terms-of-service.txt") Resource termsOfService
    ) {
        return args -> {
            var doc = FileSystemDocumentLoader.loadDocument(Path.of(termsOfService.getFile().toURI()));
            ingestor.ingest(doc);
        };
    }
}
