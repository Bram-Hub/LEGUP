package edu.rpi.legup.model.elements;

import java.lang.annotation.*;
import java.lang.annotation.ElementType;

/**
 * Annotation to mark classes as elements that should be registered. This annotation is used to
 * indicate that a class is an element within the system and should be registered for use within the
 * application.
 */
@Inherited
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface RegisterElement {}
