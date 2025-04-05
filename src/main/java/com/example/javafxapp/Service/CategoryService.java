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
    public void add(Category category) {
        categoryRepository.add(category);
    }

    // update product .
    public void update(Category category) {
        categoryRepository.update(category);
    }

    // delete product
    public void delete(int categoryId) {
        categoryRepository.delete(categoryId);
    }

    // get all product .
    public List<Category> getAll(){
        return categoryRepository.getAll() ;
    }

    // find product by product_name .
    public Category findCategoryByID(int category_id) {
        return categoryRepository.findCategoryByID(category_id) ;
    }

    // find category by product_name .
    public Category findCategoryByName(String category_name) {
        return categoryRepository.findCategoryByName(category_name) ;
    }
}
