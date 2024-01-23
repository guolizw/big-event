package com.zw.validation;

import com.zw.anno.State;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

//ConstraintValidator<给哪个注解提供校验规则,校验的数据类型>

public class StateValidation implements ConstraintValidator<State,String> {


    @Override
    public boolean isValid(String s, ConstraintValidatorContext constraintValidatorContext) {
        if(s==null)
            return false;
        if(s.equals("已发布")||s.equals("草稿"))
            return true;

        return false;
    }
}
