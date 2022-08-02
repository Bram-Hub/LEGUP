package edu.rpi.legup.model.elements;

import java.lang.annotation.*;
import java.lang.annotation.ElementType;

@Inherited
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface RegisterElement {

}
