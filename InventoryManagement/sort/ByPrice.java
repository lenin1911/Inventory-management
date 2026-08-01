package InventoryManagement.sort;

import java.util.Comparator;
import java.util.List;
import InventoryManagement.Product;

public class ByPrice implements SortStrategy {
    @Override
    public void sort(List<Product> products) {
        products.sort(Comparator.comparingDouble(Product::getPrice));
    }
}
