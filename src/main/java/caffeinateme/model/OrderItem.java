package caffeinateme.model;

import java.util.Objects;

public class OrderItem {
    private final String product;
    private final int quantity;
    public OrderItem(String product, int quantity) {
        this.product = product;
        this.quantity = quantity;
    }
    public int getQuantity() {
        return quantity;
    }

    public String getProduct() {
        return product;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (obj == null || obj.getClass() != this.getClass()) return false;
        var that = (OrderItem) obj;
        return Objects.equals(this.product, that.product) &&
                this.quantity == that.quantity;
    }

    @Override
    public int hashCode() {
        return Objects.hash(product, quantity);
    }

    @Override
    public String toString() {
        return "OrderItem[" +
                "product=" + product + ", " +
                "quantity=" + quantity + ']';
    }

}

