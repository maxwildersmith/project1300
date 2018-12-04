package Lab5;

public class GroceryItemWithStore extends GroceryItem {
    String store;
    /**
     * constructor with that initializes the variables of GroceryItem
     *
     * @param name  the name of the grocery
     * @param price the price of the item
     * @param count the amount of the item needed
     * @param store the store for the grocery item
     */
    public GroceryItemWithStore(String name, double price, int count, String store) {
        super(name, price, count);
        this.store = store;
    }

    @Override
    public String toString() {
        return super.toString() +
                ", "+store;
    }
}
