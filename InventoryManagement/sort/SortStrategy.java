package InventoryManagement.sort;

import java.util.List;
import InventoryManagement.Product;

/**
 * Strategy interface for sorting products.
 */
public interface SortStrategy {
    void sort(List<Product> products);
}
