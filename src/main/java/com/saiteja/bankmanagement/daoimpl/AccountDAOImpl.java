package com.saiteja.bankmanagement.daoimpl;

import com.saiteja.bankmanagement.dao.AccountDAO;
import com.saiteja.bankmanagement.model.Account;
import com.saiteja.bankmanagement.config.DBConnection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.format.DateTimeFormatter;
import java.sql.Timestamp;
import java.time.LocalDateTime;

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

    // deposit

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

    ///// Withdraw money

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

    // Transfer money
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

            String senderQuery = "select balance from accounts where account_no=? and user_id=? and pin=?";

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

            String receiverQuery = "select account_no from accounts where account_no=?";

            PreparedStatement receiverStmt = connect.prepareStatement(receiverQuery);

            receiverStmt.setLong(1, toAccount);

            ResultSet receiverRs = receiverStmt.executeQuery();

            if (!receiverRs.next()) {
                System.out.println("Receiver account not found.");
                connect.rollback();
                return false;
            }

            String debitSql = "update accounts set balance = balance - ? where account_no=?";

            PreparedStatement debitStmt = connect.prepareStatement(debitSql);

            debitStmt.setDouble(1, amount);
            debitStmt.setLong(2, fromAccount);

            int debitRows = debitStmt.executeUpdate();

            String creditSql = "update accounts set balance = balance + ? where account_no=?";

            PreparedStatement creditStmt = connect.prepareStatement(creditSql);

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

    // Transcation History

    public void transactions(long accountNumber, String transcationType, double amount, String description) {
        connect = DBConnection.getConnection();
        String transactionQuery = "insert into transactions (account_no, transaction_type, amount, description ) values (?,?,?,?)";

        try {

            pstmt = connect.prepareStatement(transactionQuery);

            pstmt.setLong(1, accountNumber);
            pstmt.setString(2, transcationType);
            pstmt.setDouble(3, amount);
            pstmt.setString(4, description);

            int rows = pstmt.executeUpdate();

            if (rows > 0) {
                // System.out.println("Transaction Updated.");
            } else {
                System.out.println("Failed to Update Transcattion");
            }

        } catch (SQLException e) {

            e.printStackTrace();
        } finally {
            try {
                if (connect != null)
                    connect.close();
                if (pstmt != null)
                    pstmt.close();
            } catch (SQLException e) {

            }
        }

    }

    public void viewTransactionHistory(long accountNumber, String pin, int user_id) {

        connect = DBConnection.getConnection();
        PreparedStatement pstmt1 = null;
        PreparedStatement pstmt2 = null;
        ResultSet rs1 = null;
        ResultSet rs2 = null;

        try {
            String historyQuery = "select * from transactions where account_no = ?";

            String verifyQuery = "select 1 from accounts where account_no = ? and pin = ? and user_id = ?";

            pstmt1 = connect.prepareStatement(verifyQuery);
            pstmt1.setLong(1, accountNumber);
            pstmt1.setString(2, pin);
            pstmt1.setInt(3, user_id);

            rs1 = pstmt1.executeQuery();

            if (rs1.next()) {

                pstmt2 = connect.prepareStatement(historyQuery);
                pstmt2.setLong(1, accountNumber);

                rs2 = pstmt2.executeQuery();
                if (rs2.next()) {

                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MMM-yyyy HH:mm");

                    System.out.println("\n================ TRANSACTION HISTORY ================");
                    System.out.printf("%-5s %-20s %-15s %-12s %s%n",
                            "ID", "Date", "Type", "Amount", "Description");
                    System.out.println(
                            "------------------------------------------------------------------------------------------------");
                    do {
                        Timestamp timestamp = rs2.getTimestamp("transaction_time");
                        String formattedDate = timestamp.toLocalDateTime().format(formatter);

                        System.out.printf("%-5d %-20s %-15s Rs. %-8.2f %s%n",
                                rs2.getInt("transaction_id"),
                                formattedDate,
                                rs2.getString("transaction_type"),
                                rs2.getDouble("amount"),
                                rs2.getString("description"));
                    } while (rs2.next());

                } else {
                    System.out.println("No transaction history found! Make your first transaction.");
                }

            } else {
                System.out.println("Invalid Account Number or PIN");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (rs1 != null) {
                    rs1.close();
                }
                if (rs2 != null) {
                    rs2.close();
                }
                if (pstmt1 != null) {
                    pstmt1.close();
                }
                if (pstmt2 != null) {
                    pstmt2.close();
                }
                if (connect != null) {
                    connect.close();
                }
            } catch (SQLException e1) {

                e1.printStackTrace();
            }
        }
    }

}
