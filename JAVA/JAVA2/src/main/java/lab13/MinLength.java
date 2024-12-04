package lab13;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.annotation.Repeatable;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
@interface MinLength {
    int min() default 3;
}

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
@Repeatable(CustomValidationList.class) // 指定容器注解
@interface CustomValidation {
    Rule rule();
}

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
@interface CustomValidationList {
    CustomValidation[] value();
}
enum Rule {
    ALL_LOWERCASE,
    NO_USERNAME,
    HAS_BOTH_DIGITS_AND_LETTERS
}