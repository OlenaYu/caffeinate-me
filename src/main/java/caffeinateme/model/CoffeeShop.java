package caffeinateme.model;

import java.util.*;

public class CoffeeShop {

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
        return orders.stream()
                .filter( order -> order.getCustomer().equals(customer))
                .findFirst();
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
}
