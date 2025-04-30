package com.example.javafxapp.Service;

import com.example.javafxapp.Config.DatabaseConnection;
import com.example.javafxapp.Model.Cart_Product;
import com.example.javafxapp.Repository.Cart_ProductRepository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class Cart_ProductService {
    private Cart_ProductRepository cartProductRepository = new Cart_ProductRepository() ;
    public void add (Cart_Product cartProduct) {
        cartProductRepository.add(cartProduct);
    }
    public void update(Cart_Product cartProduct) {
        cartProductRepository.update(cartProduct);
    }

    // delete .
    public void delete(Cart_Product cartProduct) {
        cartProductRepository.delete(cartProduct);
    }

    // get all .
    public List<Cart_Product> getAll(int cardId){
       return cartProductRepository.getAll(cardId) ;
    }
}
