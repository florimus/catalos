package com.commerce.catalos.core.constants;

import java.util.Map;

public class RoleConstants {

    public static final Map<String, String> ROLE_KEYS = Map.ofEntries(
            Map.entry("USR", "User"),
            Map.entry("PTY", "ProductType"),
            Map.entry("PRD", "Product"),
            Map.entry("VAR", "Variant"),
            Map.entry("STK", "Stock"),
            Map.entry("PRZ", "Prize"),
            Map.entry("PRO", "Promotion"),
            Map.entry("CAT", "Category"),
            Map.entry("BRD", "Brand"),
            Map.entry("MOD", "Modules"),
            Map.entry("ROL", "Roles"),
            Map.entry("TAX", "Taxes"));

    public static final Map<String, String> PERMISSION_CODES = Map.of(
            ":NN", "EDIT",
            ":LS", "READ",
            ":RM", "DELETE");
}
