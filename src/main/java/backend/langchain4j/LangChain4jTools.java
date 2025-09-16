package backend.langchain4j;

import backend.data.Product;
import backend.data.ProductSearchResponseDTO;
import backend.service.AiHelperService;
import backend.service.ProductService;
import dev.langchain4j.agent.tool.Tool;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicBoolean;

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
                Алатаката се користи за да се најдат продукти.
                Доколку булеан вредноста 'exactMatch' е точна, тогаш е најден точен производ и треба да се испишат сите податоци за производите на корисникот
                Доколку булеан вредноста 'exactMatch' не е точна, тогаш се најдени слични производи и треба да се испишат имињата на продуктите.
                Оваа функција може да работи и со текст (име на продукт) и со бројка (индекс).
            """)
    public ProductSearchResponseDTO findProducts(String query) {
        AtomicBoolean called = toolCallGuard.get();

        // Trying to cancel the model from calling the tools in cycles, bug from hallucinating.
        if (called.getAndSet(true)) {
            return new ProductSearchResponseDTO(
                    service.findAllByName(lastSearchResults.isEmpty() ? "" : lastSearchResults.get(0)),
                    Collections.emptyList(), true
            );
        }

        String productName;
        if (query.matches("\\d+")) {
            int index = Integer.parseInt(query);
            if (lastSearchResults == null || lastSearchResults.isEmpty()) {
                throw new IllegalStateException("Нема претходни резултати за избор.");
            }
            if (index < 1 || index > lastSearchResults.size()) {
                throw new IllegalArgumentException("Надвор од граници: " + index);
            }
            productName = lastSearchResults.get(index - 1);
        } else {
            productName = query.trim().toUpperCase();
        }

        List<Product> results = service.findAllByName(productName);

        if (!results.isEmpty()) {
            return new ProductSearchResponseDTO(results, Collections.emptyList(), true);
        } else {

            lastSearchResults = aiHelper.findClosestProducts(query);

            return new ProductSearchResponseDTO(Collections.emptyList(), lastSearchResults, false);
        }
    }

    @Tool("""
            За побараниот продукт, го наоѓа и маркетот во кој се продава за најевтина цена.
            """)
    public String findCheapestMarketForProduct(String productName) {
        Optional<Product> product = service.findByNameOrderByPriceAsc(productName.toUpperCase());

        return product
                .map(value -> "Производот  " + value.getName() + " е најевтин во "
                        + value.getMarket() + " по цена од " + value.getPriceInMkd() + " ден.")
                .orElse("Производот не може да се пронајде.");
    }

    @Tool("""
            Дава информации за специфичен продукт во специфичен маркет.
            Внесот на корисникот мора да е: "ИмеНаПроизвод, ИмеНаМаркет".
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

        String market = parts[1].trim().toLowerCase();

        if (!normalizedMarkets.contains(market)) {
            throw new IllegalArgumentException("Непознат маркет:" + market);
        }

        market = market.substring(0, 1).toUpperCase() + market.substring(1).toLowerCase();

        return service.findByNameAndMarket(productName, market);
    }
}
