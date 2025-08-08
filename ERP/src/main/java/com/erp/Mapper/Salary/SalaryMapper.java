package com.erp.Mapper.Salary;

import com.erp.Dto.Request.SalaryRequest;
import com.erp.Dto.Response.SalaryResponse;
import com.erp.Model.Salary;
import com.erp.Mapper.User.UserMapper;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import java.util.List;

@Mapper(componentModel = "spring", uses = {UserMapper.class})
public interface SalaryMapper {

    @Mapping(source = "user", target = "user")
    @Mapping(source = "user.firstName", target = "firstName")
    @Mapping(source = "user.lastName", target = "lastName")
    SalaryResponse mapToResponse(Salary salary);

    List<SalaryResponse> mapToResponseList(List<Salary> salaries);

    @Mapping(target = "id", ignore = true)
    Salary mapToSalary(SalaryRequest request, @MappingTarget Salary salary);
}