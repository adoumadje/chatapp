package com.adoumadje.chatapp.common.exception;

public class ResourceAlreadyExistsException extends RuntimeException {
    public ResourceAlreadyExistsException(String resourceName, String fieldName, String valueName) {
        super(String.format("%s already exits for the given data [%s : %S]", resourceName, fieldName,
                valueName));
    }
}
