package backend.ollama;

import dev.langchain4j.data.document.splitter.DocumentSplitters;
import dev.langchain4j.data.segment.TextSegment;
import dev.langchain4j.model.embedding.EmbeddingModel;
import dev.langchain4j.rag.content.retriever.EmbeddingStoreContentRetriever;
import dev.langchain4j.store.embedding.EmbeddingStore;
import dev.langchain4j.store.embedding.EmbeddingStoreIngestor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class EmbeddingConfig {

    @Bean
    public EmbeddingStoreIngestor embeddingStoreIngestor(EmbeddingModel embeddingModel,
                                                         EmbeddingStore<TextSegment> embeddingStore) {
        // Creates a document ingestor that:
        // - Splits documents into chunks of 1000 tokens
        // - Uses 100 tokens overlap to maintain context between chunks
        // - Uses the provided embedding model to convert text to vectors
        // - Stores the vectors in the provided embedding store
        return EmbeddingStoreIngestor.builder()
                .documentSplitter(DocumentSplitters.recursive(50, 0)) // 1000, 100
                .embeddingModel(embeddingModel)
                .embeddingStore(embeddingStore)
                .build();
    }

    @Bean
    public EmbeddingStoreContentRetriever embeddingStoreContentRetriever(
            EmbeddingStore<TextSegment> embeddingStore,
            EmbeddingModel embeddingModel) {
        // Creates a content retriever that:
        // - Uses vector similarity search to find relevant document segments
        // - Returns maximum 3 most relevant results
        // - Only returns results with similarity score above 0.6
        // - Uses the same embedding model for query encoding
        return EmbeddingStoreContentRetriever.builder()
                .embeddingStore(embeddingStore)
                .embeddingModel(embeddingModel)
                .maxResults(3)  // Adjust this based on how many context segments you want
                .minScore(0.6)  // Minimum similarity threshold (0-1)
                .build();
    }
}