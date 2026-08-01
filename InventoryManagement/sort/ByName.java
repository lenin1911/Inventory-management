package InventoryManagement.sort;

import java.util.Comparator;
import java.util.List;
import InventoryManagement.Product;

public class ByName implements SortStrategy {
    @Override
    public void sort(List<Product> products) {
        products.sort(Comparator.comparing(Product::getName, String.CASE_INSENSITIVE_ORDER));
    }
}
