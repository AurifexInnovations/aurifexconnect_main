package com.erp.Service.SalaryService;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import java.time.YearMonth;

@Converter(autoApply = true)
public class YearMonthAttributeConverter implements AttributeConverter<YearMonth, String> {

    @Override
    public String convertToDatabaseColumn(YearMonth attribute) {
        return (attribute != null) ? attribute.toString() : null; // Stored as "2025-11"
    }

    @Override
    public YearMonth convertToEntityAttribute(String dbData) {
        return (dbData != null) ? YearMonth.parse(dbData) : null;
    }
}
