package com.school21.annotations;


import java.lang.annotation.*;

@Inherited
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface HtmlForm {

    String fileName() default "user_form.html";
    String action() default "/user";
    String method() default "post";
}
