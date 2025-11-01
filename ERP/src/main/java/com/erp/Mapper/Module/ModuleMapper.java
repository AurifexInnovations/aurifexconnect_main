package com.erp.Mapper.Module;

import com.erp.Dto.Request.ModuleDto;
import com.erp.Model.Module;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Component
public class ModuleMapper {

    public Module map(ModuleDto moduleDto){
        return update( null,moduleDto);
    }

    public Module update(Module module, ModuleDto moduleDto){
        if(Objects.isNull(module)){
            module = new Module();
        }

        if(Objects.nonNull(moduleDto)){
            module.setName(moduleDto.getName());
            module.setDescription(moduleDto.getDescription());
        }

        return module;
    }

    public ModuleDto map(Module module){
        return update(null , module);
    }

    public ModuleDto update(ModuleDto moduleDto , Module module){

        if(Objects.isNull(moduleDto)){
            moduleDto = new ModuleDto();
        }

        if(Objects.nonNull(module)){
            moduleDto.setId(module.getId());
            moduleDto.setName(module.getName());
            moduleDto.setDescription(module.getDescription());
        }

        return moduleDto;
    }

    public List<ModuleDto> map(List<Module> modules){
        return update(null , modules);
    }

    public List<ModuleDto> update(List<ModuleDto> moduleDtos , List<Module> modules){
        if(Objects.isNull(moduleDtos)){
            moduleDtos = new ArrayList<>();
        }

        if(Objects.nonNull(modules)){

            for (Module module : modules){
                ModuleDto moduleDto = map(module);
                moduleDtos.add(moduleDto);
            }
        }

        return moduleDtos;
    }
}
