package com.example.javafxapp.Service;

import com.example.javafxapp.Config.DatabaseConnection;
import com.example.javafxapp.Model.Product;
import com.example.javafxapp.Repository.ProductRepository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ProductService {

    private ProductRepository productRepository = new ProductRepository() ;
    // add product .
    public void addProduct(Product product) {
        productRepository.add(product);
    }

    // update product .
    public void updateProduct(Product product) {
        productRepository.update(product);
    }

    // delete product
    public void deleteProduct(int productId) {
        productRepository.delete(productId);
    }

    // get all product .
    public List<Product> getAllProduct(){
        return productRepository.getAll() ;
    }

    // find product by product_name .
    public Product findProductByName(String product_name) {
        return productRepository.findByName(product_name) ;
    }

    // find product by product_name .
    public Product findProductByID(int productId) {
        return productRepository.findByID(productId) ;
    }

    // get all product bu category_id.
    public List<Product> getAllByCategoryId(int category_id){
        return productRepository.getAllByCategoryId(category_id) ;
    }

    // get all product is outstanding .
    public List<Product> getAllIsOutStanding(){
       return productRepository.getAllIsOutStanding() ;
    }

    // change status
    public void changeStatus(int productId , boolean status) {
       productRepository.changeStatus(productId,status);
    }

    // change status
    public void changeOutStanding(int productId , boolean outstanding) {
        productRepository.changeOutStanding(productId,outstanding);
    }

    // find all product by keyword .
    public List<Product> findProductsByKeyword(String keyword) {
        return productRepository.findAllByKeyword(keyword) ;
    }
}
