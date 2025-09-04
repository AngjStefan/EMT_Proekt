//package backend.ollama;
//
//import dev.langchain4j.model.chat.ChatModel;
//import dev.langchain4j.model.chat.StreamingChatModel;
//import dev.langchain4j.model.embedding.EmbeddingModel;
//import dev.langchain4j.model.ollama.OllamaChatModel;
//import dev.langchain4j.model.ollama.OllamaEmbeddingModel;
//import dev.langchain4j.model.ollama.OllamaStreamingChatModel;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//
//@Configuration
//public class OllamaConfig {
//    private static final String BASE_URL = "http://localhost:11434";
//    private static final String MODEL_NAME = "qwen2.5:3b";
//    // Previous:
//    // deepseek-r1:1.5b - no tools;
//    // llama3.1:8b - too much ram
//    // qwen2.5:3b - doesn't understand Macedonian.
//
//    @Bean
//    public ChatModel chatModel() {
//        // Creates standard chat model for:
//        // - Regular question-answering
//        // - Single response generation
//        // - Zero temperature for consistent outputs
//        return OllamaChatModel.builder()
//                .baseUrl(BASE_URL)
//                .temperature(0.0)  // Deterministic responses
//                .modelName(MODEL_NAME)
//                .build();
//    }
//
//    @Bean
//    public StreamingChatModel streamingChatModel() {
//        // Creates streaming model for:
//        // - Real-time response generation
//        // - Token-by-token output
//        // - Better user experience with immediate feedback
//        return OllamaStreamingChatModel.builder()
//                .baseUrl(BASE_URL)
//                .temperature(0.0)
//                .modelName(MODEL_NAME)
//                .build();
//    }
//
//    @Bean
//    public EmbeddingModel embeddingModel() {
//        // Creates embedding model for:
//        // - Converting text to vectors
//        // - Document indexing
//        // - Similarity search
//        return OllamaEmbeddingModel.builder()
//                .baseUrl(BASE_URL)
//                .modelName(MODEL_NAME)
//                .build();
//    }
//
////    @Bean
////    public EmbeddingModel embeddingModel() {
////        return OpenAiEmbeddingModel.builder()
////                .apiKey(System.getenv("OPENAI_API_KEY")) // or inject securely
////                .modelName("text-embedding-3-small")
////                .build();
////    }
//}