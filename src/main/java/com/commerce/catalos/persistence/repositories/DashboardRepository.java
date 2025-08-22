package com.commerce.catalos.persistence.repositories;

import com.commerce.catalos.persistence.dtos.Dashboard;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface DashboardRepository extends MongoRepository<Dashboard, String> {

    Dashboard findDashboardByYearAndActiveAndEnabled(final Integer year, final boolean active, final boolean enabled);

}
