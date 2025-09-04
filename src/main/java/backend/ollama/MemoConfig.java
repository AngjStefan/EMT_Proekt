//package backend.ollama;
//
//import dev.langchain4j.data.segment.TextSegment;
//import dev.langchain4j.memory.chat.ChatMemoryProvider;
//import dev.langchain4j.memory.chat.MessageWindowChatMemory;
//import dev.langchain4j.model.embedding.EmbeddingModel;
//import dev.langchain4j.store.embedding.EmbeddingStore;
//import dev.langchain4j.store.embedding.pgvector.PgVectorEmbeddingStore;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//
//@Configuration
//public class MemoConfig {
//    private EmbeddingModel embeddingModel;
//
//    public MemoConfig(EmbeddingModel embeddingModel) {
//        this.embeddingModel = embeddingModel;
//    }
//
//    @Bean
//    public ChatMemoryProvider chatMemoryProvider() {
//        // Creates a memory provider that:
//        // - Maintains separate chat history for each chat ID
//        // - Keeps last 10 messages in memory for context
//        // - Helps maintain conversation coherence
//        return chatId -> MessageWindowChatMemory.withMaxMessages(10);
//    }
//
//    @Bean
//    public EmbeddingStore<TextSegment> embeddingStore() {
//        // Configures PostgreSQL with pgvector extension to:
//        // - Store document embeddings as vectors
//        // - Enable similarity search
//        // - Auto-create required tables
//        // - Match embedding dimensions with model
//        return PgVectorEmbeddingStore.builder()
//                .host("localhost")
//                .port(3456)
//                .database("ollama_db")
//                .user("emt")
//                .password("emt")
//                .table("my_embeddings")
//                .dimension(embeddingModel.dimension())  // Automatically matches model's embedding size
//                .createTable(true)  // Creates table if it doesn't exist
//                .build();
//    }
//}