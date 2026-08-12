package com.software_engineers.service;

import java.util.List;

import com.software_engineers.database.ProductDAO;
import com.software_engineers.model.Product;

/**
 * Product Service, handles application layer logic related to a given products,
 *
 * @author Rolando Juarez
 * @version 1.0
 */
public class ProductService {
    ProductDAO productDAO;

    public ProductService() {
        productDAO = new ProductDAO();
    }

    public Product getProductById(int id) {
        return productDAO.getProductById(id);
    }

    public List<Product> getAllProducts() {
        return productDAO.getAllProducts();
    }

    public List<Product> searchProductsByName(String keyword) {
        if (keyword.equals(null) || keyword.trim().isEmpty()) {
            return getAllProducts();
        }

        return productDAO.searchProductsByName(keyword);
    }

    // @formatter:off
    // maybe unused
    // public List<Product> getProductsByCategory(String category) {
    //     return productDAO.getProductsByCategory(category);
    // }
    // @formatter:on

    public boolean adjustStock(int productId, int amount) {
        Product product = productDAO.getProductById(productId);
        if (product == null) {
            throw new RuntimeException("Product with id: " + productId + " does not exist");
        }

        if (product.getStock() - amount < 0) {
            throw new RuntimeException("Adjusting the stock of product with id: " + productId + " by " + amount
                    + " would result in a negative stock of this product");
        }

        return productDAO.adjustStock(productId, amount);
    }
}
