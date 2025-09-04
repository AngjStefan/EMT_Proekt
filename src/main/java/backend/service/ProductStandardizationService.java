package backend.service;

import org.springframework.stereotype.Service;

@Service
public class ProductStandardizationService {
    /**
     * Standardizes a product name:
     * - Converts to uppercase
     * - Normalizes measurement units: g, гр → Г
     *   mg, мг → МГ
     *   kg, кг → КГ
     *   l, л → Л
     *   ml, мл → МЛ
     *   cm, цм → СМ
     *   mm, мм → ММ
     *
     * Only matches numbers immediately followed by the unit.
     *
     * @param productName Original product name
     * @return Standardized product name
     */
    public String standardize(String productName) {
        if (productName == null || productName.isEmpty()) {
            return productName;
        }

        String standardized = productName.toUpperCase();

        standardized = standardized
                .replaceAll("(\\d+)(G|GR|ГР)", "$1Г")
                .replaceAll("(\\d+)(MG|МГ)", "$1МГ")
                .replaceAll("(\\d+)(KG|КГ)", "$1КГ")
                .replaceAll("(\\d+)(ML|МЛ)", "$1МЛ")
                .replaceAll("(\\d+)(L|Л)", "$1Л")
                .replaceAll("(\\d+)(CM|ЦМ)", "$1СМ")
                .replaceAll("(\\d+)(MM|ММ)", "$1ММ");

        return standardized;
    }
}
