package com.software.teamfive.sitepackageapi.util;

public abstract class ApiModel<T> {

    /**
     * Implement this method to handle a 'not found' message
     * @return Info on what 'not found' means in this context
     */
    public abstract T notFound();

}
