package backend.service;

import backend.data.Product;
import backend.data.ProductRepository;
import com.vaadin.flow.server.auth.AnonymousAllowed;
import com.vaadin.hilla.Endpoint;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ProductService {

    private final ProductRepository productRepository;
    private final ProductStandardizationService standardization;

    public ProductService(ProductRepository productRepository, ProductStandardizationService productStandardizationService) {
        this.productRepository = productRepository;
        this.standardization = productStandardizationService;
    }


    public List<Product> findAll() {
        return productRepository.findAll();
    }

    public List<Product> findAllByName(String productName) {
        return productRepository.findAllByName(productName);
    }

    public Optional<Product> findById(Long id) {
        return productRepository.findById(id);
    }

    public Optional<Product> findByName(String productName) {
        return productRepository.findProductByName(productName);
    }

    public Optional<Product> findByNameAndMarket(String productName, String marketName) {
        return productRepository.findProductByNameAndMarket(productName, marketName);
    }

    public Optional<Product> findByNameOrderByPriceAsc(String productName) {
        return productRepository.findByNameOrderByPriceInMkdAsc(productName);
    }

    public Optional<Product> save(Product product) {
        if (product != null) {
            String productName = standardization.standardize(product.getName());
            return Optional.of(productRepository.save(new Product(productName, product.getPriceInMkd(), product.getMarket())));
        }
        return Optional.empty();
    }

    public Optional<Product> update(Long id, Product product) {
        String productName = standardization.standardize(product.getName());
        return findById(id)
                .map(existingProduct -> {
                    existingProduct.setName(productName);
                    existingProduct.setPriceInMkd(product.getPriceInMkd());
                    existingProduct.setMarket(product.getMarket());
                    return productRepository.save(existingProduct);
                });
    }

    public List<String> findAllUniqueProductNames() {
        return productRepository.findDistinctProductNames();
    }

    public List<String> findAllUniqueProductMarketNames() {
        return productRepository.findDistinctProductMarkets();
    }

    public List<Product> searchProductsbyName (String name) { return  productRepository.findByNameContainingIgnoreCase(name);}

    public void deleteAll() {
        productRepository.deleteAll();
    }
}
