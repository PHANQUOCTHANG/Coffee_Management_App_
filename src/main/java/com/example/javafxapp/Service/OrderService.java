package com.example.javafxapp.Service;

import java.math.BigDecimal;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import com.example.javafxapp.Config.DatabaseConnection;
import com.example.javafxapp.Model.Order;
import com.example.javafxapp.Repository.OrderRepository;

public class OrderService {
    private OrderRepository or = new OrderRepository();
    
    public List<Order> getAllOrder(){
        return or.getAll();
    }

    // tra ve orderId
    public int addOrder(int userId, BigDecimal totalAmount , double discount , String member_phone){
        return or.add(userId, totalAmount , discount, member_phone);
    }

    public void deleteOrder(int id){
        or.delete(id);
    }

    public void updateOrder(BigDecimal totalAmount, String status, int id  , double discount , String member_phone){
        or.update(totalAmount, status, id , discount, member_phone);
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
    public List<Order> getOrdersByDateRange(LocalDate from, LocalDate to) {
        return or.getOrdersByDateRange(from , to) ;
    }

    public Order findOrderById(int id){
        return or.findOrderById(id);
    }

    public List<Order> getOrder(String roleCheckStr, String roleStr, String fromPriceStr, String toPriceStr, String statusCheckStr, List<String> selectedStatuses, LocalDate fromDateStr, LocalDate toDateStr) {
        return or.getOrder(roleCheckStr, roleStr, fromPriceStr, toPriceStr, statusCheckStr, selectedStatuses, fromDateStr, toDateStr);
    }

    public boolean updateOrderPayment(int orderId, String paymentMethod) {
        return or.updateOrderPayment(orderId, paymentMethod);
    }
}
