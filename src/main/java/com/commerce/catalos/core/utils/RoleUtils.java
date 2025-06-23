package com.commerce.catalos.core.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import com.commerce.catalos.core.constants.RoleConstants;

public class RoleUtils {

    public static Map<String, List<String>> populatePermissionListFromPermissionString(final String permissions) {
        Map<String, Set<String>> permissionMap = new HashMap<>();

        String[] permissionEntries = permissions.split(",");

        for (String entry : permissionEntries) {
            String roleKey = entry.substring(0, 3);
            String permissionCode = entry.substring(3);

            String roleName = RoleConstants.ROLE_KEYS.get(roleKey);
            String permissionLabel = RoleConstants.PERMISSION_CODES.get(permissionCode);

            if (roleName != null && permissionLabel != null) {
                permissionMap
                        .computeIfAbsent(roleName, key -> new HashSet<>())
                        .add(permissionLabel);
            }
        }

        return permissionMap.entrySet()
                .stream()
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        entry -> entry.getValue()
                                .stream()
                                .sorted()
                                .collect(Collectors.toList()),
                        (existing, replacement) -> replacement,
                        LinkedHashMap::new));
    }

    public static String generatePermissionStringFromMap(Map<String, List<String>> rolePermissions) {
        List<String> permissionEntries = new ArrayList<>();

        for (Map.Entry<String, List<String>> entry : rolePermissions.entrySet()) {
            String roleName = entry.getKey();
            List<String> permissions = entry.getValue();

            String rolePrefix = RoleConstants.ROLE_KEYS.entrySet().stream()
                    .filter(e -> e.getValue().equals(roleName))
                    .map(Map.Entry::getKey)
                    .findFirst()
                    .orElse(null);

            if (rolePrefix == null) {
                continue;
            }

            for (String permission : permissions) {
                String permissionSuffix = RoleConstants.PERMISSION_CODES.entrySet().stream()
                        .filter(e -> e.getValue().equalsIgnoreCase(permission))
                        .map(Map.Entry::getKey)
                        .findFirst()
                        .orElse(null);

                if (permissionSuffix != null) {
                    permissionEntries.add(rolePrefix + permissionSuffix);
                }
            }
        }

        return String.join(",", permissionEntries);
    }

}
