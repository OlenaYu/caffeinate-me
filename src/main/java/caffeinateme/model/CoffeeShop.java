package caffeinateme.model;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

public class CoffeeShop {
    private final ProductCatalog productCatalog;

    public CoffeeShop(ProductCatalog productCatalog) {
        this.productCatalog = productCatalog;
    }
    private static final double MAX_DISTANCE = 10000;
    private Queue<Order> orders = new LinkedList<>();
    private Map<String, Customer> registeredCustomers = new HashMap<>();

    public void placeOrder(Order order, double distanceInMetres) {
        placeOrder(order, MAX_DISTANCE);
    }

    public void placeOrder(Order order, Double distanceInMetres) {
        if (distanceInMetres <= 200) {
            order = order.withStatus(OrderStatus.Urgent);
        }
        orders.add(order);
    }

    public List<Order> getPendingOrders() {
        return new ArrayList<>(orders);
    }

    public Optional<Order> getOrderFor(Customer customer) {
        return orders.stream().filter( order -> order.getCustomer().equals(customer)).findFirst();
    }

    public Customer registerNewCustomer(String customerName) {
        Customer newCustomer = Customer.named(customerName);
        registeredCustomers.put(customerName, newCustomer);
        return newCustomer;
    }

    public void setCustomerETA(Customer customer, int etaInMinutes) {
        getOrderFor(customer).ifPresent(
                order -> {
                    if (etaInMinutes < 5) {
                        order.updateStatusTo(OrderStatus.Urgent);
                    } else if (etaInMinutes > 10) {
                        order.updateStatusTo(OrderStatus.Normal);
                    } else {
                        order.updateStatusTo(OrderStatus.High);
                    }
                }
        );
    }
    //returns a Receipt object for a specific customer
    public Receipt getReceiptFor(Customer customer) {
        List<Order> customerOrders = orders.stream()
                .filter( order -> order.getCustomer().equals(customer))
                .collect(Collectors.toList());

    /*
    this::subtotalFor is a shorthand way of referring to the subtotalFor method of the current class.
     This method reference is then used within the mapToDouble function to apply the subtotalFor
     method to each element in the customerOrders list when computing the subtotal.
     */
        double subTotal = customerOrders.stream().mapToDouble(this::subtotalFor).sum();
        List<ReceiptLineItem> lineItems = customerOrders.stream()
                .map(order -> new ReceiptLineItem(order.getProduct(), order.getQuantity(), subtotalFor(order)))
                .collect(Collectors.toList());

        double serviceFee = subTotal * 5 / 100;
        double total = subTotal + serviceFee;

        return new Receipt(roundedTo2DecimalPlaces(subTotal),
                roundedTo2DecimalPlaces(serviceFee),
                roundedTo2DecimalPlaces(total),
                lineItems);
    }
    /*
     roundedTo2DecimalPlaces() method takes a double value, rounds it to two decimal places using the "round half up" method
     (also known as "banker's rounding"), and returns the result as a double. This is often used to ensure that
     a double value is displayed with a specific number of decimal places, which can be useful in financial
     or mathematical calculations where precision is important
     */
    private double roundedTo2DecimalPlaces(double value) {
        return new BigDecimal(Double.toString(value)).setScale(2, BigDecimal.ROUND_HALF_UP)
                .doubleValue();
    }

    private double subtotalFor(Order order) {
        return productCatalog.priceOf(order.getProduct()) * order.getQuantity();
    }
}
