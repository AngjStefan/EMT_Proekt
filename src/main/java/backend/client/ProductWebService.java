package backend.client;

import backend.data.PagedResponse;
import backend.data.Product;
import com.vaadin.flow.server.auth.AnonymousAllowed;
import com.vaadin.hilla.BrowserCallable;
import backend.service.ProductService;
import org.springframework.data.domain.PageRequest;

import java.util.List;

@BrowserCallable
@AnonymousAllowed
public class ProductWebService {

    private final ProductService productService;

    public ProductWebService(ProductService productService) {
        this.productService = productService;
    }

    // Force Hilla to generate Product model
    public Product getProductExample() {
        return new Product();
    }

    public PagedResponse<Product> getProducts(int page, int size) {
        var pageResult = productService.findAll(PageRequest.of(page, size));
        return new PagedResponse<>(pageResult.getContent(), pageResult.getTotalElements());
    }

    public PagedResponse<Product> findAllProductsByName(String productName, int page, int size) {
        var pageResult = productService.findByNameContainingIgnoreCase(productName, PageRequest.of(page, size));
        return new PagedResponse<>(pageResult.getContent(), pageResult.getTotalElements());
    }
}
