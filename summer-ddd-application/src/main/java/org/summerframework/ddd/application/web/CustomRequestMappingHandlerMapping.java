package org.summerframework.ddd.application.web;

import java.lang.reflect.Method;

import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;
import org.summerframework.ddd.application.command.CommandRequestMappingHandler;

public class CustomRequestMappingHandlerMapping extends RequestMappingHandlerMapping {

    @Override
    protected RequestMappingInfo getMappingForMethod(Method method, Class<?> handlerType) {
        String path = null;
        path = CommandRequestMappingHandler.tryMapping(method, handlerType);

        RequestMappingInfo info;
        if (path == null) {
            info = super.getMappingForMethod(method, handlerType);
        } else {
            info = RequestMappingInfo.paths(path).methods(RequestMethod.POST).build();
        }
        return info;
    }

}
