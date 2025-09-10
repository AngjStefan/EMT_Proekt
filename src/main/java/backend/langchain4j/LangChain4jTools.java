package backend.langchain4j;

import backend.data.Product;
import backend.service.AiHelperService;
import backend.service.ProductService;
import dev.langchain4j.agent.tool.Tool;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Collectors;

@Component
public class LangChain4jTools {

    private final ProductService service;
    private final AiHelperService aiHelper;
    private final ToolCallGuard toolCallGuard;
    private List<String> lastSearchResults;

    public LangChain4jTools(ProductService service, AiHelperService aiHelper, ToolCallGuard toolCallGuard) {
        this.service = service;
        this.aiHelper = aiHelper;
        this.toolCallGuard = toolCallGuard;
        this.lastSearchResults = new ArrayList<>();
    }

    @Tool("""
        It finds all the products matching the given query. It can also work with typing an integer (index),
        if the lastSearchResults list isn't empty.
        If it returns an empty list (didn't find a matching product) it calls the function 'findClosestProductNames'.
        You CANNOT call this tool more than once before returning a response.
        """)
    public List<Product> findAllProductsByName(String query) {
        AtomicBoolean called = toolCallGuard.get();
        if (called.getAndSet(true)) {
            if (lastSearchResults.isEmpty()) {
                return Collections.emptyList();
            } else {
                return service.findAllByName(lastSearchResults.get(0));
            }
        }
        String productName;

        if (query.matches("\\d+")) { // number selection
            int index = Integer.parseInt(query);
            if (index >= 0 && index < lastSearchResults.size()) {
                productName = lastSearchResults.get(index);
            } else {
                throw new IllegalArgumentException("Надвор од граници: " + index);
            }
        } else {
            productName = query.trim().toUpperCase();
        }

        List<Product> results = service.findAllByName(productName);

        if (!results.isEmpty()) {
            lastSearchResults = results.stream()
                    .map(Product::getName)
                    .collect(Collectors.toList());
        }

        return results;
    }

    @Tool("""
            It finds where the product is sold at the cheapest.
            """)
    public String findCheapestMarketForProduct(String productName) {
        Optional<Product> product = service.findByNameOrderByPriceAsc(productName);

        return product
                .map(value -> "Производот  " + value.getName() + " е најевтин во "
                        + value.getMarket() + " по цена од " + value.getPriceInMkd() + " ден.")
                .orElse("Производот не може да се пронајде.");
    }

    @Tool("""
            It shows the product's details in a specific market.
            The user's query must be like: "ИмеНаПроизвод, ИмеНаМаркет".
            """)
    public Optional<Product> findAllProductsByNameAndMarket(String query) {
        List<String> markets = service.findAllUniqueProductMarketNames();
        List<String> normalizedMarkets = markets.stream()
                .map(String::toLowerCase)
                .toList();


        String[] parts = query.split(",", 2);
        if (parts.length < 2) {
            throw new IllegalArgumentException("Невалиден формат. Очекувано: ИмеНаПроизвод, ИмеНаМаркет");
        }

        String productName = parts[0].trim();

        String market = parts[1].trim();
        market.toLowerCase();


        if (!normalizedMarkets.contains(market)) {
            throw new IllegalArgumentException("Непознат маркет:" + market);
        }

        return service.findByNameAndMarket(productName, market);
    }


    @Tool("""
            It returns the most similar products to the user's query.
            The function is ONLY called if 'findAllProductsByName' returns an empty list.
            """)
    public List<String> findClosestProductNames(String query) {
        return lastSearchResults = aiHelper.findClosestProducts(query);
    }
}
