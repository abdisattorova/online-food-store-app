package com.online.foodstore.utils;

public interface ErrorMessages {

    /* COMMON */
    String NOT_FOUND = "%s not found";
    String USER_DELETION_CONSTRAINT = "You cannot delete yourself.";
    String USER_MODIFICATION_CONSTRAINT = "You cannot modify yourself.";

    /* VALIDATION ERRORS */
    String SHOULDNT_BE_NULL = " should not be null";
    String SHOULDNT_BE_EMPTY = " should not be empty";
    String SHOULD_NOT_BE_NEGATIVE = " should not be negative number";

    /* AUTH RELATED */
    String LOGIN_SHOULD_BE_UNIQUE = "Username should be unique";
    String LOGIN_PW_ERROR = "Username or password is incorrect";
    String JWT_EXPIRED = "Token expired";


    static String notFound(String className) {
        return String.format(NOT_FOUND, className);
    }
}
