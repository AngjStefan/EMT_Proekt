package backend.service;

import dev.langchain4j.data.segment.TextSegment;
import dev.langchain4j.model.embedding.EmbeddingModel;
import dev.langchain4j.rag.content.retriever.EmbeddingStoreContentRetriever;
import dev.langchain4j.rag.query.Query;
import dev.langchain4j.store.embedding.EmbeddingStore;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AiHelperService {

    private final EmbeddingStoreContentRetriever contentRetriever;
    private final EmbeddingStore<TextSegment> embeddingStore;
    private final EmbeddingModel embeddingModel;
    private final ProductService productService;

    public AiHelperService(EmbeddingStoreContentRetriever contentRetriever,
                           EmbeddingStore<TextSegment> embeddingStore,
                           EmbeddingModel embeddingModel,
                           ProductService productService) {
        this.contentRetriever = contentRetriever;
        this.embeddingStore = embeddingStore;
        this.embeddingModel = embeddingModel;
        this.productService = productService;
    }

    public void embedProduct(String productName) {
        var results = contentRetriever.retrieve(Query.from(productName));
        boolean alreadyEmbedded = results.stream()
                .anyMatch(r -> r.textSegment().text().equals(productName));
        if (alreadyEmbedded) {
            System.out.println("Already embedded: " + productName);
            return;
        }

        TextSegment segment = TextSegment.from(productName);
        var embedding = embeddingModel.embed(segment).content();

        embeddingStore.add(embedding, segment);

        System.out.println("New embedding stored: " + productName);
    }

    public void embedAllProducts() {
        List<String> productNames = productService.findAllUniqueProductNames();
        System.out.println("Found " + productNames.size() + " unique products");

        for (String productName : productNames) {
            embedProduct(productName);
        }
    }

    public List<String> findClosestProducts(String query) {
        var results = contentRetriever.retrieve(Query.from(query));

        return results.stream()
                .map(r -> r.textSegment().text())
                .toList();
    }
}