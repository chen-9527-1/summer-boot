package org.summerframework.ddd.core;

import java.util.HashMap;
import java.util.Map;

/**
 * 线程上下文共享集合
 */
public class ThreadLocalMap {

    private static ThreadLocal<Map<String, Object>> threadContext = new ThreadLocal<Map<String, Object>>() {
        protected Map<String, Object> initialValue() {
            return new HashMap<String, Object>();
        }
    };

    public static Object get(String key) {
        return getContextMap().get(key);
    }

    public static void put(String key, Object value) {
        getContextMap().put(key, value);
    }

    public static Object remove(String key) {
        return getContextMap().remove(key);
    }

    public static void clear() {
        threadContext.remove();
    }

    public static Map<String, Object> getContextMap() {
        return threadContext.get();
    }

    public static void setContextMap(Map<String, Object> value) {
        threadContext.set(value);
    }

}