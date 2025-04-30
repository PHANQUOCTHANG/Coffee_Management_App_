package com.example.javafxapp.Repository;

import com.example.javafxapp.Config.DatabaseConnection;
import com.example.javafxapp.Model.Cart;
import com.example.javafxapp.Model.Product;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CartRepository implements JDBCRepository<Cart> {

    // add cart .
    public void add(Cart cart) {
        String sql = "INSERT INTO Cart(account_id) Value (?) " ;
        try(Connection connection = DatabaseConnection.getConnection();
            PreparedStatement pstmt = connection.prepareStatement(sql , Statement.RETURN_GENERATED_KEYS)
        ) {
            pstmt.setInt(1,cart.getAccount_id());
            pstmt.executeUpdate() ;
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // update cart .
    public void update(Cart cart) {
        String sql = "UPDATE Cart set account_id = ? where cart_id = ?" ;
        try(Connection connection = DatabaseConnection.getConnection() ;
            PreparedStatement pstmt = connection.prepareStatement(sql , Statement.RETURN_GENERATED_KEYS)
        ) {
            pstmt.setInt(1,cart.getAccount_id());
            pstmt.setInt(2,cart.getCart_id());
            pstmt.executeUpdate() ;
        } catch (SQLException e) {
            e.printStackTrace() ;
        }
    }

    // delete cart .
    public void delete(int cartId) {
        String sql = "UPDATE Cart set deleted = ? where cart_id = ?" ;
        try(Connection connection = DatabaseConnection.getConnection() ;
            PreparedStatement pstmt = connection.prepareStatement(sql) ;
        ) {
            pstmt.setBoolean(1,true);
            pstmt.setInt(2,cartId);
            pstmt.executeUpdate() ;
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // get all cart .
    public List<Cart> getAll(){
        String sql = "SELECT * FROM Cart where deleted = ?" ;
        List<Cart> carts = new ArrayList<>() ;
        try (Connection connection = DatabaseConnection.getConnection() ;
             PreparedStatement pstmt = connection.prepareStatement(sql) ;
        ) {
            pstmt.setBoolean(1,false);
            ResultSet rs = pstmt.executeQuery();
            while(rs.next()) {
                carts.add(new Cart(
                        rs.getInt("cart_id") ,
                        rs.getInt("account_id")  ,
                        rs.getBoolean("deleted")
                )) ;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return carts ;
    }

    // find cart by cart_id .
    public Cart findByID(int cartId) {
        String sql = "SELECT * from Cart where cart_id = ? AND deleted = ?" ;
        try(Connection connection = DatabaseConnection.getConnection() ;
            PreparedStatement preparedStatement = connection.prepareStatement(sql)
        ){
            preparedStatement.setInt(1,cartId);
            preparedStatement.setBoolean(2,false);
            ResultSet rs = preparedStatement.executeQuery() ;
            if (rs.next()) {
                return new Cart(
                        rs.getInt("cart_id") ,
                        rs.getInt("account_id")  ,
                        rs.getBoolean("deleted")) ;
            }
        }
        catch(SQLException e) {
            e.printStackTrace();
        }
        return null ;
    }

    // find cart by cart_name .
    public Cart findByName(String cart_name) {
        return null ;
    }

    // check account have cart ?
    public Integer checkAccountHaveCart (int account_id) {
        String sql = "SELECT cart_id from Cart where account_id = ? AND deleted = ?" ;
        try(Connection connection = DatabaseConnection.getConnection() ;
            PreparedStatement preparedStatement = connection.prepareStatement(sql)
        ){
            preparedStatement.setInt(1,account_id);
            preparedStatement.setBoolean(2,false);
            ResultSet rs = preparedStatement.executeQuery() ;
            if (rs.next()) return rs.getInt("cart_id") ;
        }
        catch(SQLException e) {
            e.printStackTrace();
        }
        return null ;
    }
}
