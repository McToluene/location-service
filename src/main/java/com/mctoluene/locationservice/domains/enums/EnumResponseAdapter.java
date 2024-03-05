package com.mctoluene.locationservice.domains.enums;

import java.util.Optional;

public enum EnumResponseAdapter {
    ACTIVE("ACTIVE", "ENABLED"),
    INACTIVE("INACTIVE", "DISABLED");

    private final String name;
    private final String enabledDisabled;

    EnumResponseAdapter(String name, String enabledDisabled) {
        this.name = name;
        this.enabledDisabled = enabledDisabled;
    }

    public static Optional<EnumResponseAdapter> getStatus(String name) {
        for (EnumResponseAdapter v : values()) {
            if (v.name.equalsIgnoreCase(name)) {
                return Optional.of(v);
            }
        }
        return Optional.empty();
    }

    public String toEnabledDisabled() {
        return enabledDisabled;
    }
}
