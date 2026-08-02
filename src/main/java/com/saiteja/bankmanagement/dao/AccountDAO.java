package com.saiteja.bankmanagement.dao;
import com.saiteja.bankmanagement.model.Account;


public interface AccountDAO {

    boolean createAccount(Account account);
    boolean deposit(int user_id, long accountNumber, String pin, double deposit);
    double viewBalance(int user_id,long accountNumber, String pin); 
    boolean withdraw(int user_id, long accountNumber, String pin, double withdrawAmount);
    boolean transfer(int user_id, long accountNumber, String pin, double transferAmount, long toAccountNumber);
    void transactions(long accountNumber, String transcationType,double amount, String description);
    void viewTransactionHistory(long accountNumber, String pin, int user_id);
}