package com.erp.Mapper.Branch;

import com.erp.Dto.Request.BranchRequest;
import com.erp.Dto.Response.BranchResponse;
import com.erp.Dto.Response.BranchResponseId;
import com.erp.Model.Branch;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.springframework.stereotype.Component;

import java.util.List;

@Mapper(componentModel = "spring")
@Component
public interface BranchMapper {

    Branch mapToBranch(BranchRequest branchRequest);

    void mapToBranchEntity(BranchRequest branchRequest, @MappingTarget Branch branch);

    BranchResponse mapToBranchResponse(Branch branch);

    @Mapping(target = "editedBy", ignore = true)
    void mapToBranchEntityForUpdate(BranchRequest branchRequest, @MappingTarget Branch branch);

    List<BranchResponse> mapToBranchResponse(List<Branch> branchList);

    List<BranchResponse> mapListToBranchResponse(List<Branch> list);

}
