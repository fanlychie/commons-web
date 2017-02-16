package org.fanlychie.commons.web.servlet;

import java.util.HashMap;
import java.util.Map;

/**
 * 本地上下文环境
 * Created by fanlychie on 2017/2/16.
 */
public final class LocalContext {

    /**
     * 线程本地变量表
     */
    private static final ThreadLocal<Map<Object, Object>> CONTEXT = new ThreadLocal<Map<Object, Object>>() {
        @Override
        protected Map<Object, Object> initialValue() {
            return new HashMap<>();
        }
    };

    /**
     * 获取本地线程变量表中的键关联的值
     *
     * @param name 名称
     * @param <T>  期望类型
     * @return 返回键关联的值
     */
    public static <T> T get(Object name) {
        return (T) CONTEXT.get().get(name);
    }

    /**
     * 将键值对放入本地线程变量表
     *
     * @param name  名称
     * @param value 值
     */
    public static void put(Object name, Object value) {
        CONTEXT.get().put(name, value);
    }

    // 私有化构造器
    private LocalContext() {

    }

}