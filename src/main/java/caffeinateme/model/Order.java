package caffeinateme.model;

import java.util.List;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicLong;

public class Order {
    private final int quantity;
    private final String product;
    private final Customer customer;
    private OrderStatus status;
    /*
    By providing multiple constructors (like Order), you offer flexibility and ease of use to the consumers of the Order class,
    allowing them to create orders with various configurations without the need for complex initialization.
     */
    public Order(int quantity, String product, Customer customer) {
        this(quantity,product, customer, OrderStatus.Normal);
    }

    public Order(int quantity, String product, Customer customer, OrderStatus status) {
        this.quantity = quantity;
        this.product = product;
        this.customer = customer;
        this.status = status;
    }

    //private final Long orderNumber;

   // private final static AtomicLong ORDER_NUMBERS = new AtomicLong();


//    public Order(List<OrderItem> items, Customer customer) {
//        this(items, customer, OrderStatus.Normal, "");
//    }

//    public Order(List<OrderItem> items, Customer customer, String comment) {
//        this(items, customer, OrderStatus.Normal, comment);
//    }

//    public Order(int items, String customer, Customer status, OrderStatus comment) {
//        this(items, customer, status, comment, ORDER_NUMBERS.incrementAndGet());
//    }


    public Order withStatus(OrderStatus status) {
        this.status = status;
        return this;
    }

    public OrderStatus getStatus() {
        return status;
    }

   // public List<OrderItem> getItems() { return items; }

   // public String getComment() { return comment; }

    public Customer getCustomer() {
        return customer;
    }

    public static OrderBuilder of(int quantity, String product) {
        return new OrderBuilder(quantity, product);
    }

    public void updateStatusTo(OrderStatus status) {
        this.status = status;
    }

    public String getProduct() {
        return product;
    }

    public int getQuantity() {
    return quantity;
   }

    public static class OrderBuilder {

        private final int quantity;
        private final String product;

        public OrderBuilder(int quantity, String product) {
            this.quantity = quantity;
            this.product = product;
        }

        public Order forCustomer(Customer customerName) {
            return new Order(quantity, product, customerName);
        }
    }
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Order order = (Order) o;
        return quantity == order.quantity &&
                Objects.equals(product, order.product) &&
                Objects.equals(customer, order.customer);
    }

    @Override
    public int hashCode() {
        return Objects.hash(quantity, product, customer);
    }
}
