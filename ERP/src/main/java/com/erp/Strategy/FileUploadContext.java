package com.erp.Strategy;

import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class FileUploadContext {
    private final List<FileUploadStrategy> strategies;

    public FileUploadContext(List<FileUploadStrategy> strategies) {
        this.strategies = strategies;
    }

    public FileUploadStrategy getStrategy(String category) {
        return strategies.stream()
                .filter(s -> s.supports(category))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("No strategy found for category: " + category));
    }
}
