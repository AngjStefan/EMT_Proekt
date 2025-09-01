package backend.data;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @Column(name = "price_in_mkd")
    private Integer priceInMkd;

    private String market;

    public Product(){}

    public Product(String name, Integer price_in_mkd, String market) {
        this.name = name;
        this.priceInMkd = price_in_mkd;
        this.market = market;
    }
}
