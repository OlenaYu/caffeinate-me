package caffeinateme.steps;

import caffeinateme.model.*;
import io.cucumber.java.DataTableType;
import io.cucumber.java.ParameterType;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.cucumber.java.eo.Do;
import net.thucydides.core.annotations.Steps;

import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

public class OrderCoffeeSteps {
    Customer customer = Customer.named("Cathy");
    ProductCatalog productCatalog = new ProductCatalog();
    CoffeeShop coffeeShop = new CoffeeShop(productCatalog);
    Order order;
//    @Steps
//    ProductCatalog productCatalog;

    @Given("^(.*) is a CaffeinateMe customer")
    public void a_caffeinate_me_customer_named(String customerName) {
        customer = coffeeShop.registerNewCustomer(customerName);
    }

    @Given("Cathy is {double} metre(s) from the coffee shop")
    public void cathy_is_metres_from_the_coffee_shop(Double distanceInMetres) {
        System.out.println(distanceInMetres);
        customer.setDistanceFromShop(distanceInMetres);
    }

    @When("^Cathy (?:orders|has ordered) an? (.*)$")
    public void cathy_orders_a(String orderedProduct) {
        this.order = Order.of(1, orderedProduct).forCustomer(customer);
        customer.placesAnOrderFor(order).at(coffeeShop);
    }

    @Then("Barry should receive the order")
    public void barry_should_receive_the_order() {
        assertThat(coffeeShop.getPendingOrders()).contains(order);
    }

    @ParameterType(value = "(Normal|High|Urgent)")
    public OrderStatus orderStatus(String statusValue) {
        return OrderStatus.valueOf(statusValue);
    }

    @Then("Barry should know that the order is {orderStatus}")
    public void barry_should_know_that_the_order_is(OrderStatus expectedStatus) {
        Order cathysOrder = coffeeShop.getOrderFor(customer)
                .orElseThrow(() -> new AssertionError("No order found!"));
        assertThat(cathysOrder.getStatus()).isEqualTo(expectedStatus);
    }

    @When("Cathy is {int} minutes away")
    public void cathyIsNMinutesAway(int etaInMinutes) {
        coffeeShop.setCustomerETA(customer, etaInMinutes);
    }

    //ASKING FOR A RECEIPT FEATURE
    @DataTableType
    public OrderItem mapRowToOrderItem(Map<String, String> entry) {
        return new OrderItem(entry.get("Product"), Integer.parseInt(entry.get("Quantity")));
    }

    @Given("^Cathy has ordered:$")
    public void hasOrdered(List<OrderItem> orders) {
        for (OrderItem item : orders) {
            Order order = Order.of(item.getQuantity(), item.getProduct()).forCustomer(customer);
            customer.placesAnOrderFor(order).at(coffeeShop);
        }
    }

    /* This method converts rows from tables like this to ProductPrice objects
         | Product            | Price |
         | regular cappuccino | 1.90  |
         | large cappuccino   | 2.25  |
         |  muffin            | 1.25  |
    */
    @DataTableType
    public ProductPrice mapRowToProduct(Map<String, String> entry) {
        return new ProductPrice(entry.get("Product"), Double.parseDouble(entry.get("Price")));
    }

    @Given("^the following prices:$")
    public void theFollowingPrices(List<ProductPrice> productPrices) {
        productCatalog.addProductWithPrices(productPrices);
        System.out.println(productPrices);
    }

    Receipt receipt;

    @When("she asks for a receipt")
    public void sheAsksForAReceipt() {
        receipt = coffeeShop.getReceiptFor(customer);

    }

    /* This method checks the totals in the receipt. For this step, we will extract the values we need from
    the table data, which is passed in as a list of maps, and then compare these values with the ones we got in
     the receipt:
     */
    @Then("^she should receive a receipt totalling:$")
    public void sheShouldReceiveAReceiptTotalling(List<Map<String, String>> receiptTotals) {
        Double subtotal = Double.parseDouble(receiptTotals.get(0).get("Subtotal"));
        Double serviceFee = Double.parseDouble(receiptTotals.get(0).get("Service Fee"));
        Double total = Double.parseDouble(receiptTotals.get(0).get("Total"));

        assertThat(receipt.getSubtotal()).isEqualTo(subtotal);
        assertThat(receipt.getServiceFee()).isEqualTo(serviceFee);
        assertThat(receipt.getTotal()).isEqualTo(total);
    }
    /*Finally, we need to check the receipt line items. You can do this by first creating a
     @DataTableType-annotated method to convert a row in the scenario table to a ReceiptLineItem domain class:
     */
    @DataTableType
    public ReceiptLineItem mapRowToReceiptLineItem (Map<String, String> entry) {
        return new ReceiptLineItem(entry.get("Product"),
                Integer.parseInt((entry.get("Quantity"))),
                Double.parseDouble(entry.get("Price")));
    }
    @And("the receipt should contain the line items:")
    public void theReceiptShouldContainTheLineItems(List<ReceiptLineItem> expectedItems) {
        assertThat(receipt.getLineItems()).containsExactlyElementsOf(expectedItems);
    }
}
