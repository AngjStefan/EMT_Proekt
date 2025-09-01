package backend.controller;

import backend.ollama.AiChatService;
import backend.service.AiHelperService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;

import java.util.List;

@RestController
@RequestMapping("/chat")
@CrossOrigin(origins = "*")
@Tag(name = "Chat API", description = "Endpoints for managing chat")
public class TestController {

    private final AiChatService chatService;
    private final AiHelperService aiHelper;

    public TestController(AiChatService chatService, AiHelperService aiHelper) {
        this.chatService = chatService;
        this.aiHelper = aiHelper;
    }

    @GetMapping
    public Flux<String> getChat(@RequestParam(defaultValue = "Hello") String message) {
        Flux<String> response = chatService.chat("1",message);

        return response;
    }

    @GetMapping("/embedAll")
    public void embedAll() {
        aiHelper.embedAllProducts();
    }
}
