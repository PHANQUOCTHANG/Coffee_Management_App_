package com.example.javafxapp.Model;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class StatisticData {
    private final StringProperty period;
    private final IntegerProperty orderCount;
    private final StringProperty revenue;
    private final StringProperty average;
    private final StringProperty topProduct;

    public StatisticData(String period, int orderCount, String revenue, String average, String topProduct) {
        this.period = new SimpleStringProperty(period);
        this.orderCount = new SimpleIntegerProperty(orderCount);
        this.revenue = new SimpleStringProperty(revenue);
        this.average = new SimpleStringProperty(average);
        this.topProduct = new SimpleStringProperty(topProduct);
    }

    // Getters
    public String getPeriod() {
        return period.get();
    }

    public int getOrderCount() {
        return orderCount.get();
    }

    public String getRevenue() {
        return revenue.get();
    }

    public String getAverage() {
        return average.get();
    }

    public String getTopProduct() {
        return topProduct.get();
    }

    // Properties for TableView binding
    public StringProperty periodProperty() {
        return period;
    }

    public IntegerProperty orderCountProperty() {
        return orderCount;
    }

    public StringProperty revenueProperty() {
        return revenue;
    }

    public StringProperty averageProperty() {
        return average;
    }

    public StringProperty topProductProperty() {
        return topProduct;
    }

    // Setters nếu cần chỉnh sửa
    public void setPeriod(String period) {
        this.period.set(period);
    }

    public void setOrderCount(int orderCount) {
        this.orderCount.set(orderCount);
    }

    public void setRevenue(String revenue) {
        this.revenue.set(revenue);
    }

    public void setAverage(String average) {
        this.average.set(average);
    }

    public void setTopProduct(String topProduct) {
        this.topProduct.set(topProduct);
    }
}