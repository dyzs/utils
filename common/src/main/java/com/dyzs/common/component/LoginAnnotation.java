package com.dyzs.common.component;

public @interface LoginAnnotation {
    String username() default "dyzs";
    String password() default "123456";
}
