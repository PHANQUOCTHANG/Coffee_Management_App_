package com.example.javafxapp.Service.User;

import java.math.BigDecimal;
import java.util.List;

import com.example.javafxapp.Model.Order;
import com.example.javafxapp.Repository.User.OrderRepository;

public class OrderService {
    private OrderRepository or = new OrderRepository();
    
    public List<Order> getAllOrder(){
        return or.getAll();
    }

    public void addOrder(int userId, BigDecimal totalAmount){
        or.add(userId, totalAmount);
    }

    public void deleteOrder(int id){
        or.delete(id);
    }

    public void updateOrder(BigDecimal totalAmount, String status, int id){
        or.update(totalAmount, status, id);
    }

    public List<Order> getOrderByStatus(String s) {
        return or.getOrderByStatus(s);
    }
}
