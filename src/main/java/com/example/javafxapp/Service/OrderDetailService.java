package com.example.javafxapp.Service;

import java.util.List;

import com.example.javafxapp.Model.OrderDetail;
import com.example.javafxapp.Model.Product;
import com.example.javafxapp.Repository.OrderDetailRepository;

import javafx.collections.ObservableList;

public class OrderDetailService {
    private OrderDetailRepository odr = new OrderDetailRepository();

    public List<OrderDetail> getAll(int id){
        return odr.getAll(id);
    }

    public boolean checkChange(int id, ObservableList<OrderDetail> obs){
        return odr.checkChange(id, obs);
    }

    public void update(int id, ObservableList<OrderDetail> obs){
        odr.update(id, obs);
    }
}
