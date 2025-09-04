package backend.langchain4j;

import dev.langchain4j.data.document.loader.FileSystemDocumentLoader;
import dev.langchain4j.data.document.splitter.DocumentSplitters;
import dev.langchain4j.data.segment.TextSegment;
import dev.langchain4j.model.TokenCountEstimator;
import dev.langchain4j.model.embedding.EmbeddingModel;
import dev.langchain4j.store.embedding.EmbeddingStore;
import dev.langchain4j.store.embedding.EmbeddingStoreIngestor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

/**
 *  In the real world, ingesting documents would happen separately, not in the same application.
 */
@Component
public class DocumentationIngestor implements CommandLineRunner {

    private final EmbeddingModel embeddingModel;
    private final EmbeddingStore<TextSegment> embeddingStore;
    private final TokenCountEstimator tokenCountEstimator;
    private final Resource termsOfService;

    public DocumentationIngestor(
            EmbeddingModel embeddingModel,
            EmbeddingStore<TextSegment> embeddingStore,
            TokenCountEstimator tokenCountEstimator,
            @Value("classpath:terms-of-service.txt") Resource termsOfService) {
        this.embeddingModel = embeddingModel;
        this.embeddingStore = embeddingStore;
        this.tokenCountEstimator = tokenCountEstimator;
        this.termsOfService = termsOfService;
    }

    @Override
    public void run(String... args) throws Exception {
        var doc = FileSystemDocumentLoader.loadDocument(termsOfService.getFile().toPath());
        // Only the first time
        //        var ingestor = EmbeddingStoreIngestor.builder()
        //                .documentSplitter(DocumentSplitters.recursive(50, 0, tokenCountEstimator))
        //                .embeddingModel(embeddingModel)
        //                .embeddingStore(embeddingStore)
        //                .build();
        //        ingestor.ingest(doc);
    }
}