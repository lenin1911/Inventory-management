package InventoryManagement;

import InventoryManagement.exceptions.InsufficientStockException;
import InventoryManagement.exceptions.ProductNotFoundException;
import InventoryManagement.sort.SortStrategy;

import java.io.*;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Service class handling inventory logic.
 * Uses a primary HashMap for O(1) ID-based access and a secondary Map for O(1) name-based search.
 */
public class InventoryService {
    // Primary store: ID -> Product (O(1) CRUD)
    private final Map<Integer, Product> products = new HashMap<>();
    
    // Secondary index: lowercase name -> ID (O(1) search by name)
    private final Map<String, Integer> nameToIndex = new HashMap<>();
    
    private final String csvFilePath = "inventory.csv";

    public InventoryService() {
        loadFromCsv();
    }

    public void addProduct(Product product) {
        products.put(product.getId(), product);
        nameToIndex.put(product.getName().toLowerCase(), product.getId());
    }

    public Product getProduct(int id) throws ProductNotFoundException {
        Product p = products.get(id);
        if (p == null) {
            throw new ProductNotFoundException("Product with ID " + id + " not found.");
        }
        return p;
    }

    public Product getProductByName(String name) throws ProductNotFoundException {
        Integer id = nameToIndex.get(name.toLowerCase());
        if (id == null) {
            throw new ProductNotFoundException("Product with name '" + name + "' not found.");
        }
        return getProduct(id);
    }

    public void deleteProduct(int id) throws ProductNotFoundException {
        Product p = getProduct(id);
        nameToIndex.remove(p.getName().toLowerCase());
        products.remove(id);
    }

    public void updateQuantity(int id, int newQty) throws ProductNotFoundException {
        Product p = getProduct(id);
        p.setQuantity(newQty);
        checkLowStock(p);
    }

    public void sellProduct(int id, int qty) throws ProductNotFoundException, InsufficientStockException {
        Product p = getProduct(id);
        if (p.getQuantity() < qty) {
            throw new InsufficientStockException("Insufficient stock for " + p.getName() + 
                ". Available: " + p.getQuantity() + ", Requested: " + qty);
        }
        p.setQuantity(p.getQuantity() - qty);
        checkLowStock(p);
    }

    public void restockProduct(int id, int qty) throws ProductNotFoundException {
        Product p = getProduct(id);
        p.setQuantity(p.getQuantity() + qty);
        checkLowStock(p);
    }

    private void checkLowStock(Product p) {
        if (p.getQuantity() <= p.getReorderThreshold()) {
            System.out.println("[ALERT] Low stock for: " + p.getName() + 
                " (Current: " + p.getQuantity() + ", Threshold: " + p.getReorderThreshold() + ")");
        }
    }

    public List<Product> getLowStockProducts() {
        return products.values().stream()
                .filter(p -> p.getQuantity() <= p.getReorderThreshold())
                .collect(Collectors.toList());
    }

    public List<Product> getAllProducts() {
        return new ArrayList<>(products.values());
    }

    public void displaySorted(SortStrategy strategy) {
        List<Product> list = getAllProducts();
        strategy.sort(list);
        if (list.isEmpty()) {
            System.out.println("Inventory is empty.");
        } else {
            list.forEach(System.out::println);
        }
    }

    // Persistence logic
    public void saveToCsv() {
        try (PrintWriter writer = new PrintWriter(new FileWriter(csvFilePath))) {
            for (Product p : products.values()) {
                writer.println(p.toCsv());
            }
        } catch (IOException e) {
            System.err.println("Error saving to CSV: " + e.getMessage());
        }
    }

    private void loadFromCsv() {
        File file = new File(csvFilePath);
        if (!file.exists()) return;

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                Product p = Product.fromCsv(line);
                if (p != null) {
                    addProduct(p);
                }
            }
        } catch (IOException e) {
            System.err.println("Error loading from CSV: " + e.getMessage());
        }
    }
}
