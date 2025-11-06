package com.erp.Mapper.Varient;

import com.erp.Dto.VarientDto;
import com.erp.Model.Varient;
import jakarta.persistence.Column;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

@Component
public class VarientMapper {

    public Varient map(VarientDto varientDto){
        return update(null , varientDto);
    }

    public Varient update(Varient varient , VarientDto varientDto){

        if(Objects.isNull(varient)){
            varient = new Varient();
        }

        if(Objects.nonNull(varientDto)) {
            varient.setItemId(varientDto.getItemId());
            varient.setStockQuantity(varientDto.getStockQuantity());
            varient.setSellingPriceType(varientDto.getSellingPriceType());
            varient.setSellingPrice(varientDto.getSellingPrice());
            varient.setPurchasePriceType(varientDto.getPurchasePriceType());
            varient.setPurchasePrice(varientDto.getPurchasePrice());
            varient.setUnitType(varientDto.getUnitType());
            varient.setUnitTypeValue(varientDto.getUnitTypeValue());
            varient.setMeasurementType(varientDto.getMeasurementType());
            varient.setMeasurement(varientDto.getMeasurement());
            varient.setExpiryDate(varientDto.getExpiryDate());
        }

        return varient;
    }

    public VarientDto map(Varient varient){
        return update(null , varient);
    }

    public VarientDto update(VarientDto varientDto , Varient varient){
        if(Objects.isNull(varientDto)){
            varientDto = new VarientDto();
        }

        if(Objects.nonNull(varient)){
            varientDto.setId(varient.getId());
            varientDto.setItemId(varient.getItemId());
            varientDto.setStockQuantity(varient.getStockQuantity());
            varientDto.setSellingPriceType(varient.getSellingPriceType());
            varientDto.setSellingPrice(varient.getSellingPrice());
            varientDto.setPurchasePriceType(varient.getPurchasePriceType());
            varientDto.setPurchasePrice(varient.getPurchasePrice());
            varientDto.setUnitType(varient.getUnitType());
            varientDto.setUnitTypeValue(varient.getUnitTypeValue());
            varientDto.setMeasurementType(varient.getMeasurementType());
            varientDto.setMeasurement(varient.getMeasurement());
            varientDto.setExpiryDate(varient.getExpiryDate());
        }

        return varientDto;
    }

    public List<VarientDto> map(List<Varient>  varientList){
        return update(null ,varientList);
    }

    public List<VarientDto> update(List<VarientDto> varientDtos ,List<Varient>  varients) {
        if (Objects.isNull(varientDtos)) {
            varientDtos = new LinkedList<>();
        }

        if (Objects.nonNull(varients)) {
            for (Varient varient : varients) {
                varientDtos.add(map(varient));
            }
        }

        return varientDtos;
    }

    public List<Varient> mapDtosToEntities(List<VarientDto>  varientDtos){
        return updateDtosToEntities(null ,varientDtos);
    }

    public List<Varient> updateDtosToEntities(List<Varient> varients ,List<VarientDto>  varientDtos){
        if(Objects.isNull(varients)){
            varients = new LinkedList<>();
        }

        if(Objects.nonNull(varientDtos)){
            for (VarientDto varientDto : varientDtos ){
                varients.add(map(varientDto));
            }
        }

        return varients;
    }
}
