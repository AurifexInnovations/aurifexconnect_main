package com.erp.Utility;

import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Component
public class DateUtils {

    // calcute future date by adding 1 month and subtracting 1 day
    public static String getFutureDate(String inputDate) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");

        // Parse the input date
        LocalDate today = LocalDate.parse(inputDate, formatter);

        // Calculate future date: +1 month -1 day
        LocalDate futureDate = today.plusMonths(1).minusDays(1);

        // Return formatted result
        return futureDate.format(formatter);
    }

    // calcute future date by adding 1 year and subtracting 1 day
    public static String getFutureDateOneYear(String inputDate) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");

        // Parse the input date
        LocalDate today = LocalDate.parse(inputDate, formatter);

        // Add 1 year and subtract 1 day
        LocalDate futureDate = today.plusYears(1).minusDays(1);

        // Return formatted result
        return futureDate.format(formatter);
    }

    public static String getFutureDate(String inputDate, String durationType) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        LocalDate baseDate = LocalDate.parse(inputDate, formatter);
        LocalDate futureDate;

        switch (durationType.toLowerCase()) {
            case "1month":
                futureDate = baseDate.plusMonths(1).minusDays(1);
                break;
            case "3month":
                futureDate = baseDate.plusMonths(3).minusDays(1);
                break;
            case "6month":
                futureDate = baseDate.plusMonths(6).minusDays(1);
                break;
            case "1year":
                futureDate = baseDate.plusYears(1).minusDays(1);
                break;
            default:
                throw new IllegalArgumentException("Unsupported duration type: " + durationType);
        }

        return futureDate.format(formatter);
    }



}
