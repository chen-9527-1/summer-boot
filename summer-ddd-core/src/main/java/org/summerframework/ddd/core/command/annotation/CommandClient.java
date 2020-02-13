package org.summerframework.ddd.core.command.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 命令客户端参数
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.TYPE })
public @interface CommandClient {

    /**
     * 命令的名称(默认是类名)
     */
    String name() default "";

    /**
     * 命令的版本(默认是v1)
     */
    String version() default "";

    /**
     * 命令的微服务名
     */
    String serviceName();

}
