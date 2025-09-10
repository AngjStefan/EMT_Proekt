package backend.client;

import backend.langchain4j.LangChain4jAssistant;
import com.vaadin.flow.server.auth.AnonymousAllowed;
import com.vaadin.hilla.BrowserCallable;
import reactor.core.publisher.Flux;

import java.util.Set;

@BrowserCallable
@AnonymousAllowed
public class AssistantService {

    private final LangChain4jAssistant aiChatService;

    public AssistantService(LangChain4jAssistant aiChatService) {
        this.aiChatService = aiChatService;
    }

    public Flux<String> chat(String chatId, String userMessage) {
            return aiChatService.chat(chatId, userMessage)
                .map(response -> {
                    if (containsUnrecognizedToolCall(response)) {
                        return "Sorry, I can only search Macedonian products.";
                    }
                    return response;
                });
    }

    private boolean containsUnrecognizedToolCall(String response) {
        if (!response.contains("tool_call")) {
            return false; // it's just a chat response
        }

        Set<String> knownTools = Set.of(
                "findAllProductsByName",
                "findCheapestMarketForProduct",
                "findAllProductsByNameAndMarket",
                "findClosestProductNames"
        );

        return knownTools.stream().noneMatch(response::contains);
    }
}
