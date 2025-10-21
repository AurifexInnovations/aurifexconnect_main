package com.erp.Utility;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ObjectMapperUtils {

    private static final ObjectMapper objectMapper = new ObjectMapper();

    public static <T> String writeValueAsString(T data){
        log.info("Into [ObjectMapperUtils] [writeValueAsString]");

        String dataToStr = "";

        try{
            dataToStr =  objectMapper.writeValueAsString(data);
        }catch (Exception exception){
            log.error("Error [ObjectMapperUtils] [writeValueAsString]");
        }

        log.info("Exit [ObjectMapperUtils] [writeValueAsString]");

        return dataToStr;
    }
}
