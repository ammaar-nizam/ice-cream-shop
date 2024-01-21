package shop;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

// Main class
public class IceCreamShop {
	public static void main(String[] args) {

		// Set a seasonal promotion with 5% of the total order
		SeasonalPromotion newYearSeasonalPromotion = new SeasonalPromotion(0.05, LocalDate.of(2024, 1, 1), LocalDate.of(2024, 1, 31));
		
		// Create a user profile and a customer
		UserProfile userProfile = new UserProfile("ammaarnizam");
		Customer customer = new Customer("Ammaar");
		
		// Create a chain of handlers
		IceCreamHandler baseTypeHandler = new BaseTypeHandler();
		IceCreamHandler flavorHandler = new FlavorHandler();
		IceCreamHandler toppingHandler = new ToppingHandler();
		IceCreamHandler syrupHandler = new SyrupHandler();
		IceCreamHandler extraHandler = new ExtraHandler();

		// Set their next handlers
		baseTypeHandler.setNext(flavorHandler);
		flavorHandler.setNext(toppingHandler);
		toppingHandler.setNext(syrupHandler);
		syrupHandler.setNext(extraHandler);

		// Customization of ice creams
		ScoopIceCream item1 = new ScoopIceCream();
		item1.setBaseType("Cone");
		item1.setFlavor("Butterscotch");
		item1.addTopping("Sliced strawberries");
		item1.addTopping("Blueberries ");
		item1.pourSyrup("Maple Syrup");
		item1.addExtra("Marshmallows");
		item1.setName("Butterscoth Cone Delight");

		// Initiate the handling process by calling handleRequest on the first handler in the chain.
		baseTypeHandler.handleRequest(item1);

		Kulfi item2 = new Kulfi();
		item2.setFlavor("Vanilla");
		item2.addTopping("Almonds");
		item2.addTopping("Cashews ");
		item2.pourSyrup("Chocolate Syrup");
		item2.addExtra("Chocolate Sprinkles");
		item2.setName("Simply Best Kulfi");

		// Initiate the handling process by calling handleRequest on the first handler in the chain.
		baseTypeHandler.handleRequest(item2);

		RolledIceCream item3 = new RolledIceCream();
		item3.setFlavor("Chocolate");
		item3.addTopping("Sliced strawberries");
		item3.pourSyrup("Chocolate Syrup");
		item3.pourSyrup("Hot fudge");
		item3.addExtra("Cookie Dough Bites");
		item3.setName("Chocolate Rolled Xtreme");

		// Initiate the handling process by calling handleRequest on the first handler in the chain.
		baseTypeHandler.handleRequest(item3);

		// Save the ice cream as a favorite for the user "Ammaar"
		userProfile.addFavoriteIceCream("MyFavourite", item3);
		
		// Create an order using the Builder pattern
		OrderBuilder orderBuilder = new OrderBuilder().
				addItem(item1).
				addItem(item2).
				addItem(item3).
				addObserver(new CustomerOrderObserver(customer.getName())).
				setCustomer(customer).
				setUserProfile(userProfile).
				setSeasonalPromotion(newYearSeasonalPromotion).
				setPaymentStrategy(new CreditCardPaymentStrategy());
		
		Order order = orderBuilder.build();
		// Now the order is built
		
		// Pack the order
		Order plasticWrappedIceCream = new PlasticWrappingDecorator(order);
		
		// Choosing delivery as an option
		order.setDeliveryInformation("34/44, Stewart Street, Colombo-2");

		// OrderDecorator plasticWrappingOrder = new PlasticWrappingDecorator(order);

		// Create a command for placing the order
		Command placeOrderCommand = new PlaceOrderCommand(order);
		// Execute the command to place the order
		placeOrderCommand.execute();
		// Now the order is placed
		// Add the order to the user's order history
        userProfile.addOrderToHistory(order);

		System.out.println(order.printOrder());
				
		// Earn loyalty points for the customer based on the total cost of the order
        int pointsEarned = (int) (order.calculateTotal() / 100); // Assume 1 point for every LKR 100.0 spent
        customer.earnLoyaltyPoints(pointsEarned);
        System.out.println("System message: \n" + plasticWrappedIceCream.getDescription() + "\n");
        System.out.println("System message: \nLoyalty Points Available: " + customer.getLoyaltyPoints() + "\n");

		// Placing another order right from the favourites
		// Retrieve and print the favorite ice cream for the user "Ammaar"
		IceCream favoriteIceCream = userProfile.getFavoriteIceCream("MyFavourite");

		// Create an order using the Builder pattern
		OrderBuilder orderBuilder1 = new OrderBuilder().
				addItem(favoriteIceCream).
				addObserver(new CustomerOrderObserver(customer.getName())).
				setCustomer(customer).
				setUserProfile(userProfile).
				setSeasonalPromotion(newYearSeasonalPromotion).
				setPaymentStrategy(new CashPaymentStrategy());
		
		Order newOrder = orderBuilder1.build();
		// Now the order is built
		
		// Pack the order
		Order boxedIceCream = new CardboardBoxPackagingDecorator(newOrder);

		// Choosing pickup as an option
		newOrder.setPickupInformation();

		// OrderDecorator plasticWrappingOrder = new PlasticWrappingDecorator(order);

		// Create a command for placing the order
		Command placeOrderCommand1 = new PlaceOrderCommand(newOrder);
		// Execute the command to place the order
		placeOrderCommand1.execute();
		// Now the order is placed
		// Add the order to the user's order history
        userProfile.addOrderToHistory(newOrder);

		System.out.println(newOrder.printOrder());
		
		// Earn loyalty points for the customer based on the total cost of the order
        int pointsEarned1 = (int) (newOrder.calculateTotal() / 100);
        customer.earnLoyaltyPoints(pointsEarned1);
        System.out.println("System message: \n" + boxedIceCream.getDescription() + "\n");
        System.out.println("System message: \nLoyalty Points Available: " + customer.getLoyaltyPoints() + "\n");
		
		// Simulate order progress
		// Setting the initial state
        order.setState(new PlacedState());
        newOrder.setState(new PlacedState());
		order.processOrder();
		newOrder.processOrder();
		
		/*ORDERS ARE IN PREPARATION*/
		order.processOrder();
		newOrder.processOrder();
		
		/*ORDERS ARE OUT FOR DELIVERY*/
        order.processOrder();
        System.out.println("Order will be delivered to " + order.getDeliveryInformation().getDeliveryAddress() + "\n");
        newOrder.processOrder();
        newOrder.processOrder();        
        
		/*ORDERS ARE COMPLETED*/
        order.processOrder();
        
        /*Customer providing feedback*/
        // Create a command for providing a feedback on one order
        Command provideFeedbackCommand = new ProvideFeedbackCommand("The order was quickly delivered and the ice creams were well packed. Rolled Ice Cream was my favourite. Highly recommended!");
        // Execute the command to provide feedback on the order
        provideFeedbackCommand.execute();

	}
}


// Behavioral - Observer Pattern starts here
/*
 An object, known as the subject, maintains a list of dependents, known as observers, that are notified of any changes to the subject's state.
*/
// OrderObserver interface with order object as the subject
abstract class OrderObserver {
    public abstract void update(Order order);
}

// Concrete observer class
class CustomerOrderObserver extends OrderObserver {
    private String customerName;

    public CustomerOrderObserver(String customerName) {
        this.customerName = customerName;
    }

    @Override
    public void update(Order order) {
        System.out.println("Real-Time Notification: \nDear " + customerName + ", your order " +order.getOrderId()+ " is " + order.getStatus().toLowerCase() + "\n");
    }
}

// Behavioral - Strategy Pattern starts here
/*
 Defines a family of algorithms, encapsulates each algorithm, and makes them interchangeable
*/
interface PaymentStrategy {
    String pay(double amount);
    String getPaymentConfirmation();
}

// A specific payment strategy
class CreditCardPaymentStrategy implements PaymentStrategy {
    @Override
    public String pay(double amount) {
        return "Paid LKR " + amount + " using credit card.";
    }
    
    @Override
    public String getPaymentConfirmation() {
        return "Credit card payment confirmed.";
    }
}

// Another payment strategy
class CashPaymentStrategy implements PaymentStrategy {
    @Override
    public String pay(double amount) {
        return "Paid LKR " + amount + " in cash.";
    }
    
    @Override
    public String getPaymentConfirmation() {
        return "Cash payment confirmed.";
    }
}

// Another payment strategy
class DigitalWalletStrategy implements PaymentStrategy {
    @Override
    public String pay(double amount) {
        return "Paid LKR " + amount + " using digital wallet.";
    }
    
    @Override
    public String getPaymentConfirmation() {
        return "Digital wallet payment confirmed.";
    }
}

// Behavioral - Chain of Responsibility Pattern starts here
/*
 Allows you to build a chain of objects that handle requests in a sequential manner. 
 Each handler in the chain decides either to process the request or to pass it to the next handler in the chain. 
*/
abstract class IceCreamHandler {
    public abstract void handleRequest(IceCream iceCream);
    public abstract void setNext(IceCreamHandler handler);
}

// Concrete handlers
class BaseTypeHandler extends IceCreamHandler {
	private IceCreamHandler next;

	public void setNext(IceCreamHandler next) {
		this.next = next;
	}

	@Override
	public void handleRequest(IceCream iceCream) {
		if (iceCream.getBaseType() == null) {
			System.out.println("Please select how you want your ice cream to be served.");
		} else if (next != null) {
			next.handleRequest(iceCream);
		}
	}
}

// Concrete handlers
class FlavorHandler extends IceCreamHandler {
    private IceCreamHandler next;

    public void setNext(IceCreamHandler next) {
        this.next = next;
    }

    @Override
    public void handleRequest(IceCream iceCream) {
        if (iceCream.getFlavor() == null) {
            System.out.println("Please select a flavor.");
        } else if (next != null) {
            next.handleRequest(iceCream);
        }
    }
}

// Concrete handlers
class ToppingHandler extends IceCreamHandler {
    private IceCreamHandler next;

    public void setNext(IceCreamHandler next) {
        this.next = next;
    }

    @Override
    public void handleRequest(IceCream iceCream) {
        if (iceCream.getToppings().isEmpty()) {
            System.out.println("Please select at least one topping.");
        } else if (next != null) {
            next.handleRequest(iceCream);
        }
    }
}

// Concrete handlers
class SyrupHandler extends IceCreamHandler {
	private IceCreamHandler next;

	public void setNext(IceCreamHandler next) {
		this.next = next;
	}

	@Override
	public void handleRequest(IceCream iceCream) {
		if (iceCream.getSyrups().isEmpty()) {
			System.out.println("Please select at least one syrup.");
		} else if (next != null) {
			next.handleRequest(iceCream);
		}
	}
}

// Concrete handlers
class ExtraHandler extends IceCreamHandler {
	private IceCreamHandler next;

	public void setNext(IceCreamHandler next) {
		this.next = next;
	}

	@Override
	public void handleRequest(IceCream iceCream) {
		if (iceCream.getExtras().isEmpty()) {
			System.out.println("Please select at least one extra.");
		} else if (next != null) {
			next.handleRequest(iceCream);
		}
	}
}

// Behavioral - Command Pattern starts here
/*
 Encapsulates a request as an object, thereby allowing for parameterization of clients with different requests, 
 queuing of requests, and logging of the parameters of a request.
*/
interface Command {
    void execute();
}

// Concrete command for representing the action of placing an order
class PlaceOrderCommand implements Command {
    private Order order;

    public PlaceOrderCommand(Order order) {
        this.order = order;
    }

    @Override
    public void execute() {
    	System.out.println("System message: \nPlacing a new order\nWorking on your order...\n");
        if (order.getItems().isEmpty()) {
            System.out.println("Please add an item to the order.");
            return;
        }
        order.setState(new PlacedState());
        System.out.println("System message: \nOrder placed successfully.\n");
    }
}

// Concrete command for representing the action of providing feedback
class ProvideFeedbackCommand implements Command {
    private String feedback;

    public ProvideFeedbackCommand(String feedback) {
        this.feedback = feedback;
    }

    @Override
    public void execute() {
    	System.out.println("Providing feedback: " + feedback + "\n");
        if (feedback.length() > 10000) {
            System.out.println("Feedback is too long. Maximum allowed characters: 100");
            return;
        }
        System.out.println("System message: \nThank you for providing a feedback. We value your feedback.");
    }
}

// Structural - Decorator Pattern starts here
/*
 Allows behavior to be added to an individual object, either statically or dynamically, 
 without affecting the behavior of other objects from the same class. 
 It is achieved by creating a set of decorator classes that are used to wrap concrete components.  
*/
abstract class OrderDecorator extends Order {
    protected Order decoratedOrder;

    public OrderDecorator(Order decoratedOrder) {
        this.decoratedOrder = decoratedOrder;
    }

    public String getDescription() {
        return decoratedOrder.getDescription();
    }

    @Override
    public double calculateTotal() {
        return decoratedOrder.calculateTotal();
    }
}

// Concrete decorator for adding plastic wrapping
class PlasticWrappingDecorator extends OrderDecorator { // For individual ice cream and sandwiches
    public PlasticWrappingDecorator(Order decoratedOrder) {
        super(decoratedOrder);
    }

    @Override
    public String getDescription() {
        return "Your order is packed using Plastic Wrapping";
    }

    @Override
    public double calculateTotal() {
        return super.calculateTotal() + 2.0; // Additional cost for plastic wrapping
    }
}

// Concrete decorator for adding cardboard box packaging
class CardboardBoxPackagingDecorator extends OrderDecorator { // For multi-packs
    public CardboardBoxPackagingDecorator(Order decoratedOrder) {
        super(decoratedOrder);
    }

    @Override
    public String getDescription() {
        return "Your order is packed using Cardboard Box Packaging";
    }

    @Override
    public double calculateTotal() {
        return super.calculateTotal() + 10.0; // Additional cost for cardboard box packaging
    }
}

//Concrete decorator for adding plastic tub packaging
class PlasticTubPackagingDecorator extends OrderDecorator { // For scoop ice creams
	public PlasticTubPackagingDecorator(Order decoratedOrder) {
		super(decoratedOrder);
	}

	@Override
	public String getDescription() {
		return "Your order is packed using Plastic Tub Packaging";
	}

	@Override
	public double calculateTotal() {
		return super.calculateTotal() + 25.0; // Additional cost for plastic tub packaging
	}
}

// Creational - Builder Pattern starts here
/*
 Separates the construction of a complex object from its representation, allowing the same construction process to create various representations. 
 It involves a director class, a builder interface, and concrete builder classes.
*/
class OrderBuilder {
    private Order order = new Order();

    public OrderBuilder addItem(IceCream item) {
        order.addItem(item);
        return this;
    }

    public OrderBuilder addObserver(OrderObserver observer) {
        order.addObserver(observer);
        return this;
    }
    
    public OrderBuilder setCustomer(Customer customer) {
        order.customer = customer;
        return this;
    }
    
    public OrderBuilder setUserProfile(UserProfile userProfile) {
        order.userProfile = userProfile;
        return this;
    }
    
    public OrderBuilder setSeasonalPromotion(SeasonalPromotion seasonalPromotion) {
        order.seasonalPromotion = seasonalPromotion;
        return this;
    }
    
    public Order build() {
    	order.setOrderId();
        return order;
    }
    
    public OrderBuilder setPaymentStrategy(PaymentStrategy paymentStrategy) {
        order.paymentStrategy = paymentStrategy;
        return this;
    }

    
}

// Behavioral - State Pattern starts here
/*
 Allows an object to alter its behavior when its internal state changes. The object will appear to change its class. 
*/
interface OrderState {
    void processOrder(Order order);
    OrderState nextState();
}

// Concrete states
class PlacedState implements OrderState {
	
    @Override
    public void processOrder(Order order) {
        System.out.println("Order placed.");
        order.setStatus("Placed");
    }
    
    @Override
    public OrderState nextState() {
        return new PreparationState();
    }

	@Override
	public String toString() {
		return "Placed";
	}
       
}

class PreparationState implements OrderState {
	
    @Override
    public void processOrder(Order order) {
        order.setStatus("In Preparation");
    }
    
    @Override
    public OrderState nextState() {
        return new DeliveryState();
    }
    
    @Override
	public String toString() {
		return "Preparation";
	}
}

class DeliveryState implements OrderState {
	
    @Override
    public void processOrder(Order order) {
        order.setStatus("Out for Delivery/Picked up");
    }
    
    @Override
    public OrderState nextState() {
        return new FinalState();
    }
    
    @Override
	public String toString() {
		return "Delivery";
	}
}

class FinalState implements OrderState {
	
    @Override
    public void processOrder(Order order) {
        order.setStatus("Completed");
    }
    
    @Override
    public OrderState nextState() {
        // There is no next state after the final state
        return this;
    }
    
    @Override
	public String toString() {
		return "Completed";
	}
}

// Context class
class Order {
	
	private static int nextOrderId = 1;
    private int orderId;
    private List<IceCream> items = new ArrayList<>();
    private String status;
    private List<OrderObserver> observers = new ArrayList<>();
    private String description;
    private DeliveryInformation deliveryInformation;
    
    Customer customer;
    UserProfile userProfile;
    SeasonalPromotion seasonalPromotion;
    PaymentStrategy paymentStrategy;
    OrderState currentState;
    
    private double grossTotal;
    private double loyaltyTotal;
    private double seasonalPromotionDiscount = 0.0;
    private double netTotal;
    
    public int getOrderId() {
		return orderId;
	}

	public void setOrderId() {
		this.orderId = nextOrderId++;
	}
	
	public void setState(OrderState state) {
        this.currentState = state;
    }
	
	public String getState() {
		return currentState.toString();
	}

    public void processOrder() {
        currentState.processOrder(this);
        currentState = currentState.nextState();
    }

	public void addItem(IceCream item) {
        items.add(item);
    }

    public void removeItem(IceCream item) {
        items.remove(item);
    }
    
    public List<IceCream> getItems(){
    	return items;
    }

    public double calculateTotal() {
   
    	return items.stream().mapToDouble(IceCream::cost).sum();
    }

    public void addObserver(OrderObserver observer) {
        observers.add(observer);
    }

    public void removeObserver(OrderObserver observer) {
        observers.remove(observer);
    }
    
    public void notifyObservers() {
        for (OrderObserver observer : observers) {
            observer.update(this);
        }
    }

    public void setStatus(String status) {
        this.status = status;
        notifyObservers();
    }

    public String getStatus() {
        return status;
    }

    public String getDescription() {
        return description;
    }

    public String printOrder() {
        StringBuilder orderDetails = new StringBuilder();
        orderDetails.append("Order Details:\n");
        int itemNumber = 1;

        for (IceCream item : items) {
        	orderDetails.append(itemNumber).append(". ").append(item.getName()).append(" /LKR ").append(item.cost()).append("\n");
            itemNumber++;
        }
        
        grossTotal = calculateTotal();
        loyaltyTotal = calculateTotalWithDiscount();
        seasonalPromotionDiscount = grossTotal * seasonalPromotion.getDiscountPercentage();
        netTotal = loyaltyTotal - seasonalPromotionDiscount;
        
        orderDetails.append("Gross Total: LKR ").append(grossTotal).append("\n");
        orderDetails.append("Loyalty Points Available: ").append(customer.getLoyaltyPoints()).append("\n");
        orderDetails.append("Total with Loyalty Discount: LKR ").append(loyaltyTotal).append("\n");
        orderDetails.append("Seasonal Promotions: ").append((seasonalPromotion.isApplicable() ? "Yes" : "No")).append("\n");
        orderDetails.append("Seasonal Promotions LKR: ").append(seasonalPromotionDiscount).append("\n");
        orderDetails.append("Net Total : LKR ").append(netTotal).append("\n");
        orderDetails.append("Status: ").append(getState()).append("\n");
        orderDetails.append("Payment Type: ").append(paymentStrategy.pay(netTotal)).append("\n");

        customer.resetLoyaltyPoints();
        
        return orderDetails.toString();
    }
    
    public void setDeliveryInformation(String deliveryAddress) {
        this.deliveryInformation = new DeliveryInformation(deliveryAddress, DeliveryStatus.DELIVERY_PENDING);
    }

    public DeliveryInformation getDeliveryInformation() {
        return deliveryInformation;
    }

    public void setPickupInformation() {
        this.deliveryInformation = new DeliveryInformation("In-store Pickup", DeliveryStatus.PICKUP);
    }
    
    public double calculateTotalWithDiscount() {
    	double total = calculateTotal();
    	if(userProfile.getOrderHistory().size() > 1) {
    		
            int loyaltyPoints = customer.getLoyaltyPoints();

            // Calculate discount based on loyalty points
            int discountAmount = loyaltyPoints / 10;
            double discount = discountAmount * 100.0;

            // Apply discount
            total -= discount; 
    	}
    	return total;
    }
}

// Ice cream interface
interface IceCream {
	
    String getDescription();
    double cost();
    
    public String getFlavor();
	public String getBaseType();
	public List<String> getToppings();
	public List<String> getSyrups();
	public List<String> getExtras();
	
	public void setFlavor(String flavor);
	public void addTopping(String topping);
	public void pourSyrup(String syrup);
	public void addExtra(String extra);
	
	public String getName();
	public void setName(String name);
}

// An abstract class with an additional method setBaseType()
abstract class AbstractIceCream implements IceCream {
	public abstract void setBaseType(String baseType); // Cone, Stick, Cup, Sundae
}

//Extends AbstractIceCream to use an additional method setBaseType()
class ScoopIceCream extends AbstractIceCream {

	private String name;
	private double cost = 250.0;
	private String baseType;
	private String flavor;
	private List<String> toppings = new ArrayList<>();
	private List<String> syrups = new ArrayList<>();
	private List<String> extras = new ArrayList<>();
	
	@Override
	public String getDescription() {
		return "Your regular ice cream.";
	}

	@Override
	public double cost() { // Get the cost here
		return cost;
	}

	@Override
	public String getFlavor() {
		return flavor;
	}

	@Override
	public String getBaseType() {
		return baseType;
	}

	@Override
	public List<String> getToppings() {
		return toppings;
	}

	@Override
	public List<String> getSyrups() {
		return syrups;
	}

	@Override
	public List<String> getExtras() {
		return extras;
	}

	@Override
	public void setFlavor(String flavor) {
		this.flavor = flavor;
	}

	@Override
	public void addTopping(String topping) {
		toppings.add(topping);
		cost = cost + 100.0;
	}

	@Override
	public void pourSyrup(String syrup) {
		syrups.add(syrup);
		cost = cost + 50.0;
	}

	@Override
	public void addExtra(String extra) {
		extras.add(extra);
		cost = cost + 40.0;
	}

	@Override
	public void setBaseType(String baseType) {
		this.baseType = baseType;
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public void setName(String name) {
		this.name = name;		
	}
    
}    

//Concrete ice cream implementation
class RolledIceCream implements IceCream {
	
	private String name;
	private double cost = 900.0;
	private String flavor;
	private List<String> toppings = new ArrayList<>();
	private List<String> syrups = new ArrayList<>();
	private List<String> extras = new ArrayList<>();

	@Override
	public String getDescription() {
		return "Rolled Ice Cream";
	}

	@Override
	public double cost() { // Get the cost here
		return cost;
	}

	@Override
	public String getFlavor() {
		return flavor;
	}

	@Override
	public List<String> getToppings() {
		return toppings;
	}

	@Override
	public void setFlavor(String flavor) {
		this.flavor = flavor;
	}

	@Override
	public void addTopping(String topping) {
		toppings.add(topping);
		cost = cost + 100.0;
	}

	@Override
	public void pourSyrup(String syrup) {
		syrups.add(syrup);
		cost = cost + 50.0;
	}

	@Override
	public void addExtra(String extra) {
		extras.add(extra);
		cost = cost + 40.0;
	}

	@Override
	public String getBaseType() {
		return "Cup";
	}

	@Override
	public List<String> getSyrups() {
		return syrups;
	}

	@Override
	public List<String> getExtras() {
		return extras;
	}
	
	@Override
	public String getName() {
		return name;
	}

	@Override
	public void setName(String name) {
		this.name = name;		
	}
}    

// Extends AbstractIceCream to use an additional method setBaseType()
class Gelato extends AbstractIceCream {
	
	private String name;
	private double cost = 400.0;
	private String baseType;
	private String flavor;
	private List<String> toppings = new ArrayList<>();
	private List<String> syrups = new ArrayList<>();
	private List<String> extras = new ArrayList<>();

	@Override
	public String getDescription() {
		return "Gelato - No cream and no eggs. Gelato is available in a wide range of flavors, but some popular picks are cream, cioccolato (chocolate), stracciatella (vanilla with chocolate flecks) and pistacchio (pistachio).";
	}

	@Override
	public double cost() { // Get the cost here
		return cost;
	}

	@Override
	public String getFlavor() {
		return flavor;
	}

	@Override
	public List<String> getToppings() {
		return toppings;
	}

	@Override
	public void setFlavor(String flavor) {
		this.flavor = flavor;
	}

	@Override
	public void addTopping(String topping) {
		toppings.add(topping);
		cost = cost + 100.0;
	}

	@Override
	public void pourSyrup(String syrup) {
		syrups.add(syrup);
		cost = cost + 50.0;
	}

	@Override
	public void addExtra(String extra) {
		extras.add(extra);
		cost = cost + 40.0;
	}

	@Override
	public String getBaseType() {
		return baseType;
	}

	@Override
	public List<String> getSyrups() {
		return syrups;
	}

	@Override
	public List<String> getExtras() {
		return extras;
	}

	@Override
	public void setBaseType(String baseType) {
		this.baseType = baseType;
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public void setName(String name) {
		this.name = name;		
	}
}    

//Concrete ice cream implementation
class Kulfi implements IceCream {
	
	private String name;
	private double cost = 550.0;
	private String flavor;
	private List<String> toppings = new ArrayList<>();
	private List<String> syrups = new ArrayList<>();
	private List<String> extras = new ArrayList<>();

	@Override
	public String getDescription() {
		return "Kulfi - Traditional Indian Ice Cream. Denser and creamier than your regular ice cream.";
	}

	@Override
	public double cost() { // Get the cost here
		return cost;
	}

	@Override
	public String getFlavor() {
		return flavor;
	}

	@Override
	public List<String> getToppings() {
		return toppings;
	}

	@Override
	public void setFlavor(String flavor) {
		this.flavor = flavor;
	}

	@Override
	public void addTopping(String topping) {
		toppings.add(topping);
		cost = cost + 100.0;
	}

	@Override
	public void pourSyrup(String syrup) {
		syrups.add(syrup);
		cost = cost + 50.0;
	}

	@Override
	public void addExtra(String extra) {
		extras.add(extra);
		cost = cost + 40.0;
	}

	@Override
	public String getBaseType() {
		return "Stick";
	}

	@Override
	public List<String> getSyrups() {
		return syrups;
	}

	@Override
	public List<String> getExtras() {
		return extras;
	}
	
	@Override
	public String getName() {
		return name;
	}

	@Override
	public void setName(String name) {
		this.name = name;		
	}
}    

//Concrete ice cream implementation
class FrozenYogurt implements IceCream {
	
	private String name;
	private double cost = 350.0;
	private String flavor;
	private List<String> toppings = new ArrayList<>();
	private List<String> syrups = new ArrayList<>();
	private List<String> extras = new ArrayList<>();

	@Override
	public String getDescription() {
		return "Frozen Yogurt - Your low-calorie dessert.";
	}

	@Override
	public double cost() { // Get the cost here
		return cost;
	}

	@Override
	public String getFlavor() {
		return flavor;
	}

	@Override
	public List<String> getToppings() {
		return toppings;
	}

	@Override
	public void setFlavor(String flavor) {
		this.flavor = flavor;
	}

	@Override
	public void addTopping(String topping) {
		toppings.add(topping);
		cost = cost + 100.0;
	}

	@Override
	public void pourSyrup(String syrup) {
		syrups.add(syrup);
		cost = cost + 50.0;
	}

	@Override
	public void addExtra(String extra) {
		extras.add(extra);
		cost = cost + 40.0;
	}

	@Override
	public String getBaseType() {
		return "Cup";
	}

	@Override
	public List<String> getSyrups() {
		return syrups;
	}

	@Override
	public List<String> getExtras() {
		return extras;
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public void setName(String name) {
		this.name = name;		
	}
}    

//Concrete ice cream implementation
class Mochi implements IceCream {
	
	private String name;
	private double cost = 800.0;
	private String flavor;
	private List<String> toppings = new ArrayList<>();
	private List<String> syrups = new ArrayList<>();
	private List<String> extras = new ArrayList<>();

	@Override
	public String getDescription() {
		return "Mochi - Japan Tradition. More like an ice cream sandwich, but with a satisfyingly chewy texture.";
	}

	@Override
	public double cost() { // Get the cost here
		return cost;
	}

	@Override
	public String getFlavor() {
		return flavor;
	}

	@Override
	public List<String> getToppings() {
		return toppings;
	}

	@Override
	public void setFlavor(String flavor) {
		this.flavor = flavor;
	}

	@Override
	public void addTopping(String topping) {
		toppings.add(topping);
		cost = cost + 100.0;
	}

	@Override
	public void pourSyrup(String syrup) {
		syrups.add(syrup);
		cost = cost + 50.0;
	}

	@Override
	public void addExtra(String extra) {
		extras.add(extra);
		cost = cost + 40.0;
	}

	@Override
	public String getBaseType() {
		return "Sandwich";
	}

	@Override
	public List<String> getSyrups() {
		return syrups;
	}

	@Override
	public List<String> getExtras() {
		return extras;
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public void setName(String name) {
		this.name = name;		
	}
}

class Customer {
	
    private String name;
    private int loyaltyPoints;

    public Customer(String name) {
        this.name = name;
        this.loyaltyPoints = 0;
    }
    
    public String getName() {
		return name;
	}

	public int getLoyaltyPoints() {
        return loyaltyPoints;
    }

    public void earnLoyaltyPoints(int points) {
        this.loyaltyPoints += points;
    }

    public void resetLoyaltyPoints() {
        this.loyaltyPoints = 0;
    }
}

class UserProfile {
	
	private String username;
    private List<Order> orderHistory;
    private Map<String, IceCream> favoriteIceCreams;

    public UserProfile(String username) {
    	this.username = username;
        this.orderHistory = new ArrayList<>();
        this.favoriteIceCreams = new HashMap<>();
    }

    public void addFavoriteIceCream(String name, IceCream iceCream) {
        favoriteIceCreams.put(name, iceCream);
    }

    public IceCream getFavoriteIceCream(String name) {
        return favoriteIceCreams.get(name);
    }
    
    public String getUsername() {
        return username;
    }

    public void addOrderToHistory(Order order) {
        orderHistory.add(order);
    }

    public List<Order> getOrderHistory() {
        return orderHistory;
    }
}

//Enum to represent delivery status
enum DeliveryStatus {
	PICKUP, DELIVERY_PENDING, OUT_FOR_DELIVERY, DELIVERED
}

// DeliveryInformation class to store delivery details
class DeliveryInformation {
	private String deliveryAddress;
	private DeliveryStatus deliveryStatus;

	public DeliveryInformation(String deliveryAddress, DeliveryStatus deliveryStatus) {
		this.deliveryAddress = deliveryAddress;
		this.deliveryStatus = deliveryStatus;
	}

	public String getDeliveryAddress() {
		return deliveryAddress;
	}

	public DeliveryStatus getDeliveryStatus() {
		return deliveryStatus;
	}

	public void setDeliveryStatus(DeliveryStatus deliveryStatus) {
		this.deliveryStatus = deliveryStatus;
	}
}

interface Promotion {
    boolean isApplicable();
}

class SeasonalPromotion implements Promotion {
	
	private double discountPercentage;
    private LocalDate startDate;
    private LocalDate endDate;

    public SeasonalPromotion(double discountPercentage, LocalDate startDate, LocalDate endDate) {
		super();
		this.discountPercentage = discountPercentage;
		this.startDate = startDate;
		this.endDate = endDate;
	}

	@Override
    public boolean isApplicable() {
        LocalDate currentDate = LocalDate.now();
        return currentDate.isAfter(startDate) && currentDate.isBefore(endDate);
    }

	public double getDiscountPercentage() {
		return discountPercentage;
	}

	public void setDiscountPercentage(double discountPercentage) {
		this.discountPercentage = discountPercentage;
	}
    
}

