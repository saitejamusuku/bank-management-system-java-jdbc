package com.saiteja.bankmanagement.config;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

@SuppressWarnings("CallToPrintStackTrace")
public class DBConnection{
    static {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

    }

    @SuppressWarnings("CallToPrintStackTrace")
    public static Connection getConnection(){

        String url = "jdbc:mysql://localhost:3306/bankmanagement";
		String user = "root";
		String password = "ROOT123";
		
		try {
			return DriverManager.getConnection(url,user,password);
		} catch (SQLException e) {
            e.printStackTrace();
		}

		return null;
        
    }

}