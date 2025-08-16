package com.bankingapplication.auth_service.util;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Random;

public class UserIdGenerator {

    private static final Random RANDOM = new Random();
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyMMdd");

    /**
     * Generates a unique user ID in the format: U-YYMMDD-XXXX Where YYMMDD is
     * the current date and XXXX is a random number
     */
    public static String generateUserId() {
        String datePart = LocalDateTime.now().format(FORMATTER);
        String randomPart = String.format("%04d", RANDOM.nextInt(10000));
        return "U-" + datePart + "-" + randomPart;
    }
}
