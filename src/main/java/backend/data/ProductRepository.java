package backend.data;

import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

    List<Product> findAllByName(String productName);

    Optional<Product> findProductByNameAndMarket(String productName, String marketName);

    Optional<Product> findByNameOrderByPriceInMkdAsc(String name);

    Page<Product> findByNameContainingIgnoreCase(String name, Pageable pageable);

    Page<Product> findAll(Pageable pageable);

    @Query("SELECT DISTINCT p.name FROM Product p")
    List<String> findDistinctProductNames();

    @Query("SELECT DISTINCT p.market FROM Product p")
    List<String> findDistinctProductMarkets();
}
