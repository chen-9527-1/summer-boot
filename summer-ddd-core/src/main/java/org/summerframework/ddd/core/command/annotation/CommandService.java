package org.summerframework.ddd.core.command.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 命令服务参数(基础注解，需要与其他注解组合使用)
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.METHOD, ElementType.ANNOTATION_TYPE })
public @interface CommandService {

    /**
     * 命令的名称(默认是类名)
     */
    String name() default "";

    /**
     * 命令的版本(默认是v1)
     */
    String version() default "";

}
