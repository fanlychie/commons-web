package org.fanlychie.commons.web.spring.converter;

import org.springframework.http.MediaType;
import org.springframework.http.converter.StringHttpMessageConverter;

import java.nio.charset.Charset;
import java.util.LinkedList;
import java.util.List;

/**
 * UTF-8 字符串消息转换器
 * Created by fanlychie on 2017/2/15.
 */
public class UTF8StringHttpMessageConverter extends StringHttpMessageConverter {

    /**
     * 构造器, 预设 UTF8 字符集编码
     */
    public UTF8StringHttpMessageConverter() {
        super(Charset.forName("UTF-8"));
        List<MediaType> mediaTypes = new LinkedList<>();
        mediaTypes.add(MediaType.valueOf(MediaType.TEXT_PLAIN_VALUE + ";charset=UTF-8"));
        mediaTypes.add(MediaType.valueOf(MediaType.TEXT_HTML_VALUE + ";charset=UTF-8"));
        mediaTypes.add(MediaType.valueOf(MediaType.APPLICATION_XML_VALUE + ";charset=UTF-8"));
        mediaTypes.add(MediaType.APPLICATION_JSON_UTF8);
        setSupportedMediaTypes(mediaTypes);
    }

}