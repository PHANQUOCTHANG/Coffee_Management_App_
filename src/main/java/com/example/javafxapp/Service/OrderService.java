package com.example.javafxapp.Service;

import java.util.List;

import com.example.javafxapp.Model.Order;
import com.example.javafxapp.Repository.OrderRepository;

public class OrderService {
    private OrderRepository or = new OrderRepository();
    
    public List<Order> getAllOrder(){
        return or.getAll();
    }

    public void addOrder(Order order){
        or.add(order);
    }

    public void deleteOrder(int id){
        or.delete(id);
    }

    public void updateOrder(Order order){
        or.update(order);
    }
}
