package backend.data;

import java.util.List;

public record ProductSearchResponseDTO(
        List<Product> products,
        List<String> suggestions,
        boolean exactMatch
) {

}
