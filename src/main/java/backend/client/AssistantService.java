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
                .doFinally(signalType -> toolCallGuard.clear());
    }
}
