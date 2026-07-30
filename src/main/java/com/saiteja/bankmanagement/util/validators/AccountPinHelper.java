package com.saiteja.bankmanagement.util.validators;
import com.saiteja.bankmanagement.util.InputHandler;
import java.util.Scanner;
public class AccountPinHelper {
    
    public long getAccountNumber(){
        System.out.println("Enter the account number:");
        Scanner sc = InputHandler.getScanner();
        long acc = sc.nextLong();
        sc.nextLine();
        return acc;

    }
    public String getPin(){
        System.out.println("Enter the pin number:");
        Scanner sc = InputHandler.getScanner();
        String pin = sc.nextLine();
        return pin;

    }
}
