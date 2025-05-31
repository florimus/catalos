package com.commerce.catalos.services;

import org.springframework.stereotype.Service;

import com.commerce.catalos.core.configurations.Logger;
import com.commerce.catalos.core.enums.DefaultRoles;
import com.commerce.catalos.persistence.dtos.Role;
import com.commerce.catalos.persistence.repositories.RoleRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class RoleServiceImpl implements RoleService {

    private final RoleRepository roleRepository;

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
        Role role = roleRepository.findByUniqueId(uniqueId)
                .orElse(Role.builder().permissions("CUS:ALL").build());
        return role.getPermissions();
    }

}
