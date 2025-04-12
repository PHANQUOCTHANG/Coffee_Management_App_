package com.example.javafxapp.Service;

import com.example.javafxapp.Config.DatabaseConnection;
import com.example.javafxapp.Model.Category;
import com.example.javafxapp.Model.Product;
import com.example.javafxapp.Repository.CategoryRepository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CategoryService {

    private CategoryRepository categoryRepository = new CategoryRepository() ;
    // add category .
    public void addCategory(Category category) {
        categoryRepository.add(category);
    }

    // update product .
    public void updateCategory(Category category) {
        categoryRepository.update(category);
    }

    // delete product
    public void deleteCategory(int categoryId) {
        categoryRepository.delete(categoryId);
    }

    // get all product .
    public List<Category> getAllCategory(){
        return categoryRepository.getAll() ;
    }

    // find product by product_name .
    public Category findCategoryByID(int category_id) {
        return categoryRepository.findByID(category_id) ;
    }

    // find category by product_name .
    public Category findCategoryByName(String category_name) {
        return categoryRepository.findByName(category_name) ;
    }
}
