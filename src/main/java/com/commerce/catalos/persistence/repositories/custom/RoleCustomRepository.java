package com.commerce.catalos.persistence.repositories.custom;

import org.springframework.data.domain.Pageable;

import com.commerce.catalos.core.configurations.Page;
import com.commerce.catalos.persistence.dtos.Role;

public interface RoleCustomRepository {

    Page<Role> searchRoles(final String search, final Pageable pageable);
}
