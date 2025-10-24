package com.erp.Service.Role;

import com.erp.Dto.Request.RoleListRequest;
import com.erp.Dto.Request.RoleRequest;
import com.erp.Dto.Response.RoleResponse;
import com.erp.Exception.ResourceNotFoundException;
import com.erp.Mapper.Role.RoleMapper;
import com.erp.Model.Role;
import com.erp.Repository.Role.RoleRepository;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class RoleServiceImpl implements RoleServices{
    private final RoleRepository roleRepository;
    private final RoleMapper roleMapper;

    @Transactional
    public List<RoleResponse> createRoles(RoleListRequest request) {
        List<Role> roles = request.getRoles().stream()
                .map(roleRequest -> {
                    String roleNameUpper = roleRequest.getRoleName().toUpperCase(); // Convert to uppercase
                    return roleRepository.findByRoleName(roleNameUpper)
                            .orElseGet(() -> {
                                Role newRole = roleMapper.toRole(roleRequest);
                                newRole.setRoleName(roleNameUpper); // Set uppercase name before saving
                                return roleRepository.save(newRole);
                            });
                })
                .toList();
        return roleMapper.toRoleResponseList(roles);
    }

    @Override
    public List<RoleResponse> getAllRoles() {
        List<Role> roles = roleRepository.findAll();
        return roleMapper.toRoleResponseList(roles);
    }

    @Override
    public RoleResponse getRoleByRoleId(long roleId) {
        Optional<Role> role = roleRepository.findById(roleId);

        if(role.isEmpty()){
            throw new ResourceNotFoundException("Role not found with this " + role + " role id");
        }
        return new RoleResponse(role.get().getRoleId() , role.get().getRoleName());
    }


}
