package com.adoumadje.chatapp.common.exception;

public class ResourceNotFoundException extends RuntimeException {
    public ResourceNotFoundException(String resourceName, String fieldName, String valueName) {
        super(String.format("%s not found with the given data [%s : %s]", resourceName,
                fieldName, valueName));
    }
}
