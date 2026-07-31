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

    public boolean createAccount(Account account) {

        connect = DBConnection.getConnection();

        String sql = "insert into accounts (account_no,user_id, account_type,balance, pin) values(?,?,?,?,?) ";

        try {

            pstmt = connect.prepareStatement(sql);

            pstmt.setLong(1, account.getAccountNumber());
            pstmt.setInt(2, account.getUserId());
            pstmt.setString(3, account.getAccountType());
            pstmt.setDouble(4, account.getBalance());
            pstmt.setString(5, account.getPin());

            if (pstmt.executeUpdate() != 0) {
                return true;
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }

    public boolean deposit(int user_id, long accountNumber, String pin, double deposit) {

        connect = DBConnection.getConnection();

        try {
            String updateBalancequery = "update accounts set balance = balance + ? where account_no = ?";

            String sql = "select 1 from accounts where account_no = ? and pin = ? and user_id = ?";
            pstmt = connect.prepareStatement(sql);
            pstmt.setLong(1, accountNumber);
            pstmt.setString(2, pin);
            pstmt.setInt(3, user_id);
            rs = pstmt.executeQuery();
            if (rs.next()) {

                pstmt = connect.prepareStatement(updateBalancequery);
                pstmt.setDouble(1, deposit);
                pstmt.setLong(2, accountNumber);

                return pstmt.executeUpdate() > 0;

            } else {
                System.out.println("Invalid Account Number or PIN");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
                if (pstmt != null) {
                    pstmt.close();
                }
                if (connect != null) {
                    connect.close();
                }
            } catch (SQLException e1) {

                e1.printStackTrace();
            }
        }

        return false;
    }

    public double viewBalance(int user_id, long accountNumber, String pin) {

        connect = DBConnection.getConnection();
        try {

            String sql = "select balance from accounts where account_no = ? and pin = ? and user_id = ?";

            pstmt = connect.prepareStatement(sql);

            pstmt.setLong(1, accountNumber);
            pstmt.setString(2, pin);
            pstmt.setInt(3, user_id);

            rs = pstmt.executeQuery();
            if (rs.next()) {

                return rs.getDouble("balance");

            }

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
                if (pstmt != null) {
                    pstmt.close();
                }
                if (connect != null) {
                    connect.close();
                }
            } catch (SQLException e1) {

                e1.printStackTrace();
            }
        }

        return -1.0;
    }

    /////Withdraw money
    
    public boolean withdraw(int user_id, long accountNumber, String pin, double withdrawAmount) {

        double currentBalance = viewBalance(user_id, accountNumber, pin);
        if (currentBalance < 0) {
            System.out.println("Invalid Account Number or PIN");
            return false;
        }
        if (currentBalance < withdrawAmount) {
            System.out.println("Insufficient Balance");
            return false;
        } else {

            connect = DBConnection.getConnection();

            try {
                String updateBalancequery = "update accounts set balance = balance - ? where account_no = ?";

                pstmt = connect.prepareStatement(updateBalancequery);
                pstmt.setDouble(1, withdrawAmount);
                pstmt.setLong(2, accountNumber);

                return pstmt.executeUpdate() > 0;

            } catch (SQLException e) {
                e.printStackTrace();
            } finally {
                try {
                    if (rs != null) {
                        rs.close();
                    }
                    if (pstmt != null) {
                        pstmt.close();
                    }
                    if (connect != null) {
                        connect.close();
                    }
                } catch (SQLException e1) {

                    e1.printStackTrace();
                }
            }

        }
        return false;
    }

    //Transfer money
    @Override
    public boolean transfer(int userId,
                            long fromAccount,
                            String pin,
                            double amount,
                            long toAccount) {

        Connection connect = null;

        try {

            connect = DBConnection.getConnection();
            connect.setAutoCommit(false);

            
            String senderQuery =
                    "select balance from accounts where account_no=? and user_id=? and pin=?";

            PreparedStatement senderStmt = connect.prepareStatement(senderQuery);

            senderStmt.setLong(1, fromAccount);
            senderStmt.setInt(2, userId);
            senderStmt.setString(3, pin);

            ResultSet rs = senderStmt.executeQuery();

            if (!rs.next()) {
                System.out.println("Invalid sender account or PIN.");
                connect.rollback();
                return false;
            }

            double currentBalance = rs.getDouble("balance");

            if (currentBalance < amount) {
                System.out.println("Insufficient Balance.");
                connect.rollback();
                return false;
            }

            
            String receiverQuery =
                    "select account_no from accounts where account_no=?";

            PreparedStatement receiverStmt =
                    connect.prepareStatement(receiverQuery);

            receiverStmt.setLong(1, toAccount);

            ResultSet receiverRs = receiverStmt.executeQuery();

            if (!receiverRs.next()) {
                System.out.println("Receiver account not found.");
                connect.rollback();
                return false;
            }

            String debitSql =
                    "update accounts set balance = balance - ? where account_no=?";

            PreparedStatement debitStmt =
                    connect.prepareStatement(debitSql);

            debitStmt.setDouble(1, amount);
            debitStmt.setLong(2, fromAccount);

            int debitRows = debitStmt.executeUpdate();

            
            String creditSql =
                    "update accounts set balance = balance + ? where account_no=?";

            PreparedStatement creditStmt =
                    connect.prepareStatement(creditSql);

            creditStmt.setDouble(1, amount);
            creditStmt.setLong(2, toAccount);

            int creditRows = creditStmt.executeUpdate();

            if (debitRows == 1 && creditRows == 1) {

                connect.commit();
                return true;

            } else {

                connect.rollback();
                return false;
            }

        } catch (SQLException e) {

            try {
                if (connect != null)
                    connect.rollback();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }

            e.printStackTrace();

        } finally {

            try {
                if (connect != null) {
                    connect.setAutoCommit(true);
                    connect.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        return false;
    }

}
