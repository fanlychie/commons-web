package org.fanlychie.commons.web.transport;

import org.fanlychie.commons.web.exception.RuntimeCastException;
import org.springframework.beans.BeanUtils;

import java.lang.reflect.ParameterizedType;

/**
 * 输入 DTO 对象转换器, 意在将当前对象作为输入对象, 并将其转换输出为业务对象, 即 DTO -> BO 的转换
 * 借助 org.springframework.beans.BeanUtils#copyProperties 工具类, 采用 Java 内省的方式内部
 * 使用安全的强缓存实现
 * Created by fanlychie on 2017/3/13.
 */
public abstract class InputDTOConverter<O> {

    private Class<O> outputClass;

    public InputDTOConverter() {
        outputClass = (Class<O>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0];
    }

    /**
     * 在控制层通过 DTO 来接收用户数据后, 通过调用此方法, 将 DTO 对象转换为业务对象 BO
     *
     * @return 返回转换的业务对象
     */
    public O convert() {
        O output;
        try {
            output = outputClass.newInstance();
        } catch (InstantiationException | IllegalAccessException e) {
            throw new RuntimeCastException(e);
        }
        BeanUtils.copyProperties(this, output);
        return output;
    }

}