package Lab5;

public class WilderSmithMaximum2 {

    /**
     * Runs and creates 5 grocery ItemWithStores
     * @param args
     */
    public static void main(String[] args) {
        GroceryItemWithStore g1 = new GroceryItemWithStore("Bananas",.99,22, "Costco");
        GroceryItemWithStore g2 = new GroceryItemWithStore("Oranges",2.99,4, "Times");
        GroceryItemWithStore g3 = new GroceryItemWithStore("Garlic",3.99,8, "Safeway");
        GroceryItemWithStore g4 = new GroceryItemWithStore("Grapes",9.99,2, "KTA Super Stores");
        GroceryItemWithStore g5 = new GroceryItemWithStore("Papayas",4.99,6, "Farmer's market");

        System.out.println(g1);
        System.out.println(g2);
        System.out.println(g3);
        System.out.println(g4);
        System.out.println(g5);

        double total = g1.amount()+g2.amount()+g3.amount()+g4.amount()+g5.amount();
        System.out.println("TOTAL: $"+String.format("%.02f",total));
    }
}
