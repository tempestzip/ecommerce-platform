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
        return productDAO.searchProductsByName(keyword);
    }

    public List<Product> getProductsByCategory(String category) {
        return productDAO.getProductsByCategory(category);
    }

    public boolean adjustStock(int productId, int amount) {
        return productDAO.adjustStock(productId, amount);
    }
}
