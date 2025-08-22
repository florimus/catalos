package com.commerce.catalos.helpers;

import com.commerce.catalos.models.dashboard.DashboardResponse;
import com.commerce.catalos.persistence.dtos.Dashboard;
import org.springframework.beans.BeanUtils;

public class DashboardHelper {

    public static DashboardResponse toDashboardResponseFromDashboard(final Dashboard dashboard) {
        DashboardResponse response = new DashboardResponse();
        BeanUtils.copyProperties(dashboard, response);
        return response;
    }
}
