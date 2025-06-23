package com.commerce.catalos.services;

import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.commerce.catalos.core.configurations.Logger;
import com.commerce.catalos.core.configurations.Page;
import com.commerce.catalos.core.enums.DefaultRoles;
import com.commerce.catalos.core.errors.NotFoundException;
import com.commerce.catalos.helpers.RoleHelper;
import com.commerce.catalos.models.roles.RoleResponse;
import com.commerce.catalos.persistence.dtos.Role;
import com.commerce.catalos.persistence.repositories.RoleRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class RoleServiceImpl implements RoleService {

    private final RoleRepository roleRepository;

    private Role findRoleByUniqueId(final String uniqueId) {
        return this.roleRepository.findByUniqueIdAndEnabled(uniqueId, true);
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
        throw new UnsupportedOperationException("Unimplemented method 'listRoles'");
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

}
