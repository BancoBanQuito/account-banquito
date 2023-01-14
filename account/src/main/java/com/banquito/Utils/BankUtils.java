package com.banquito.Utils;

import java.security.SecureRandom;

public class BankUtils {
    public class RandomNumber {
        // private static final String characters = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
        private static final String characters = "0123456789";
        private static SecureRandom random = new SecureRandom();

        public static String generateCode(int len) {
            StringBuilder sb = new StringBuilder(len);
            for (int i = 0; i < len; i++)
                sb.append(characters.charAt(random.nextInt(characters.length())));
            return sb.toString();
        }
    }
}
