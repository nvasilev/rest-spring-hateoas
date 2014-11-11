/*
 * Some rights reserved. This work is licensed under a Creative Commons License, BY, Version 4.0
 * 2014, Bulgarian Java Users Group
 */
package org.jug.bg.rest.hateoas.spring.common;

/**
 * Builders interface for parametrized type <code>T</code>.
 *
 * @param <T> Type of product which is to be produced by this builder.
 *
 * @author Nikolay Vasilev
 */
public interface Builder<T> {

    /**
     * Builds the product of type <code>T</code>.
     *
     * @return the product of type <code>T</code>.
     */
    T build();
}
