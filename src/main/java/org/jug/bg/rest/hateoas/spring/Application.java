/*
 * Some rights reserved. This work is licensed under a Creative Commons License, BY, Version 4.0
 * 2014, Bulgarian Java Users Group
 */
package org.jug.bg.rest.hateoas.spring;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.hateoas.config.EnableEntityLinks;
import org.springframework.hateoas.config.EnableHypermediaSupport;

/**
 * Spring Boot configuration class.
 */
@Configuration
@ComponentScan
@EnableAutoConfiguration
//
// Spring HATEOAS annotations:
//
@EnableEntityLinks /* enables EntityLinks's usage */
@EnableHypermediaSupport(type = {EnableHypermediaSupport.HypermediaType.HAL})
//
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}
