package caffeinateme.model;

import java.util.ArrayList;
import java.util.List;

public class ProductCatalog {
    List<ProductPrice> catalog = new ArrayList<>();
    public void addProductWithPrices(List<ProductPrice> productPrices) {
        catalog.addAll(productPrices);
    }
    /*
    -The priceOf method takes a productName as input and returns the price of the product with that name
    from a catalog;
    -The filter operation is used to filter the products in the catalog based on whether their product
    name matches the input productName.
    -The findFirst operation returns the first product that matches the filter condition.
    If no product matches, it will return an empty Optional
    -.getPrice() is called on the product to retrieve its price, and this price is returned as the result of the method.
     */
    public double priceOf(String productName) {
        return catalog.stream()
                .filter(product -> product.getProduct().equals(productName))
                .findFirst()
                .orElseThrow(UnknownProductException::new)
                .getPrice();
    }
}
