/*
 * Some rights reserved. This work is licensed under a Creative Commons License, BY, Version 4.0
 * 2014, Bulgarian Java Users Group
 */
package org.jug.bg.rest.hateoas.spring.common.resource;


import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Abstract resource class which encapsulates shared behaviour for the resources, e.g. error handling etc.
 *
 * @author Nikolay Vasilev
 */
public abstract class AbstractResource {

    // No constant representing HAL's response type was found in Spring HATEOAS MediaType class,
    // hence a constant must had been defined.
    protected static final String APPLICATION_HAL_JSON = "application/hal+json";

    /**
     * Handles resource not found situation.
     *
     * @param notFoundException Not found error.
     */
    @ExceptionHandler(NotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public void handleNotFoundException(NotFoundException notFoundException) {
        handleError(notFoundException, "Handling resource NOT FOUND error.");
    }

    /**
     * Handles bad request exception.
     *
     * @param badRequestException Bad request exception.
     */
    @ExceptionHandler()
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public void handle(BadRequestException badRequestException) {
        handleError(badRequestException, "Handling BAD REQUEST exception.");
    }

    /**
     * Handles internal server error.
     *
     * @param runtimeException Unspecified exception.
     */
    @ExceptionHandler(RuntimeException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public void handle(RuntimeException runtimeException) {
        handleError(runtimeException, "Handling unspecified exception.");
    }


    /**
     * Auxiliary method for handling exceptions.
     *
     * @param exception Exception to be handled.
     * @param errMsg Error message.
     */
    private void handleError(RuntimeException exception, String errMsg) {
        System.err.println(errMsg);
        exception.printStackTrace(System.err);
    }
}
