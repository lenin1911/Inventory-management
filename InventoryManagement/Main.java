package InventoryManagement;

import InventoryManagement.exceptions.InsufficientStockException;
import InventoryManagement.exceptions.ProductNotFoundException;
import InventoryManagement.sort.*;

import java.util.Scanner;

/**
 * Entry point for the Inventory Management System.
 * Handles user interaction via a menu-driven console interface.
 */
public class Main {
    private static final InventoryService service = new InventoryService();
    private static final Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        boolean running = true;
        System.out.println("=== Inventory Management System ===");

        while (running) {
            printMenu();
            String choice = scanner.nextLine().trim();

            try {
                switch (choice) {
                    case "1": addProduct(); break;
                    case "2": searchProduct(); break;
                    case "3": deleteProduct(); break;
                    case "4": updateQuantity(); break;
                    case "5": sellProduct(); break;
                    case "6": restockProduct(); break;
                    case "7": displayAll(); break;
                    case "8": displaySorted(); break;
                    case "9": checkLowStock(); break;
                    case "10":
                        service.saveToCsv();
                        System.out.println("Inventory saved. Exiting...");
                        running = false;
                        break;
                    default:
                        System.out.println("Invalid option. Please try again.");
                }
            } catch (Exception e) {
                // Graceful error handling as requested
                System.out.println("Error: " + e.getMessage());
            }
        }
    }

    private static void printMenu() {
        System.out.println("\n1. Add Product");
        System.out.println("2. Search Product");
        System.out.println("3. Delete Product");
        System.out.println("4. Update Quantity");
        System.out.println("5. Sell Product");
        System.out.println("6. Restock Product");
        System.out.println("7. Display All");
        System.out.println("8. Display Sorted");
        System.out.println("9. Check Low Stock");
        System.out.println("10. Exit");
        System.out.print("Select an option: ");
    }

    private static void addProduct() {
        System.out.print("Enter ID: ");
        int id = Integer.parseInt(scanner.nextLine());
        System.out.print("Enter Name: ");
        String name = scanner.nextLine();
        System.out.print("Enter Price: ");
        double price = Double.parseDouble(scanner.nextLine());
        System.out.print("Enter Quantity: ");
        int qty = Integer.parseInt(scanner.nextLine());
        System.out.print("Enter Reorder Threshold (Enter for default 10): ");
        String thresholdStr = scanner.nextLine();
        
        Product p;
        if (thresholdStr.isEmpty()) {
            p = new Product(id, name, price, qty);
        } else {
            p = new Product(id, name, price, qty, Integer.parseInt(thresholdStr));
        }
        service.addProduct(p);
        System.out.println("Product added successfully.");
    }

    private static void searchProduct() throws ProductNotFoundException {
        System.out.print("Search by (1) ID or (2) Name? ");
        String type = scanner.nextLine();
        if (type.equals("1")) {
            System.out.print("Enter ID: ");
            int id = Integer.parseInt(scanner.nextLine());
            System.out.println(service.getProduct(id));
        } else {
            System.out.print("Enter Name: ");
            String name = scanner.nextLine();
            System.out.println(service.getProductByName(name));
        }
    }

    private static void deleteProduct() throws ProductNotFoundException {
        System.out.print("Enter ID to delete: ");
        int id = Integer.parseInt(scanner.nextLine());
        service.deleteProduct(id);
        System.out.println("Product deleted.");
    }

    private static void updateQuantity() throws ProductNotFoundException {
        System.out.print("Enter ID: ");
        int id = Integer.parseInt(scanner.nextLine());
        System.out.print("Enter New Quantity: ");
        int qty = Integer.parseInt(scanner.nextLine());
        service.updateQuantity(id, qty);
        System.out.println("Quantity updated.");
    }

    private static void sellProduct() throws ProductNotFoundException, InsufficientStockException {
        System.out.print("Enter ID: ");
        int id = Integer.parseInt(scanner.nextLine());
        System.out.print("Enter Quantity to Sell: ");
        int qty = Integer.parseInt(scanner.nextLine());
        service.sellProduct(id, qty);
        System.out.println("Sale successful.");
    }

    private static void restockProduct() throws ProductNotFoundException {
        System.out.print("Enter ID: ");
        int id = Integer.parseInt(scanner.nextLine());
        System.out.print("Enter Quantity to Restock: ");
        int qty = Integer.parseInt(scanner.nextLine());
        service.restockProduct(id, qty);
        System.out.println("Restock successful.");
    }

    private static void displayAll() {
        service.getAllProducts().forEach(System.out::println);
    }

    private static void displaySorted() {
        System.out.println("Sort by: (1) Price (2) Quantity (3) Name");
        String choice = scanner.nextLine();
        SortStrategy strategy;
        switch (choice) {
            case "1": strategy = new ByPrice(); break;
            case "2": strategy = new ByQuantity(); break;
            case "3": strategy = new ByName(); break;
            default:
                System.out.println("Invalid choice.");
                return;
        }
        service.displaySorted(strategy);
    }

    private static void checkLowStock() {
        var lowStock = service.getLowStockProducts();
        if (lowStock.isEmpty()) {
            System.out.println("No products are below the reorder threshold.");
        } else {
            System.out.println("Low Stock Products:");
            lowStock.forEach(System.out::println);
        }
    }
}
