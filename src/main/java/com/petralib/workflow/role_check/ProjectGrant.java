package com.petralib.workflow.role_check;

import com.petralib.auth.Role;
import com.petralib.auth.UserAction;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface ProjectGrant {
    UserAction userAction();
}
