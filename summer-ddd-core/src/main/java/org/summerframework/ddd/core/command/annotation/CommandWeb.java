package org.summerframework.ddd.core.command.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.springframework.web.bind.annotation.RequestMapping;

/**
 * 命令服务参数(通过SpringWeb提供服务)
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.METHOD })
@RequestMapping
@CommandService
public @interface CommandWeb {

}
