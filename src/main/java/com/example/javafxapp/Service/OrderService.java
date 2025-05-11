package com.example.javafxapp.Service;

import java.math.BigDecimal;
import java.util.List;

import com.example.javafxapp.Model.Order;
import com.example.javafxapp.Repository.OrderRepository;

public class OrderService {
    private OrderRepository or = new OrderRepository();
    
    public List<Order> getAllOrder(){
        return or.getAll();
    }

    public Order getOrderById(int id){
        return or.getOrderById(id);
    }

    // tra ve orderId
    public int addOrder(int userId, BigDecimal totalAmount){
        return or.add(userId, totalAmount);
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

    public void updateOrder(int OrderDetailId, BigDecimal val){
        or.updateOrder(OrderDetailId, val);
    }

    public void updateStatus(int orderId, String status){
        or.updateStatus(orderId, status);
    }
}
