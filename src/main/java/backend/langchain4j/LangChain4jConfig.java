//package backend.langchain4j;
//
//import dev.langchain4j.data.segment.TextSegment;
//import dev.langchain4j.memory.chat.ChatMemoryProvider;
//import dev.langchain4j.memory.chat.TokenWindowChatMemory;
//import dev.langchain4j.model.Tokenizer;
//import dev.langchain4j.model.chat.StreamingChatModel;
//import dev.langchain4j.model.embedding.EmbeddingModel;
//import dev.langchain4j.model.googleai.GoogleAiEmbeddingModel;
//import dev.langchain4j.model.googleai.GoogleAiGeminiChatModel;
//import dev.langchain4j.model.googleai.GoogleAiGeminiStreamingChatModel;
//import dev.langchain4j.model.googleai.GoogleAiGeminiTokenCountEstimator;
//import dev.langchain4j.rag.content.retriever.ContentRetriever;
//import dev.langchain4j.rag.content.retriever.EmbeddingStoreContentRetriever;
//import dev.langchain4j.store.embedding.EmbeddingStore;
//import dev.langchain4j.store.embedding.inmemory.InMemoryEmbeddingStore;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//
//@Configuration
//public class LangChain4jConfig {
//
//    @Bean
//    Tokenizer tokenizer() {
//        return GoogleAiGeminiTokenizer.builder()
//                .apiKey(System.getenv("GOOGLE_API_KEY"))
//                .modelName("gemini-2.0-flash")
//                .build();
//    }
//
//    @Bean
//    ChatMemoryProvider chatMemoryProvider(GoogleAiGeminiTokenCountEstimator estimator) {
//        return chatId -> TokenWindowChatMemory.withMaxTokens(1000, estimator);
//    }
//
//    @Bean
//    EmbeddingStore<TextSegment> embeddingStore() {
//        return new InMemoryEmbeddingStore<>();
//    }
//
//    @Bean
//    StreamingChatModel streamingChatModel() {
//        return GoogleAiGeminiStreamingChatModel.builder()
//                .apiKey(System.getenv("GOOGLE_API_KEY"))
//                .modelName("gemini-2.0-flash")
//                .temperature(0.0)
//                .build();
//    }
//
//    @Bean
//    EmbeddingModel embeddingModel() {
//        return GoogleAiEmbeddingModel.builder()
//                .apiKey(System.getenv("GOOGLE_API_KEY"))
//                .modelName("text-embedding-004")
//                .build();
//    }
//
//
//    @Bean
//    ContentRetriever contentRetriever(
//            EmbeddingStore<TextSegment> embeddingStore,
//            EmbeddingModel embeddingModel
//    ) {
//        return EmbeddingStoreContentRetriever.builder()
//                .embeddingStore(embeddingStore)
//                .embeddingModel(embeddingModel)
//                .maxResults(3) //2
//                .minScore(0.6)
//                .build();
//    }
//}
