package com.saiteja.bankmanagement.daoimpl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import com.saiteja.bankmanagement.config.DBConnection;
import com.saiteja.bankmanagement.dao.UserDAO;
import com.saiteja.bankmanagement.model.User;

public class UserDAOImpl implements UserDAO {

    @Override
    public boolean register(User user) {

        Connection connect = null;
        PreparedStatement pstmt = null;
        
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
}