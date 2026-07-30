package com.saiteja.bankmanagement.util;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;

public class AccountNumberGenerator {

    private static final Random random = new Random();
    private static final Set<Long> generatedNumbers = new HashSet<>();

    public static long generateAccountNumber() {
        long accountNumber;

        do {
            
            accountNumber = 100000000000L + (long) (random.nextDouble() * 900000000000L);
            
        } while (generatedNumbers.contains(accountNumber));

        generatedNumbers.add(accountNumber);
        return accountNumber;
    }
}