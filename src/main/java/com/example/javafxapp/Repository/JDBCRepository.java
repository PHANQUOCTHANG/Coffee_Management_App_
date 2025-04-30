package com.example.javafxapp.Repository;

import java.util.List;

public interface JDBCRepository<T> {
    public void add(T a) ;
    public void update(T a) ;
    public void delete(int id) ;
    public List<T> getAll() ;
    public T findByID(int id) ;
    public T findByName(String name) ;
}
