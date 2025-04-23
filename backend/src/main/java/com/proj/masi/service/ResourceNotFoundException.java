package com.proj.masi.service;

public class ResourceNotFoundException extends RuntimeException {
    public ResourceNotFoundException(String entity, Object key) {
        super(entity + " not found with id = " + key);
    }
}
