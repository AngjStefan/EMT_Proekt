package backend.ollama;

import backend.data.Product;
import backend.service.AiHelperService;
import backend.service.ProductService;
import dev.langchain4j.agent.tool.Tool;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class OllamaTools {

    private final ProductService service;
    private final AiHelperService aiHelper;

    public OllamaTools(ProductService service, AiHelperService aiHelper) {
        this.service = service;
        this.aiHelper = aiHelper;
    }

    @Tool("""
            Retrieves data about all products with a certain name,
            such as the market where the products are available and the price of the products in each market (in the currency MKD).
            """)
    public List<Product> findAllProductsByName(String productName) {
        return service.findAllByName(productName);
    }

    @Tool("""
            Retrieves the market where a product with a given name is sold at the lowest price (in MKD).
            Returns a information about where the product is sold at the lowest price (in MKD).
            """)
    public String findCheapestMarketForProduct(String productName) {
        Optional<Product> product = service.findByNameOrderByPriceAsc(productName);

        return product
                .map(value -> "The product " + value.getName() + " is sold the cheapest at "
                        + value.getMarket() + " at the price of " + value.getPriceInMkd() + " MKD.")
                .orElse("The product could not be found.");
    }

    @Tool("""
            Retrieves the full data about a products with a certain name and the market where it's sold.
            The user must provide the input in the format: "ProductName, MarketName".
            Example: "7 Days - Кроасани мини какао 60g, Кам".
            """)
    public Optional<Product> findAllProductsByNameAndMarket(String query) {
        List<String> markets = service.findAllUniqueProductMarketNames();
        List<String> normalizedMarkets = markets.stream()
                .map(String::toLowerCase)
                .toList();


        String[] parts = query.split(",", 2);
        if (parts.length < 2) {
            throw new IllegalArgumentException("Invalid format. Expected: ProductName, MarketName");
        }

        String productName = parts[0].trim();

        String market = parts[1].trim();
        market.toLowerCase();


        if (!normalizedMarkets.contains(market)) {
            throw new IllegalArgumentException("Unknown market: " + market);
        }

        return service.findByNameAndMarket(productName, market);
    }


    @Tool("""
            Retrieves the 3 most similar product names to the given query string.
            """)
    public List<String> findClosestProductNames(String query) {
        return aiHelper.findClosestProducts(query);
    }


}
