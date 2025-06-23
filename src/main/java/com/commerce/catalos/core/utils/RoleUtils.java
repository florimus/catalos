package com.commerce.catalos.core.utils;

import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import com.commerce.catalos.core.constants.RoleConstants;

public class RoleUtils {

    public static Map<String, List<String>> populatePermissionListFromPermissions(final String permissions) {
        // Using a set to avoid duplicate permissions for each role
        Map<String, Set<String>> permissionMap = new HashMap<>();

        // Split the permissions string into individual permission entries
        String[] permissionEntries = permissions.split(",");

        for (String entry : permissionEntries) {
            // Extract the role prefix (e.g., "USR") and permission code (e.g., ":NN")
            String roleKey = entry.substring(0, 3);
            String permissionCode = entry.substring(3);

            // Get the readable role name and permission label
            String roleName = RoleConstants.ROLE_KEYS.get(roleKey);
            String permissionLabel = RoleConstants.PERMISSION_CODES.get(permissionCode);

            // Only proceed if both mappings exist
            if (roleName != null && permissionLabel != null) {
                // Add permission to the role's permission list
                permissionMap
                        .computeIfAbsent(roleName, key -> new HashSet<>())
                        .add(permissionLabel);
            }
        }

        // Convert the set of permissions into a sorted list for each role
        return permissionMap.entrySet()
                .stream()
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        entry -> entry.getValue()
                                .stream()
                                .sorted()
                                .collect(Collectors.toList()),
                        (existing, replacement) -> replacement,
                        LinkedHashMap::new // preserve insertion order
                ));
    }

}
