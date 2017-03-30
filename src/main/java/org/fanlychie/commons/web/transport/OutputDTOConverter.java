package org.fanlychie.commons.web.transport;

import org.springframework.beans.BeanUtils;

/**
 * 输出 DTO 对象转换器, 意在将输入的参数对象转换输出为数据传输对象, 即 BO -> DTO 的转换
 * 借助 org.springframework.beans.BeanUtils#copyProperties 工具类, 采用 Java 内省的方式内部
 * 使用安全的强缓存实现
 * Created by fanlychie on 2017/3/13.
 */
public abstract class OutputDTOConverter<I> {

    public OutputDTOConverter(I input) {
        BeanUtils.copyProperties(input, this);
    }

}