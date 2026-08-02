package com.saiteja.bankmanagement;

import java.util.Scanner;

import com.saiteja.bankmanagement.model.User;
import com.saiteja.bankmanagement.service.AccountService;
import com.saiteja.bankmanagement.service.UserService;
import com.saiteja.bankmanagement.util.InputHandler;

public class Main {

    public static void main(String[] args) {

        System.out.println("===== Welcome to SAMO Bank =====");

        Scanner sc = InputHandler.getScanner();

        UserService userService = new UserService();
        AccountService accountService = new AccountService();

        int res;

        do {

            System.out.println("\n===== Main Menu =====");
            System.out.println("1. Login");
            System.out.println("2. Register");
            System.out.println("3. Exit");
            System.out.print("Choose option: ");

            res = sc.nextInt();
            sc.nextLine();

            switch (res) {

                case 1:

                    User currentUser = userService.login();

                    if (currentUser != null) {
                        boolean loggedIn = true;
                        boolean isAccount = accountService.checkAccountStatus(currentUser);

                        if (!isAccount && !accountService.createAccount(currentUser)) {
                            System.out.println("Unable to create account.");
                            break; 
                        }

                        isAccount = true;

                        System.out.println("\n===== Welcome " + currentUser.getName() + " =====");
                        
                        boolean isverified = false;
                        long accountNumber = -1;
                        String pin = "";

                        int attempts = 3;

                        while (attempts > 0 && !isverified) {

                            System.out.print("Enter your account number: ");
                            accountNumber = sc.nextLong();
                            sc.nextLine();

                            System.out.print("Enter your PIN: ");
                            pin = sc.nextLine();

                            if (accountService.verifyCredentials(accountNumber, pin, currentUser)) {
                                isverified = true;
                            } else {
                                attempts--;
                                if (attempts > 0) {
                                    System.out.println("Invalid account number or PIN.");
                                    System.out.println("Attempts remaining: " + attempts);
                                }
                            }
                        }

                        if (!isverified) {
                            System.out.println("Too many failed attempts.");
                            System.out.println("Returning to Main Menu...");
                            break; 
                        }

                        while (loggedIn && isAccount && isverified) {

                            System.out.println("\n===== Account Menu =====");
                            System.out.println("1. Deposit");
                            System.out.println("2. View Balance");
                            System.out.println("3. Transfer");
                            System.out.println("4. Withdrawl View Balance");
                            System.out.println("5. View Transaction History");
                            System.out.println("6. Logout");
                            System.out.print("Choose option: ");

                            int loginChoice = sc.nextInt();
                            sc.nextLine();

                            switch (loginChoice) {

                                case 1:
                                    accountService.deposit(currentUser, accountNumber, pin);
                                    break;

                                case 2:
                                    accountService.viewBalance(currentUser, accountNumber, pin);

                                    break;

                                case 3:
                                    accountService.transfer(currentUser, accountNumber, pin);
                                    break;

                                case 4:

                                    accountService.withdraw(currentUser, accountNumber, pin);
                                    break;
                                case 5:
                                    accountService.viewTransactionHistory(currentUser, accountNumber, pin);
                                    break;

                                case 6:
                                    System.out.println("Logged out successfully.");
                                    loggedIn = false;
                                    break;

                                default:
                                    System.out.println("Invalid choice! Please try again.");
                            }
                        }

                    } else {
                        System.out.println("Login failed!");
                    }

                    break;

                case 2:
                    userService.register();
                    break;

                case 3:
                    System.out.println("Thank you for using SAMO Bank.");
                    break;

                default:
                    System.out.println("Please choose a valid option.");
            }

        } while (res != 3);

        sc.close();
    }
}