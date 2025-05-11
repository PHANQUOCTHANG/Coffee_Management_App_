package com.example.javafxapp.Service;

import com.example.javafxapp.Config.DatabaseConnection;
import com.example.javafxapp.Model.OrderUser;
import com.example.javafxapp.Repository.OrderUserRepository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class OrderUserService {
    private OrderUserRepository orderUserRepository = new OrderUserRepository() ;
    // add orderUser .
    public void add(OrderUser orderUser) {
       orderUserRepository.add(orderUser);
    }

    // get all orderUser
    public List<OrderUser> getAll() {
        return orderUserRepository.getAll() ;
    }


    // get orderUser in currentTime ;
    public OrderUser getOrderUserCurrent () {
        return orderUserRepository.getOrderUserCurrent();
    }

    public void delete(int ouId){
        orderUserRepository.delete(ouId);
    }
}
