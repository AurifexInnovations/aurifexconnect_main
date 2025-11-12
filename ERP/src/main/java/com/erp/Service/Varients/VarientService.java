package com.erp.Service.Varients;

import com.erp.Dto.VarientDto;
import com.erp.Mapper.Varient.VarientMapper;
import com.erp.Model.Varient;
import com.erp.Repository.Varient.VarientRepository;
import com.erp.Utility.ObjectMapperUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
@Slf4j
public class VarientService {

    private final VarientRepository varientRepository;
    private final VarientMapper varientMapper;

    public List<VarientDto> addAndUpdateVarients(List<VarientDto> varientDtos){
        log.info("Into [VarientService] [addVarients] ");

        log.info("[VarientService] [addVarients]  :: VarientRequest :: {} "
                , ObjectMapperUtils.writeValueAsString(varientDtos));

        List<Varient> varients = varientMapper.mapDtosToEntities(varientDtos);

        varients = varientRepository.saveAll(varients);

        log.info("Exit [VarientService] [addVarients] ");

        return varientMapper.map(varients);
    }

    public List<VarientDto> updateVarients(List<VarientDto> varientDtos){
        log.info("Into [VarientService] [updateVarients] ");

        Map<Long ,VarientDto > varientDtoMap = convertListOfVarientDtosToMap(varientDtos);

        List<Varient> varients =
                varientRepository.findByIds(varientDtoMap.keySet());

        for(Varient varient : varients){

            if(varientDtoMap.containsKey(varient.getId())){
                VarientDto varientDto = varientDtoMap.get(varient.getId());
                varientMapper.update(varient , varientDto);
            }

        }

        varients = varientRepository.saveAll(varients);

        log.info("Exit [VarientService] [updateVarients] ");

        return varientMapper.map(varients);
    }

    private Map<Long ,VarientDto > convertListOfVarientDtosToMap(List<VarientDto> varientDtos){
        log.info("Into [VarientService] [convertListOfVarientDtosToMap] ");

        Map<Long ,VarientDto > map = varientDtos.stream().collect(Collectors.toMap(
                key -> key.getId(),
                value -> value,
                (key , value) -> value
        ));

        log.info("Exit [VarientService] [convertListOfVarientDtosToMap] ");

        return map;
    }
}

