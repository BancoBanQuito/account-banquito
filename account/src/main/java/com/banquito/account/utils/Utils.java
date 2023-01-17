package com.banquito.account.utils;

import java.security.SecureRandom;
import java.util.Date;

public class Utils {

    private static final String numberString = "0123456789";
    private static final String alphanumericString = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
    private static SecureRandom random = new SecureRandom();

    public static String generateNumberCode(int length) {
        StringBuilder sb = new StringBuilder(length);
        for (int i = 0; i < length; i++)
            sb.append(numberString.charAt(random.nextInt(numberString.length())));
        return sb.toString();
    }

    public static String generateAlphanumericCode(int length){
        StringBuilder sb = new StringBuilder(length);
        for (int i = 0; i < length; i++)
            sb.append(alphanumericString.charAt(random.nextInt(numberString.length())));
        return sb.toString();
    }

    public static Date currentDate(){
        return new Date();
    }
}
