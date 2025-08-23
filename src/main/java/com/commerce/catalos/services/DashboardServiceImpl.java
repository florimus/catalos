package com.commerce.catalos.services;

import com.commerce.catalos.core.configurations.Logger;
import com.commerce.catalos.core.utils.TimeUtils;
import com.commerce.catalos.helpers.DashboardHelper;
import com.commerce.catalos.models.dashboard.DashboardResponse;
import com.commerce.catalos.persistence.dtos.Dashboard;
import com.commerce.catalos.persistence.repositories.DashboardRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;

@Service
@RequiredArgsConstructor
public class DashboardServiceImpl implements DashboardService {

    private final DashboardRepository dashboardRepository;

    private Dashboard getDashboardConfigByYear(final Integer year) {
        return this.dashboardRepository.findDashboardByYearAndActiveAndEnabled(year, true, true);
    }

    @Override
    public DashboardResponse getDashboardInfo() {
        Integer currentYear = TimeUtils.getCurrentYear();
        Logger.info("46581a6a-6329-49ff-b139-ff0c2a1ab422", "fetching dashboard info by year", currentYear);
        return DashboardHelper.toDashboardResponseFromDashboard(this.getDashboardConfigByYear(currentYear));
    }

    @Async
    @Override
    public void createNewUser() {
        CompletableFuture.runAsync(() -> {
            Integer currentYear = TimeUtils.getCurrentYear();
            Dashboard dashboard = this.getDashboardConfigByYear(currentYear);
            dashboard.setNewCustomersCount(dashboard.getNewCustomersCount() + 1);
            Logger.info("", "updating user count by one for this month");
            this.dashboardRepository.save(dashboard);
        });
    }

    @Async
    @Override
    public void createNewOrder() {
        CompletableFuture.runAsync(() -> {
            Integer currentYear = TimeUtils.getCurrentYear();
            Dashboard dashboard = this.getDashboardConfigByYear(currentYear);
            dashboard.setNewOrdersCount(dashboard.getNewOrdersCount() + 1);
            Logger.info("", "updating orders count by one for this month");
            this.dashboardRepository.save(dashboard);
        });
    }
}
