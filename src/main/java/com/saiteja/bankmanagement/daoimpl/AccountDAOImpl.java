package com.saiteja.bankmanagement.daoimpl;
import com.saiteja.bankmanagement.dao.AccountDAO;
import com.saiteja.bankmanagement.model.Account;
import com.saiteja.bankmanagement.config.DBConnection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;


public class AccountDAOImpl implements AccountDAO {

    Connection connect = null;
    PreparedStatement pstmt = null;
    ResultSet rs = null;
    
    public boolean createAccount(Account account){

        connect = DBConnection.getConnection();

        String sql = "insert into accounts (account_no,user_id, account_type,balance, pin) values(?,?,?,?,?) ";

        try{
            
            pstmt = connect.prepareStatement(sql);


            pstmt.setLong(1, account.getAccountNumber());
            pstmt.setInt(2, account.getUserId());
            pstmt.setString(3, account.getAccountType());
            pstmt.setDouble(4, account.getBalance());
            pstmt.setString(5,account.getPin());

            if(pstmt.executeUpdate()!=0){
                return true;
            }

        }
        
        catch(SQLException e){
            e.printStackTrace();
        }

        return false;
    }


    public boolean deposit(int user_id, long accountNumber, String pin, double deposit){

        connect = DBConnection.getConnection();
        
        try {
            String updateBalancequery = "update accounts set balance = balance + ? where account_no = ?";

            String sql = "select 1 from accounts where account_no = ? and pin = ? and user_id = ?";
            pstmt = connect.prepareStatement(sql);
            pstmt.setLong(1,accountNumber);
            pstmt.setString(2, pin);
            pstmt.setInt(3,user_id);
            rs = pstmt.executeQuery();
            if(rs.next()){
                
                pstmt = connect.prepareStatement(updateBalancequery);
                pstmt.setDouble(1,deposit);
                pstmt.setLong(2, accountNumber);

                return pstmt.executeUpdate() > 0;

            }else{
                System.out.println("Invalid Account Number or PIN");
            }
            
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
        finally{
            try {
                if (rs != null) rs.close();
                if (pstmt != null) pstmt.close();
                if (connect != null) connect.close();
            } catch (SQLException e1) {
                
                e1.printStackTrace();
            }
        }

        return false;
    }


    public double viewBalance(int user_id, long accountNumber, String pin){

        connect = DBConnection.getConnection();
        try {
            

            String sql = "select balance from accounts where account_no = ? and pin = ? and user_id = ?";

            pstmt = connect.prepareStatement(sql);

            pstmt.setLong(1,accountNumber);
            pstmt.setString(2, pin);
            pstmt.setInt(3,user_id);
            
            rs = pstmt.executeQuery();
            if(rs.next()){
                
                return rs.getDouble("balance");

            }
            
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
        finally{
            try {
                if (rs != null) rs.close();
                if (pstmt != null) pstmt.close();
                if (connect != null) connect.close();
            } catch (SQLException e1) {
                
                e1.printStackTrace();
            }
        }
        
        return -1.0;
    }


}
