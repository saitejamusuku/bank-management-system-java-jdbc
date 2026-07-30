package com.saiteja.bankmanagement.daoimpl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.saiteja.bankmanagement.config.DBConnection;
import com.saiteja.bankmanagement.dao.UserDAO;
import com.saiteja.bankmanagement.model.User;

public class UserDAOImpl implements UserDAO {
    Connection connect = null;
    PreparedStatement pstmt = null;
    ResultSet rs = null;
    @Override
    public boolean register(User user) {
        
        String sql = "insert into users (full_name,email,phone,password) values (?,?,?,?)";

        
        try {
            connect = DBConnection.getConnection();
            pstmt = connect.prepareStatement(sql);
            pstmt.setString(1, user.getName());
            pstmt.setString(2, user.getEmail());
            pstmt.setString(3, user.getPhone());
            pstmt.setString(4, user.getPassword());
            
            return pstmt.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
        finally{
            try {
                
                connect.close();
                pstmt.close();   

            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }



    public User login(String email, String password)  {

        String sql = "SELECT * FROM users WHERE email = ? AND password = ?";
        connect = DBConnection.getConnection();
        try (PreparedStatement pstmt = connect.prepareStatement(sql)) {

            pstmt.setString(1, email);
            pstmt.setString(2, password);
            rs = pstmt.executeQuery();

            if(rs.next()){
                User user = new User(rs.getInt(1),rs.getString(2), rs.getString(3),rs.getString(4), rs.getString(5));
                System.out.println("Debug at UserdaoImp" + user.getId());
                return user;
            }    

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }


}