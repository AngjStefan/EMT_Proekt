package backend.client;

import backend.ollama.AiChatService;
import com.vaadin.flow.server.auth.AnonymousAllowed;
import com.vaadin.hilla.BrowserCallable;
import reactor.core.publisher.Flux;

@BrowserCallable
@AnonymousAllowed
public class AssistantService {

    private final AiChatService aiChatService;

    public AssistantService(AiChatService aiChatService) {
        this.aiChatService = aiChatService;
    }

    public Flux<String> chat(String chatId, String userMessage) {
        return aiChatService.chat(chatId, userMessage);
    }
}
