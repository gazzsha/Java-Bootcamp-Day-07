package com.school21.annotations;

import java.lang.annotation.*;

@Inherited
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface HtmlInput {
    String type() default "text";
    String name();
    String placeholder();
}
