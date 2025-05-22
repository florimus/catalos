package com.commerce.catalos.persistances.repositories.custom;

import org.springframework.data.domain.Pageable;

import com.commerce.catalos.core.configurations.Page;
import com.commerce.catalos.persistances.dtos.User;

public interface UserCustomRepository {

    Page<User> searchUsers(String search, Pageable pageable);
}
