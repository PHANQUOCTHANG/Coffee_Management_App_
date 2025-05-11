package com.example.javafxapp.Repository;

import com.example.javafxapp.Config.DatabaseConnection;
import com.example.javafxapp.Model.Category;
import com.example.javafxapp.Model.Product;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ProductRepository implements JDBCRepository<Product> {

    // add product .
    public void add(Product product) {
        String sql = "INSERT INTO Product(product_name , description , price , category_id , imgSrc , status , outstanding) Value (?,?,?,?,?,?,?) " ;
        try(Connection connection = DatabaseConnection.getConnection() ;
            PreparedStatement pstmt = connection.prepareStatement(sql , Statement.RETURN_GENERATED_KEYS)
        ) {
            pstmt.setString(1,product.getProduct_name());
            pstmt.setString(2,product.getDescription());
            pstmt.setDouble(3,product.getPrice());
            pstmt.setInt(4,product.getCategory_id());
            pstmt.setString(5, product.getImgSrc());
            pstmt.setBoolean(6,product.isStatus());
            pstmt.setBoolean(7, product.isOutstanding());
            pstmt.executeUpdate() ;
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // update product .
    public void update(Product product) {
        String sql = "UPDATE Product set product_name = ? , description = ? , price = ? , category_id = ? , imgSrc = ? , status = ? , outstanding = ? where product_id = ?" ;
        try(Connection connection = DatabaseConnection.getConnection() ;
            PreparedStatement pstmt = connection.prepareStatement(sql , Statement.RETURN_GENERATED_KEYS)
        ) {
            pstmt.setString(1,product.getProduct_name());
            pstmt.setString(2,product.getDescription());
            pstmt.setDouble(3,product.getPrice());
            pstmt.setInt(4,product.getCategory_id());
            pstmt.setString(5, product.getImgSrc());
            pstmt.setBoolean(6,product.isStatus());
            pstmt.setBoolean(7, product.isOutstanding());
            pstmt.setInt(8,product.getProduct_id());
            pstmt.executeUpdate() ;
        } catch (SQLException e) {
            e.printStackTrace() ;
        }
    }

    // delete product
    public void delete(int productId) {
        String sql = "UPDATE Product set deleted = ? where product_id = ?" ;
        try(Connection connection = DatabaseConnection.getConnection() ;
            PreparedStatement pstmt = connection.prepareStatement(sql) ;
        ) {
            pstmt.setBoolean(1,true);
            pstmt.setInt(2,productId);
            pstmt.executeUpdate() ;
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // get all product .
    public List<Product> getAll(){
        String sql = "SELECT * FROM Product where deleted = ?" ;
        List<Product> products = new ArrayList<>() ;
        try (Connection connection = DatabaseConnection.getConnection() ;
            PreparedStatement pstmt = connection.prepareStatement(sql) ;
        ) {
            pstmt.setBoolean(1,false);
            ResultSet rs = pstmt.executeQuery();
            while(rs.next()) {
                products.add(new Product(
                        rs.getInt("product_id") ,
                        rs.getString("product_name")  ,
                        rs.getString("description") ,
                        rs.getDouble("price") ,
                        rs.getInt("category_id")  ,
                        rs.getString("imgSrc") ,
                        rs.getBoolean("status"),
                        rs.getBoolean("outstanding") ,
                        rs.getBoolean("deleted")
                )) ;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return products ;
    }

    // find product by product_name .
    public Product findByID(int productId) {
        String sql = "SELECT * from product where product_id = ? AND deleted = ?" ;
        try(Connection connection = DatabaseConnection.getConnection() ;
            PreparedStatement preparedStatement = connection.prepareStatement(sql)
        ){
            preparedStatement.setInt(1,productId);
            preparedStatement.setBoolean(2,false);
            ResultSet rs = preparedStatement.executeQuery() ;
            if (rs.next()) {
                return new Product(
                        rs.getInt("product_id") ,
                        rs.getString("product_name")  ,
                        rs.getString("description") ,
                        rs.getDouble("price") ,
                        rs.getInt("category_id")  ,
                        rs.getString("imgSrc") ,
                        rs.getBoolean("status"),
                        rs.getBoolean("outstanding") ,
                        rs.getBoolean("deleted")) ;
            }
        }
        catch(SQLException e) {
            e.printStackTrace();
        }
        return null ;
    }

    // find product by product_name .
    public Product findByName(String product_name) {
        String sql = "SELECT * from product where product_name = ? AND deleted = ?" ;
        try(Connection connection = DatabaseConnection.getConnection() ;
        PreparedStatement preparedStatement = connection.prepareStatement(sql)
        ){
            preparedStatement.setString(1,product_name);
            preparedStatement.setBoolean(2,false);
            ResultSet rs = preparedStatement.executeQuery() ;
            if (rs.next()) {
                return new Product(
                        rs.getInt("product_id") ,
                        rs.getString("product_name")  ,
                        rs.getString("description") ,
                        rs.getDouble("price") ,
                        rs.getInt("category_id")  ,
                        rs.getString("imgSrc") ,
                        rs.getBoolean("status"),
                        rs.getBoolean("outstanding") ,
                        rs.getBoolean("deleted")) ;
            }
        }
        catch(SQLException e) {
            e.printStackTrace();
        }
        return null ;
    }

    // get all product by category_id.
    public List<Product> getAllByCategoryId(int category_id){
        String sql = "SELECT * FROM Product where deleted = ? AND category_id = ?" ;
        List<Product> products = new ArrayList<>() ;
        try (Connection connection = DatabaseConnection.getConnection() ;
             PreparedStatement pstmt = connection.prepareStatement(sql) ;
        ) {
            pstmt.setBoolean(1,false);
            pstmt.setInt(2,category_id);
            ResultSet rs = pstmt.executeQuery();
            while(rs.next()) {
                products.add(new Product(
                        rs.getInt("product_id") ,
                        rs.getString("product_name")  ,
                        rs.getString("description") ,
                        rs.getDouble("price") ,
                        rs.getInt("category_id")  ,
                        rs.getString("imgSrc") ,
                        rs.getBoolean("status"),
                        rs.getBoolean("outstanding") ,
                        rs.getBoolean("deleted")
                )) ;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return products ;
    }

    // get all product is outstanding .
    public List<Product> getAllIsOutStanding(){
        String sql = "SELECT * FROM Product where deleted = ? and outstanding = ? and status = ?" ;
        List<Product> products = new ArrayList<>() ;
        try (Connection connection = DatabaseConnection.getConnection() ;
             PreparedStatement pstmt = connection.prepareStatement(sql) ;
        ) {
            pstmt.setBoolean(1,false);
            pstmt.setBoolean(2,true);
            pstmt.setBoolean(3,false);
            ResultSet rs = pstmt.executeQuery();
            while(rs.next()) {
                products.add(new Product(
                        rs.getInt("product_id") ,
                        rs.getString("product_name")  ,
                        rs.getString("description") ,
                        rs.getDouble("price") ,
                        rs.getInt("category_id")  ,
                        rs.getString("imgSrc") ,
                        rs.getBoolean("status"),
                        rs.getBoolean("outstanding") ,
                        rs.getBoolean("deleted")
                )) ;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return products ;
    }

    // change status
    public void changeStatus(int productId , boolean status) {
        String sql = "UPDATE Product set status = ? where product_id = ?" ;
        try(Connection connection = DatabaseConnection.getConnection() ;
            PreparedStatement pstmt = connection.prepareStatement(sql) ;
        ) {
            pstmt.setBoolean(1,!status);
            pstmt.setInt(2,productId);
            pstmt.executeUpdate() ;
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // change status
    public void changeOutStanding(int productId , boolean outstanding) {
        String sql = "UPDATE Product set outstanding = ? where product_id = ?" ;
        try(Connection connection = DatabaseConnection.getConnection() ;
            PreparedStatement pstmt = connection.prepareStatement(sql) ;
        ) {
            pstmt.setBoolean(1,!outstanding);
            pstmt.setInt(2,productId);
            pstmt.executeUpdate() ;
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // find all product by keyword .
    public List<Product> findAllByKeyword(String keyword) {
        String sql = "SELECT * FROM product WHERE product_name LIKE ? AND deleted = ?";
        List<Product> products = new ArrayList<>() ;
        try (Connection connection = DatabaseConnection.getConnection() ;
             PreparedStatement preparedStatement = connection.prepareStatement(sql) ;
        ){
            preparedStatement.setString(1, "%" + keyword + "%");
            preparedStatement.setBoolean(2, false);
            ResultSet rs = preparedStatement.executeQuery();
            while(rs.next()) {
                products.add(new Product(
                        rs.getInt("product_id") ,
                        rs.getString("product_name")  ,
                        rs.getString("description") ,
                        rs.getDouble("price") ,
                        rs.getInt("category_id")  ,
                        rs.getString("imgSrc") ,
                        rs.getBoolean("status"),
                        rs.getBoolean("outstanding") ,
                        rs.getBoolean("deleted")
                )) ;
            }
        }catch(SQLException e) {
            e.printStackTrace();
        }
        return products ;
    }

    public List<Product> getProductsByCategory(String category){
        String sql = "SELECT * FROM product WHERE category_id = ? AND deleted = ?";
        List<Product> products = new ArrayList<>();
        try (Connection connection = DatabaseConnection.getConnection();
            PreparedStatement pstmt = connection.prepareStatement(sql)){
            pstmt.setString(1, category);
            pstmt.setBoolean(2, false);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()){  
                products.add(new Product(
                        rs.getInt("product_id") ,
                        rs.getString("product_name")  ,
                        rs.getString("description") ,
                        rs.getDouble("price") ,
                        rs.getInt("category_id")  ,
                        rs.getString("imgSrc") ,
                        rs.getBoolean("status"),
                        rs.getBoolean("outstanding") ,
                        rs.getBoolean("deleted")
                ));
            }
        } catch (SQLException e){   
            e.printStackTrace();
        }
        return products;
    }

    public List<Product> getProductsByType(String type){
        String sql = "SELECT * FROM product WHERE type = ? AND deleted = ?";
        List<Product> products = new ArrayList<>();
        try (Connection connection = DatabaseConnection.getConnection();
            PreparedStatement pstmt = connection.prepareStatement(sql)){    
            pstmt.setString(1, type);
            pstmt.setBoolean(2, false);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()){
                products.add(new Product(
                        rs.getInt("product_id") ,
                        rs.getString("product_name")  ,
                        rs.getString("description") ,
                        rs.getDouble("price") ,
                        rs.getInt("category_id")  ,
                        rs.getString("imgSrc") ,
                        rs.getBoolean("status"),
                        rs.getBoolean("outstanding") ,
                        rs.getBoolean("deleted")
                ));
            }
        } catch (SQLException e){
            e.printStackTrace();
        }
        return products;
    }
}
