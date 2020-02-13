package org.summerframework.ddd.core.command.standard;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.util.StringUtils;
import org.springframework.web.client.HttpMessageConverterExtractor;
import org.springframework.web.client.RequestCallback;
import org.springframework.web.client.RestTemplate;
import org.summerframework.ddd.core.command.Command;
import org.summerframework.ddd.core.command.CommandBus;
import org.summerframework.ddd.core.command.CommandContext;
import org.summerframework.ddd.core.command.CommandResult;
import org.summerframework.ddd.core.command.ObjectCommand;
import org.summerframework.ddd.core.command.annotation.CommandClient;

public class CloudCommandBus implements CommandBus {

    protected final RestTemplate restTemplate;
    protected final String gatewayUrl;
    protected final CommandContext commandContext;
    protected final HttpMessageConverterExtractor<String> responseExtractor;
    protected final ObjectMapper jsonMapper = new ObjectMapper();

    protected final Map<String, JavaType> typeMap = new ConcurrentHashMap<>(64);
    protected final Map<String, String> urlMap = new ConcurrentHashMap<>(64);

    public CloudCommandBus(RestTemplate restTemplate, CommandContext commandContext, String gatewayUrl) {
        this.restTemplate = restTemplate;
        this.commandContext = commandContext;
        this.gatewayUrl = gatewayUrl;

        List<HttpMessageConverter<?>> messageConverters = new ArrayList<>(1);
        messageConverters.add(new StringHttpMessageConverter(StandardCharsets.UTF_8));
        responseExtractor = new HttpMessageConverterExtractor<>(String.class, messageConverters);

        jsonMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    }

    @SuppressWarnings("unchecked")
    protected <T> JavaType getResultType(ObjectCommand<T> command) {
        Class<?> commandClass = command.getClass();
        String typeKey = commandClass.getName();
        JavaType resultType = typeMap.get(typeKey);
        if (resultType == null) {
            Type[] interfacesTypes = commandClass.getGenericInterfaces();
            for (Type ct : interfacesTypes) {
                if (ct instanceof ParameterizedType) {
                    ParameterizedType pt = (ParameterizedType) ct;
                    if (pt.getRawType().equals(ObjectCommand.class)) {
                        Class<T> clazz = (Class<T>) pt.getActualTypeArguments()[0];
                        resultType = jsonMapper.getTypeFactory().constructParametricType(CommandResult.class, clazz);
                        typeMap.put(typeKey, resultType);
                        break;
                    }
                }
            }
        }
        return resultType;
    }

    protected <T> JavaType getResultType(Command command) {
        String typeKey = command.getClass().getName();
        JavaType resultType = typeMap.get(typeKey);
        if (resultType == null) {
            resultType = jsonMapper.getTypeFactory().constructParametricType(CommandResult.class, Void.class);
            typeMap.put(typeKey, resultType);
        }
        return resultType;
    }

    protected String getCommandUrl(Command command) {
        Class<?> commandClass = command.getClass();
        String urlKey = commandClass.getName();
        String url = urlMap.get(urlKey);
        if (url == null) {
            CommandClient annotation = commandClass.getAnnotation(CommandClient.class);
            if (annotation == null) {
                throw new RuntimeException(urlKey + "需要配置CommandClient注解");
            }
            String commandName = annotation.name();
            if (StringUtils.isEmpty(commandName)) {
                commandName = command.getClass().getSimpleName();
            }
            String commandVersion = annotation.version();
            if (StringUtils.isEmpty(commandVersion)) {
                commandVersion = "v1";
            }
            url = getRequestUrl(annotation.serviceName(), commandName, commandVersion);
            urlMap.put(urlKey, url);
        }
        return url;
    }

    @Override
    public <T> CommandResult<T> execute(ObjectCommand<T> command) {
        JavaType resultType = getResultType(command);
        return doExecute(command, resultType);
    }

    @Override
    public CommandResult<Void> execute(Command command) {
        JavaType resultType = getResultType(command);
        return doExecute(command, resultType);
    }

    protected <T> CommandResult<T> doExecute(Command command, JavaType resultType) {
        try {
            HttpEntity<String> entity = new HttpEntity<>(jsonMapper.writeValueAsString(command), getRequestHeaders());
            RequestCallback requestCallback = restTemplate.httpEntityCallback(entity, String.class);
            String url = getCommandUrl(command);
            String resultString = restTemplate.execute(url, HttpMethod.POST, requestCallback, responseExtractor);
            return jsonMapper.readValue(resultString, resultType);
        } catch (Exception e) {
            try {
                return jsonMapper.convertValue(getErrorMap(e), resultType);
            } catch (Exception en) {
                en.printStackTrace();
                return null;
            }
        }
    }

    protected HttpHeaders getRequestHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        if (commandContext.getValues() != null) {
            headers.setAll(commandContext.getValues());
        }
        return headers;
    }

    protected String getRequestUrl(String serviceName, String commandName, String commandVersion) {
        return gatewayUrl + "/" + serviceName + "/command/" + commandName + "/" + commandVersion;
    }

    protected Map<String, Object> getErrorMap(Exception e) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("success", false);
        map.put("code", "error");
        map.put("message", e.getClass().getName() + ":" + e.getMessage());
        return map;
    }

    @Override
    public String invoke(String serviceName, String commandName, String commandVersion, String commandData) {
        try {
            String url = getRequestUrl(serviceName, commandName, commandVersion);
            HttpEntity<String> entity = new HttpEntity<>(commandData, getRequestHeaders());
            RequestCallback requestCallback = restTemplate.httpEntityCallback(entity, String.class);
            return restTemplate.execute(url, HttpMethod.POST, requestCallback, responseExtractor);
        } catch (Exception e) {
            try {
                return jsonMapper.writeValueAsString(getErrorMap(e));
            } catch (Exception en) {
                en.printStackTrace();
                return null;
            }
        }
    }

}
