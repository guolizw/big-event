package com.zw.anno;

//自定义注解

import com.zw.validation.StateValidation;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;


@Target(FIELD)
@Retention(RUNTIME)

@Documented //该注解可以归纳到帮助文档里
@Constraint(
        validatedBy = {StateValidation.class}
)
public @interface State {
    //提供State注解失败的提示信息
    String message() default "只能是已发布或者是草稿";

    //指定分组
    Class<?>[] groups() default {};

    //负载 获取到State注解的附加信息

    Class<? extends Payload>[] payload() default {};
}
