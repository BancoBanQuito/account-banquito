package com.banquito.account.utils;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.Field;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
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

    public static LocalDateTime currentDateTime(){
        return LocalDateTime.now();
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

    public static boolean saveLog(Object object, String codeLocalAccount){
        try {
            Files.createDirectories(Paths.get("../log/"));
            String date = currentDateTime().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));
            String filename = date + "_" +codeLocalAccount + ".txt";
            File file = new File("../log/"+filename);
            FileWriter fw = new FileWriter(file);
            PrintWriter pw = new PrintWriter(fw);

            String[] messages = object.toString().split(",");

            for(String message: messages){
                pw.println(message);
            }

            pw.close();
            return true;
        }catch (IOException e)  {
            return false;
        }
    }
}
