package com.commerce.catalos.services;

import com.commerce.catalos.models.dashboard.DashboardResponse;

public interface DashboardService {

    DashboardResponse getDashboardInfo();

    void createNewUser();

}
