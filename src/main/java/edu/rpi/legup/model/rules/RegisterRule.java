package edu.rpi.legup.model.rules;

import java.lang.annotation.*;

/**
 * Annotation to register a class as a rule in the system.
 * This annotation can be applied to rule classes to indicate that they should be registered
 * in the rule system.
 */
@Inherited
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface RegisterRule {}
