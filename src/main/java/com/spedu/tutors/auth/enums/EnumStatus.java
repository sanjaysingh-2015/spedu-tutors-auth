package com.spedu.tutors.auth.enums;

public enum EnumStatus {
    ACTIVE("Active"),
    INACTIVE("Inactive"),
    BLOCKED("Blocked"),
    DRAFT("Draft"),
    PUBLISHED("Published"),
    ARCHIVED("Archived"),
    FAILED("Failed"),
    REFUNDED("Refunded"),
    DELETED("Deleted");

    private final String label;

    EnumStatus(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }
}
