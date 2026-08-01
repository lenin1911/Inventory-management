package InventoryManagement.sort;

import java.util.Comparator;
import java.util.List;
import InventoryManagement.Product;

public class ByQuantity implements SortStrategy {
    @Override
    public void sort(List<Product> products) {
        products.sort(Comparator.comparingInt(Product::getQuantity));
    }
}
