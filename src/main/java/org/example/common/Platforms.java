package org.example.common;

/**
 * Описание Модулей Платформ
 */
public enum Platforms {

    FELLOW("fellow");

    private final String name;

    Platforms(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return name;
    }
}
