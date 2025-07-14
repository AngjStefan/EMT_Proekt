package backend.data;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

    List<Product> findAllByName(String productName);
    Optional<Product> findProductByName(String productName);
    Optional<Product> findProductByNameAndMarket(String productName, String marketName);
}
