package caffeinateme.model;

import java.util.Objects;

public final class ReceiptLineItem {
    private final String product;
    private final int quantity;
    private final double price;

    public ReceiptLineItem(String product, int quantity, double price) {
        this.product = product;
        this.quantity = quantity;
        this.price = price;
    }

    public String getProduct() {
        return product;
    }

    public int getQuantity() {
        return quantity;
    }

    public double getPrice() {
        return price;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (obj == null || obj.getClass() != this.getClass()) return false;
        var that = (ReceiptLineItem) obj;
        return Objects.equals(this.product, that.product) &&
                Objects.equals(this.quantity, that.quantity) &&
                Objects.equals(this.price, that.price);
    }

    @Override
    public int hashCode() {
        return Objects.hash(product, quantity, price);
    }

    @Override
    public String toString() {
        return "ReceiptLineItem[" +
                "product=" + product + ", " +
                "quantity=" + quantity + ", " +
                "subtotalFor=" + price + ']';
    }

}
