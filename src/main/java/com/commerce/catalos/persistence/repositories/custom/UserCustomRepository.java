package com.commerce.catalos.persistence.repositories.custom;

import org.springframework.data.domain.Pageable;

import com.commerce.catalos.core.configurations.Page;
import com.commerce.catalos.persistence.dtos.User;

public interface UserCustomRepository {

    Page<User> searchUsers(String search, final String role, Pageable pageable);
}
