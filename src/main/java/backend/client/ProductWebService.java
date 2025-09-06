package backend.client;

import backend.data.Product;
import com.vaadin.flow.server.auth.AnonymousAllowed;
import com.vaadin.hilla.BrowserCallable;
import backend.service.ProductService;

import java.util.List;

@BrowserCallable
@AnonymousAllowed
public class ProductWebService {

    private final ProductService productService;

    public ProductWebService(ProductService productService) {
        this.productService = productService;
    }

    public List<Product> getProducts() {
        return productService.findAll();
    }

    public List<Product> searchProductsbyName(String name) {
        return productService.searchProductsbyName(name);
    }
}
