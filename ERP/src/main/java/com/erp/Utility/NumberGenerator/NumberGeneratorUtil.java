package com.erp.Utility.NumberGenerator;

import lombok.AllArgsConstructor;

import java.time.LocalDate;

@AllArgsConstructor
public class NumberGeneratorUtil {

    public static String generate(String prefix, long nextSequence) {
        int year = LocalDate.now().getYear();

        // Format: PREFIX-YYYY-XXXX
        // Example: INV-2025-0001
        return String.format("%s-%d-%04d", prefix, year, nextSequence);
    }

}
