package com.saiteja.bankmanagement.dao;
import com.saiteja.bankmanagement.model.Account;


public interface AccountDAO {

    boolean createAccount(Account account);
    boolean deposit(int user_id, long accountNumber, String pin, double deposit);
    double viewBalance(int user_id,long accountNumber, String pin); 

}