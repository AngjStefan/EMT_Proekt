package backend.client;

import backend.langchain4j.LangChain4jAssistant;
import com.vaadin.flow.server.auth.AnonymousAllowed;
import com.vaadin.hilla.BrowserCallable;
import reactor.core.publisher.Flux;

@BrowserCallable
@AnonymousAllowed
public class AssistantService {

    private final LangChain4jAssistant aiChatService;

    public AssistantService(LangChain4jAssistant aiChatService) {
        this.aiChatService = aiChatService;
    }

    public Flux<String> chat(String chatId, String userMessage) {
        return aiChatService.chat(chatId, userMessage);
    }
}
