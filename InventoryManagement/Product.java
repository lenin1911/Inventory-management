package InventoryManagement;

/**
 * Represents a product in the inventory.
 */
public class Product {
    private int id;
    private String name;
    private double price;
    private int quantity;
    private int reorderThreshold;

    public Product(int id, String name, double price, int quantity) {
        this(id, name, price, quantity, 10);
    }

    public Product(int id, String name, double price, int quantity, int reorderThreshold) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.quantity = quantity;
        this.reorderThreshold = reorderThreshold;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public double getPrice() { return price; }
    public void setPrice(double price) { this.price = price; }

    public int getQuantity() { return quantity; }
    public void setQuantity(int quantity) { this.quantity = quantity; }

    public int getReorderThreshold() { return reorderThreshold; }
    public void setReorderThreshold(int reorderThreshold) { this.reorderThreshold = reorderThreshold; }

    @Override
    public String toString() {
        return String.format("ID: %d | Name: %-15s | Price: $%-8.2f | Qty: %-4d | Threshold: %d", 
                id, name, price, quantity, reorderThreshold);
    }

    public String toCsv() {
        return String.format("%d,%s,%.2f,%d,%d", id, name, price, quantity, reorderThreshold);
    }

    public static Product fromCsv(String csv) {
        String[] parts = csv.split(",");
        if (parts.length < 5) return null;
        try {
            return new Product(
                Integer.parseInt(parts[0]),
                parts[1],
                Double.parseDouble(parts[2]),
                Integer.parseInt(parts[3]),
                Integer.parseInt(parts[4])
            );
        } catch (NumberFormatException e) {
            return null;
        }
    }
}
