package backend.service;

import dev.langchain4j.data.document.Document;
import dev.langchain4j.rag.content.retriever.EmbeddingStoreContentRetriever;
import dev.langchain4j.rag.query.Query;
import dev.langchain4j.store.embedding.EmbeddingStoreIngestor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AiHelperService {

    private final EmbeddingStoreIngestor ingestor;
    private final EmbeddingStoreContentRetriever retriever;
    private final ProductService productService;

    public AiHelperService(EmbeddingStoreContentRetriever retriever, EmbeddingStoreIngestor ingestor, ProductService productService) {
        this.retriever = retriever;
        this.ingestor = ingestor;
        this.productService = productService;
    }

    public void embedProduct (String productName) {
        var results = retriever.retrieve(Query.from(productName));
        if (results.isEmpty()) {
            System.out.println("Already embedded: " + productName);
            return;
        }

        // Embedding Store Ingestor only takes documents
        Document doc = Document.from(productName);

        ingestor.ingest(doc);
        System.out.println("New embedding stored: " + productName);
    }

    public void embedAllProducts () {
        List<String> productNames = productService.findAllUniqueProductNames();
        System.out.println("Found " + productNames.size() + " unique products");

        for (String productName : productNames) {
            embedProduct(productName);
        }
    }

    public List<String> findClosestProducts(String query) {
        var results = retriever.retrieve(Query.from(query));

        // Results contain the ingested documents with similarity scores
        return results.stream()
                .map(r -> r.toString()) // Extracts original product name
                .toList();
    }
}
