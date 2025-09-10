package backend.client;

import backend.langchain4j.LangChain4jAssistant;
import backend.langchain4j.ToolCallGuard;
import com.vaadin.flow.server.auth.AnonymousAllowed;
import com.vaadin.hilla.BrowserCallable;
import reactor.core.publisher.Flux;

import java.util.Set;

@BrowserCallable
@AnonymousAllowed
public class AssistantService {

    private final ToolCallGuard toolCallGuard;
    private final LangChain4jAssistant aiChatService;


    public AssistantService(ToolCallGuard toolCallGuard, LangChain4jAssistant aiChatService) {
        this.toolCallGuard = toolCallGuard;
        this.aiChatService = aiChatService;
    }

    public Flux<String> chat(String chatId, String userMessage) {
        toolCallGuard.reset();

        return aiChatService.chat(chatId, userMessage)
                .map(response -> {
                    if (containsUnrecognizedToolCall(response)) {
                        return "Sorry, I can only search Macedonian products.";
                    }
                    return response;
                })
                .doFinally(signalType -> toolCallGuard.clear());
    }

    private boolean containsUnrecognizedToolCall(String response) {
        if (!response.contains("tool_call")) {
            return false;
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
