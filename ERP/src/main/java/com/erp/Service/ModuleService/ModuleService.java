package com.erp.Service.ModuleService;

import com.erp.Dto.Request.ModuleDto;
import com.erp.Exception.ResourceFoundException;
import com.erp.Exception.ResourceNotFoundException;
import com.erp.Mapper.Module.ModuleMapper;
import com.erp.Model.GenericUser;
import com.erp.Model.Module;
import com.erp.Repository.Module.ModuleRepository;
import com.erp.Security.util.UserIdentity;
import com.erp.Utility.ObjectMapperUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
@Slf4j
public class ModuleService {

    private final ModuleRepository moduleRepository;
    private final ModuleMapper moduleMapper;
    private final UserIdentity userIdentity;

    public ModuleDto addModule(ModuleDto moduleDto){
        log.info("Into [ModuleService] [addModule]");

        log.info("[ModuleService] [addModule] :: Request :: {} " ,
                ObjectMapperUtils.writeValueAsString(moduleDto));

        validate(moduleDto.getName());

        Module module = moduleMapper.map(moduleDto);

        module.setCreatedAt(LocalDateTime.now());

        GenericUser user =  userIdentity.getCurrentUser();
        module.setCreatedBy(user.getId());
        module.setActive(true);

        module = moduleRepository.save(module);

        log.info("Exit [ModuleService] [addModule]");

        return moduleMapper.map(module);
    }

    private void validate(String name){
        log.info("Into [ModuleService] [validate]");

        log.info("[ModuleService] [validate] :: name :: {} " , name);

        Module module =
                moduleRepository.findByName(name);

        if(Objects.nonNull(module)){
            throw new ResourceFoundException("Already module with this name is present");
        }

        log.info("Exit [ModuleService] [validate]");
    }

    public Module getModule(long moduleId){
        log.info("Into [ModuleService] [getModuleById]");

        log.info("[ModuleService] [getModuleById] :: moduleId :: {} " , moduleId);

        Module module =
                moduleRepository.findById(moduleId);

        if(Objects.isNull(module)){
            throw new ResourceNotFoundException("Module Is not found please check");
        }

        log.info("Exit [ModuleService] [getModuleById]");

        return module;
    }

    public ModuleDto updateModule(long moduleId , ModuleDto moduleDto){
        log.info("Into [ModuleService] [updateModule]");

        log.info("[ModuleService] [updateModule] :: moduleId :: {} :: Request :: {} " , moduleId ,
                ObjectMapperUtils.writeValueAsString(moduleDto));

        Module module = getModule(moduleId);

        moduleMapper.update(module , moduleDto);

        module = moduleRepository.save(module);

        log.info("Exit [ModuleService] [updateModule]");

        return  moduleMapper.map(module);
    }

    public List<ModuleDto> getModules(){
        log.info("Into [ModuleService] [getModules]");

        List<Module> modules =
                moduleRepository.getAllModules();

        log.info("Exit [ModuleService] [getModules]");

        return moduleMapper.map(modules);
    }

    public ModuleDto deleteModule(long moduleId){
        log.info("Into [ModuleService] [getModules]");

        log.info("[ModuleService] [getModules] :: moduleId :: {}" , moduleId);

        Module module = getModule(moduleId);
        module.setActive(false);

        module = moduleRepository.save(module);

        log.info("Exit [ModuleService] [getModules]");

        return moduleMapper.map(module);
    }

    public ModuleDto getModuleByModuleId(long moduleId){
        log.info("Into [ModuleService] [getModuleByModuleId]");

        log.info("Into [ModuleService] [getModuleByModuleId] :: moduleId :: {} " , moduleId);

        Module module = getModule(moduleId);

        log.info("Exit [ModuleService] [getModuleByModuleId]");

        return moduleMapper.map(module);
    }
}
