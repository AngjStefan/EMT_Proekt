package backend.langchain4j;

import backend.data.Product;
import backend.service.ProductService;
import dev.langchain4j.agent.tool.Tool;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class LangChain4jTools {

    private final ProductService service;

    public LangChain4jTools(ProductService service) {
        this.service = service;
    }

    @Tool("""
            Retrieves data about all products with a certain name,
            such as the market where the products are available and the price of the products in each market (in the currency MKD).
            """)
    public List<Product> findAllProductsByName(String productName) {
        return service.findAllByName(productName);
    }
}
