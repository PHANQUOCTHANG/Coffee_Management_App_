package com.example.javafxapp.Service;

import com.example.javafxapp.Config.DatabaseConnection;
import com.example.javafxapp.Model.OrderUser_Product;
import com.example.javafxapp.Repository.OrderUser_ProductRepository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class OrderUser_ProductService {
    private OrderUser_ProductRepository orderUserProductRepository = new OrderUser_ProductRepository() ;
    // add OrderUser_Product .
    public void add (OrderUser_Product orderUserProduct) {
        orderUserProductRepository.add(orderUserProduct);
    }

    // get all product by orderUser_id
    public List<OrderUser_Product> getAllByOrderUserId(int orderUserId) {
    return orderUserProductRepository.getAllByOrderUserId(orderUserId) ;
    }
}
