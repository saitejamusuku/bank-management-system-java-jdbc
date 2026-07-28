package com.saiteja.bankmanagement;

import java.util.Scanner;

import com.saiteja.bankmanagement.service.UserService;
import com.saiteja.bankmanagement.util.InputHandler;

public class Main {

    public static void main(String[] args) {

        System.out.println("===== Welcome to SAMO Bank =====");

        Scanner sc = InputHandler.getScanner();

        UserService userService = new UserService();

        int res;

        do {

            System.out.println("\nMenu");
            System.out.println("1. Register");
            System.out.println("2. Exit");
            System.out.print("Choose option: ");

            res = sc.nextInt();
            sc.nextLine();

            switch(res) {

                case 1:
                    userService.register();
                    break;

                case 2:
                    System.out.println("Exiting...");
                    break;

                default:
                    System.out.println("Choose the given options");

            }

        } while(res != 2);

    }
}