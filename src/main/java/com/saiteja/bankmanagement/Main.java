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

                        System.out.println("\n===== Welcome " + currentUser.getName() + " =====");

                        boolean loggedIn = true;

                        while (loggedIn) {

                            System.out.println("\n===== Account Menu =====");
                            System.out.println("1. Create Account");
                            System.out.println("2. Deposit");
                            System.out.println("3. View Balance");
                            System.out.println("4. Transfer");
                            System.out.println("5. Withdrawl View Balance");
                            System.out.println("6. Logout");
                            System.out.print("Choose option: ");

                            int loginChoice = sc.nextInt();
                            sc.nextLine();

                            switch (loginChoice) {

                                case 1:
                                    accountService.createAccount(currentUser);
                                    break;

                                case 2:
                                    accountService.deposit(currentUser);
                                    break;

                                case 3:
                                    accountService.viewBalance(currentUser);
                                    
                                    break;

                                case 4:
                                    accountService.transfer(currentUser);
                                    break;

                                case 5:
                                    
                                    accountService.withdraw(currentUser);
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