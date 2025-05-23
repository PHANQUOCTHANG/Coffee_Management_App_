package com.example.javafxapp.Model;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;

public class Statistic {
    private final SimpleStringProperty period;
    private final SimpleStringProperty orderCount;
    private final SimpleStringProperty revenue;
    private final SimpleStringProperty average;
    private final SimpleStringProperty topProduct;

    public Statistic(String period, String orderCount, String revenue, String average, String topProduct) {
        this.period = new SimpleStringProperty(period);
        this.orderCount = new SimpleStringProperty(orderCount);
        this.revenue = new SimpleStringProperty(revenue);
        this.average = new SimpleStringProperty(average);
        this.topProduct = new SimpleStringProperty(topProduct);
    }


    public String getPeriod() { return period.get(); }
    public String getOrderCount() { return orderCount.get(); }
    public String getRevenue() { return revenue.get(); }
    public String getAverage() { return average.get(); }
    public String getTopProduct() { return topProduct.get(); }
}