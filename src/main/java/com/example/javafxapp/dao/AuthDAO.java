package com.example.javafxapp.dao;

import com.example.javafxapp.config.DatabaseConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class AuthDAO {

    // check account
    public boolean isValidAccount(String accountName, String password) throws SQLException {
        String query = "SELECT * FROM account WHERE account_name= ? AND password = ?";
        Connection connection = DatabaseConnection.getConnection() ;
        PreparedStatement preparedStatement = connection.prepareStatement(query) ;
        preparedStatement.setString(1,accountName);
        preparedStatement.setString(2,password);
        ResultSet rs = preparedStatement.executeQuery() ;
        return rs.next() ;
    }
}
