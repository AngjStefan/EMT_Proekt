package backend.controller;

import backend.data.Product;
import backend.langchain4j.LangChain4jAssistant;
import backend.service.AiHelperService;
import backend.service.ProductService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;

import java.util.List;

@RestController
@RequestMapping("/chat")
@CrossOrigin(origins = "*")
@Tag(name = "Chat API", description = "Endpoints for managing chat")
public class TestController {

    private final LangChain4jAssistant chatService;
    private final AiHelperService aiHelper;
    private final ProductService productService;

    public TestController(LangChain4jAssistant chatService, AiHelperService aiHelper, ProductService productService) {
        this.chatService = chatService;
        this.aiHelper = aiHelper;
        this.productService = productService;
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

    @GetMapping("/updateAll")
    public String updateAll() {

        List<Product> products = productService.findAll();

        products.forEach(p -> productService.update(p.getId(), p));

        return "Total products updated: " + products.size();
    }
}
