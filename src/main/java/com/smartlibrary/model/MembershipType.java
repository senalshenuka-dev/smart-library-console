package com.smartlibrary.model;

/**
 * Membership types that affect borrowing limits and fines.
 */
public enum MembershipType {
    STUDENT,
    FACULTY,
    GUEST;

    public int borrowLimit() {
        return switch (this) {
            case STUDENT -> 5;
            case FACULTY -> 10;
            case GUEST -> 2;
        };
    }

    public int dueDays() {
        return switch (this) {
            case STUDENT -> 14;
            case FACULTY -> 30;
            case GUEST -> 7;
        };
    }
}