package com.saiteja.bankmanagement.service;
import java.util.Scanner;

import com.saiteja.bankmanagement.dao.UserDAO;
import com.saiteja.bankmanagement.daoimpl.UserDAOImpl;
import com.saiteja.bankmanagement.model.User;
import com.saiteja.bankmanagement.util.InputHandler;
import com.saiteja.bankmanagement.util.validators.EmailValidator;
import com.saiteja.bankmanagement.util.validators.PhoneNumberValidator;


public class UserService {
    EmailValidator ev = new EmailValidator();
    PhoneNumberValidator pv = new PhoneNumberValidator();
    Scanner sc = InputHandler.getScanner();

    String getEmail(){
        String email;
        System.out.println("Enter your email");
        do {
            email = sc.nextLine();
            if (!ev.isValidEmail(email)) {
                System.out.println("Please Re-Enter the email");
            }
        } while (!ev.isValidEmail(email));

        return email;
    }

    String getPhone(){
        System.out.println("Enter your Phone:");
        String phone;
        do {
            phone = sc.nextLine();
            if (!pv.isValidIndianNumber(phone)) {
                System.out.println("Re-Enter your Phone");
            }
        } while (!pv.isValidIndianNumber(phone));

        return phone;
    }

    String getPassword(){
        System.out.println("Enter your Password");
        String password;
        do {
            password = sc.nextLine();

            if (password == null || password.trim().isEmpty()) {
                System.out.println("Password cannot be empty.");
            }

        } while (password == null || password.trim().isEmpty());
        return password;
    }

    public void register() {


        System.out.println("Enter your name:");
        String name = sc.nextLine();
        String email = getEmail();
        String phone = getPhone();
        String password = getPassword();
        
        User user = new User(0,name, email, phone, password);
        UserDAO userDAO = new UserDAOImpl();

        if (userDAO.register(user)) {
            System.out.println("Registration Successful");
        } else {
            System.out.println("Registration Failed");
        }


    }


    public User login(){

        UserDAO userDAO = new UserDAOImpl();
        User user = userDAO.login(getEmail(), getPassword());
        if(user!=null){
            System.out.println("Login Success");
            
            return user;
        }else{
            System.out.println("Login Failed");
        }
        return null;
    }



}
