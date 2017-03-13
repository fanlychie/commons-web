package org.fanlychie.commons.web.transport;

import org.fanlychie.commons.web.exception.RuntimeCastException;
import org.springframework.beans.BeanUtils;

/**
 * 输出 DTO 对象转换器, 意在将输入的参数对象转换输出为数据传输对象, 即 BO -> DTO 的转换
 * 借助 org.springframework.beans.BeanUtils#copyProperties 工具类, 采用 Java 内省的方式内部
 * 使用安全的强缓存实现
 * Created by fanlychie on 2017/3/13.
 */
public abstract class OutputDTOConverter<I> {

    /**
     * 业务层处理完用户请求后, 通过调用此方法, 将业务对象 BO 转换为数据传输对象 DTO
     *
     * @param input 输入对象, 通常为 BO 对象
     * @param <O>   期望的 DTO 数据类型
     * @return 返回数据传输对象
     */
    public <O extends OutputDTOConverter> O convert(I input) {
        O output;
        try {
            output = (O) getClass().newInstance();
        } catch (InstantiationException | IllegalAccessException e) {
            throw new RuntimeCastException(e);
        }
        BeanUtils.copyProperties(input, output);
        return output;
    }

}