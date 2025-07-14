package backend.data;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;

@Data
@Entity
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private Integer price_in_mkd;

    private String market;

    public Product(){}

    public Product(String name, Integer price_in_mkd, String market) {
        this.name = name;
        this.price_in_mkd = price_in_mkd;
        this.market = market;
    }
}
