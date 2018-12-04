package Lab5;

public class GroceryItem {
    protected String name;
    protected double price;
    protected int count;

    /**
     * creates a string to output with the name, price and count
     * @return a String with the the name, price and item count
     */
    @Override
    public String toString() {
        return name +
                ", $" + price +
                ", count=" + count +
                ", total cost $" + String.format("%.2f",amount());
    }

    /**
     * constructor with that initializes the variables of GroceryItem
     * @param name the name of the grocery
     * @param price the price of the item
     * @param count the amount of the item needed
     */
    public GroceryItem(String name, double price, int count) {
        this.name = name;
        this.price = price;
        this.count = count;
    }

    public double amount(){
        return price*count;
    }
}
