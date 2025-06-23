package com.commerce.catalos.services;

import java.util.Date;

import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.commerce.catalos.core.configurations.Logger;
import com.commerce.catalos.core.configurations.Page;
import com.commerce.catalos.core.enums.DefaultRoles;
import com.commerce.catalos.core.errors.ConflictException;
import com.commerce.catalos.core.errors.NotFoundException;
import com.commerce.catalos.core.utils.RoleUtils;
import com.commerce.catalos.helpers.RoleHelper;
import com.commerce.catalos.models.roles.CreateRoleRequest;
import com.commerce.catalos.models.roles.RoleResponse;
import com.commerce.catalos.models.roles.RoleStatusUpdateResponse;
import com.commerce.catalos.models.roles.UpdateRoleRequest;
import com.commerce.catalos.persistence.dtos.Role;
import com.commerce.catalos.persistence.repositories.RoleRepository;
import com.commerce.catalos.security.AuthContext;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class RoleServiceImpl implements RoleService {

    private final RoleRepository roleRepository;

    private final AuthContext authContext;

    private Role findRoleByUniqueId(final String uniqueId) {
        return this.roleRepository.findByUniqueIdAndEnabled(uniqueId, true);
    }

    private Role findRoleById(final String id) {
        return this.roleRepository.findByIdAndEnabled(id, true);
    }

    /**
     * This method returns the permission of the role specified by the given
     * uniqueId.
     * If the uniqueId is null or is equal to DefaultRoles.Customer.name(), the
     * method
     * will return "CUS:ALL" as the permission.
     * Otherwise, it will query the database for the role with the given uniqueId
     * and
     * return its permissions. If the role is not found, the method will return
     * "CUS:ALL" as the permission.
     * 
     * @param uniqueId the unique id of the role
     * @return the permission of the role
     */
    @Override
    public String getRoleById(final String uniqueId) {
        if (uniqueId == null || DefaultRoles.Customer.name().equals(uniqueId)) {
            Logger.info("76ca50e1-df83-4197-b3fb-499a6a7ca2a5", "Customer role requested with uniqueId: {}", uniqueId);
            return "CUS:ALL";
        }
        Role role = this.findRoleByUniqueId(uniqueId);
        if (null == role || !role.isActive()) {
            role = Role.builder().permissions("CUS:ALL").build();
        }
        return role.getPermissions();
    }

    @Override
    public Page<RoleResponse> listRoles(String query, Pageable pageable) {
        Logger.info("8952fefd-8935-4b1d-a1e2-21ec90d34658", "Finding roles with query: {} and pageable: {}",
                query, pageable);
        Page<Role> roles = roleRepository.searchRoles(query, pageable);
        return new Page<RoleResponse>(
                RoleHelper.toRoleListResponseFromRoles(roles.getHits()),
                roles.getTotalHitsCount(),
                roles.getCurrentPage(),
                roles.getPageSize());
    }

    @Override
    public RoleResponse getRoleByUniqueId(final String uniqueId) {
        Role role = this.findRoleByUniqueId(uniqueId);
        if (null == role) {
            Logger.error("e935af8e-2f37-4585-8eba-199ec8c5e4e4", "Role not found with uniqueId: {}", uniqueId);
            throw new NotFoundException("Role not found");
        }
        return RoleHelper.toRoleResponseFromRole(role);

    }

    @Override
    public RoleResponse createRole(final CreateRoleRequest createRoleRequest) {
        if (null != this.findRoleByUniqueId(createRoleRequest.getUniqueId())) {
            Logger.error("f806821d-000a-46aa-b254-b1cdb609559c", "Role already exists with uniqueId: {}",
                    createRoleRequest.getUniqueId());
            throw new ConflictException("Role already exists");
        }
        String permissions = RoleUtils.generatePermissionStringFromMap(createRoleRequest.getPermissionList());
        Role role = RoleHelper.toRoleFromCreateRoleRequest(createRoleRequest);
        role.setPermissions(permissions);
        role.setDefault(false);
        role.setActive(true);
        role.setEnabled(true);
        role.setCreatedAt(new Date());
        role.setCreatedBy(authContext.getCurrentUser().getEmail());
        Logger.info("7d5e7e1c-4a6d-4f5f-9e7c-3d6f4c5e3d2c", "Role creating with uniqueId: {}",
                createRoleRequest.getUniqueId());
        return RoleHelper.toRoleResponseFromRole(this.roleRepository.save(role));
    }

    @Override
    public RoleResponse updateRole(final String uniqueId, final UpdateRoleRequest updateRoleRequest) {
        Role role = this.findRoleByUniqueId(uniqueId);
        if (null == role) {
            Logger.error("e373cc35-216b-4196-afd7-a490342e97b5", "Role not found with uniqueId: {}", uniqueId);
            throw new NotFoundException("Role not found");
        }
        if (role.isDefault()) {
            Logger.warn("cf39eaee-c1e6-4912-9959-027cb3ca484c", "Cannot update default role with uniqueId: {}",
                    uniqueId);
            throw new ConflictException("Cannot update default role");
        }
        String permissions = RoleUtils.generatePermissionStringFromMap(updateRoleRequest.getPermissionList());
        role.setName(updateRoleRequest.getName());
        role.setDescription(updateRoleRequest.getDescription());
        role.setPermissionList(updateRoleRequest.getPermissionList());
        role.setPermissions(permissions);
        role.setUpdatedAt(new Date());
        role.setUpdatedBy(authContext.getCurrentUser().getEmail());
        Logger.info("01c8031e-0949-4245-a8c7-161f42bb71c3", "Role updating with uniqueId: {}", uniqueId);
        return RoleHelper.toRoleResponseFromRole(this.roleRepository.save(role));
    }

    @Override
    public RoleStatusUpdateResponse updateRoleStatus(final String id, final boolean status) {
        Role role = this.findRoleById(id);
        if (null == role) {
            Logger.error("d9db38e1-5364-46d5-9f3d-89fecc90b876", "Role not found with id: {}", id);
            throw new NotFoundException("Role not found");
        }
        if (role.isDefault()) {
            Logger.warn("df685ef0-0168-433a-94e8-c00740142e7a", "Cannot update default role with uniqueId: {}",
                    role.getUniqueId());
            throw new ConflictException("Cannot update default role");
        }
        role.setActive(status);
        role.setUpdatedAt(new Date());
        role.setUpdatedBy(authContext.getCurrentUser().getEmail());
        Logger.info("606c8e99-1c75-4f47-9248-3775b26e3b4a", "Role updating with uniqueId: {}", role.getUniqueId());
        role = this.roleRepository.save(role);
        return RoleStatusUpdateResponse.builder().status(role.isActive())
                .message(role.isActive() ? "Role Activated" : "Role Deactivated").build();
    }

}
