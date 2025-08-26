package backend.controller;

import backend.ollama.AiChatService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;

@RestController
@RequestMapping("/chat")
@CrossOrigin(origins = "*")
@Tag(name = "Chat API", description = "Endpoints for managing chat")
public class TestController {

    private final AiChatService chatService;

    public TestController(AiChatService chatService) {
        this.chatService = chatService;
    }

    @GetMapping
    public Flux<String> getChat(@RequestParam(defaultValue = "Hello") String message) {
        Flux<String> response = chatService.chat("1",message);

        return response;
    }
}
