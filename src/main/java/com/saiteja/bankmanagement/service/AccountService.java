package com.saiteja.bankmanagement.service;

import java.util.Scanner;

import com.saiteja.bankmanagement.dao.AccountDAO;
import com.saiteja.bankmanagement.daoimpl.AccountDAOImpl;
import com.saiteja.bankmanagement.model.Account;
import com.saiteja.bankmanagement.model.User;
import com.saiteja.bankmanagement.util.AccountNumberGenerator;
import com.saiteja.bankmanagement.util.InputHandler;
import com.saiteja.bankmanagement.util.validators.AccountPinHelper;

public class AccountService {
    AccountPinHelper ac = new AccountPinHelper();
    private final Scanner sc = InputHandler.getScanner();
    private final AccountDAO accountDAO = new AccountDAOImpl();

    public boolean checkAccountStatus(User user){
        if(accountDAO.checkAccountStatus(user.getId())){
            return true;
        }
        return false;
    }

    public boolean verifyCredentials(long accountNumber, String pin, User user){
        if(accountDAO.verifyCredientals(accountNumber, pin, user.getId())){
            System.out.println("Account details verified");
            return true;
        }
        else{
            System.out.println("Please check your credient and TRY AGAIN !!");
        }
        
        return false;
    }

    public boolean createAccount(User user) {
        System.out.println("please open your bank account:");

        String accountType = getAccountType();

        double balance = getInitialDeposit();

        String pin = getPin();

        long accountNumber = AccountNumberGenerator.generateAccountNumber();

        Account account = new Account(
                accountNumber,
                user.getId(),
                accountType,
                balance,
                pin
        );

        if (accountDAO.createAccount(account)) {
            System.out.println();
            System.out.println("\nAccount Created Successfully!");
            System.out.println("Account Number : " + accountNumber);
            System.out.println();
            return true;
        } else {
            System.out.println();
            System.out.println("\nFailed to create account.");
            System.out.println();
        }
        return false;
    }

    private String getAccountType() {

        while (true) {

            System.out.println("""
                    
                    ===== Select Account Type =====
                    1. Savings Account
                    2. Current Account
                    """);

            int choice = sc.nextInt();
            sc.nextLine();

            switch (choice) {
                case 1:
                    return "SAVINGS";

                case 2:
                    return "CURRENT";

                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }

    private double getInitialDeposit() {

        while (true) {

            System.out.print("Enter Initial Deposit (₹0 - ₹10,000): ");

            double balance = sc.nextDouble();
            sc.nextLine();

            if (balance >= 0 && balance <= 10000) {
                return balance;
            }

            System.out.println("Invalid amount.");
        }
    }

    private String getPin() {

        while (true) {

            System.out.print("Set 4-Digit PIN: ");

            String pin = sc.nextLine();

            if (pin.matches("\\d{4}")) {
                return pin;
            }

            System.out.println("PIN must contain exactly 4 digits.");
        }
    }

    //deposit

    public void deposit(User user, long accountNumber, String pin){

        System.out.println("Enter the deposit amount:");
        double deposit = sc.nextDouble();sc.nextLine();

        if(accountDAO.deposit(user.getId(),accountNumber, pin, deposit)){

            accountDAO.transactions(accountNumber,"DEPOSIT", deposit, "Rupees"+ deposit + " as been added to your account");
            System.out.println();
            System.out.println("Succesfully deposited: "+ deposit);
            System.out.println();
        }
        else{
            System.out.println();
            System.out.println("Failure!!");
            System.out.println();
        }

    }

    public void viewBalance(User currentUser, long accountNumber, String pin){

   
        double currentUserBalance = accountDAO.viewBalance(currentUser.getId(),accountNumber, pin);
        if(currentUserBalance >= 0)
        {   
            System.out.println();
            System.out.println("Your Balance is: "+ currentUserBalance);
        }
        else
        {   
            System.out.println();
            System.out.println("Invalid Account Number or PIN");
            System.out.println();
        }

    }


    public void withdraw(User user, long accountNumber, String pin){

        System.out.println("Enter Withdraw Amount:");
        double withdrawAmount = sc.nextDouble();
         sc.nextLine();

        if(accountDAO.withdraw(user.getId(), accountNumber, pin, withdrawAmount)){
            
            accountDAO.transactions(accountNumber,"WITHDRAW", withdrawAmount, "Rupees"+ withdrawAmount + " as been drawn from your account");
            System.out.println("withdraw Successful");

        }

    }


    public void transfer(User user, long fromAccount, String pin){
;
        System.out.println("Enter the amount you want to transfer: ");
        double amount = sc.nextDouble();
        sc.nextLine();
        System.out.println("Enter Recevier accout number");
        long toAccount = sc.nextLong();

        if (fromAccount == toAccount) {
            System.out.println("Cannot transfer to the same account.");
            return;
        }

        if(accountDAO.transfer(user.getId(), fromAccount, pin, amount, toAccount)){
            accountDAO.transactions(fromAccount,"TRANSFER_OUT", amount, "Rupees"+ amount + " as been sent from your account to " + toAccount);
            accountDAO.transactions(toAccount,"TRANSFER_IN", amount, "Rupees"+ amount + " as been recevied to your account from " + fromAccount);
            System.out.println("Amount transfered successfully");
        }
        else{
            System.out.println("Transfer failed. No money was deducted from your account.");
        }

    }

    public void viewTransactionHistory(User user, long accountNumber, String pin){


        accountDAO.viewTransactionHistory(accountNumber, pin,user.getId());

    }

}