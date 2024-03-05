package com.mctoluene.locationservice.domains.enums;

import java.util.Optional;

public enum Sorting {
    ASC("ASC"), DESC("DESC");

    Sorting(String name) {
        this.name = name;
    }

    private final String name;

    public static Optional<Sorting> getSort(String name) {
        for (Sorting v : values())
            if (v.name.equalsIgnoreCase(name))
                return Optional.of(v);
        return Optional.empty();
    }
}
