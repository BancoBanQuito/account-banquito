package com.banquito.account.utils;

import java.lang.reflect.Field;
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

    public static boolean hasAllAttributes(Object object){
        boolean result = true;
        Field[] fields = object.getClass().getDeclaredFields();
        for(Field field : fields){
            field.setAccessible(true);
            try {
                Object value = field.get(object);
                if(value == null){
                    result = false;
                }
                else {
                    if(value.toString().isEmpty()){
                        result = false;
                    }
                }
            } catch (IllegalAccessException | NullPointerException e) {
                result = false;
            }
        }
        return result;
    }

    public static boolean isNullEmpty(Object value){
        return (value == null || value.toString().isEmpty());
    }
}
