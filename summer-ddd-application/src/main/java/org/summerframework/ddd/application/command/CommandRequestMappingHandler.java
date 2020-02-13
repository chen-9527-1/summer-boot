package org.summerframework.ddd.application.command;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.util.StringUtils;
import org.summerframework.ddd.core.command.Command;
import org.summerframework.ddd.core.command.annotation.CommandService;

public class CommandRequestMappingHandler {

    private final static Map<String, String> COMMAND_MAP = new HashMap<>(128);

    public final static String tryMapping(Method method, Class<?> clazz) {
        CommandService annotation = AnnotationUtils.getAnnotation(method, CommandService.class);
        if (annotation == null) {
            return null;
        }
        String handler = clazz.getName() + "." + method.getName();
        Class<?>[] parameterTypes = method.getParameterTypes();
        if (parameterTypes.length == 0) {
            throw new RuntimeException(handler + "没有配置命令参数");
        }
        if (!Command.class.isAssignableFrom(parameterTypes[0])) {
            throw new RuntimeException(handler + "第一个参数不是命令参数");
        }
        String name = StringUtils.isEmpty(annotation.name()) ? parameterTypes[0].getSimpleName() : annotation.name();
        String existedHandler = COMMAND_MAP.get(name);
        if (existedHandler == null) {
            COMMAND_MAP.put(name, handler);
        } else {
            throw new RuntimeException("命令 " + name + " 有多个执行者: " + existedHandler + " , " + handler);
        }
        String version = StringUtils.isEmpty(annotation.version()) ? "v1" : annotation.version();
        return "/command/" + name + "/" + version;
    }

}