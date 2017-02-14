package org.fanlychie.commons.web.spring.converter;

import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;

import java.util.LinkedList;
import java.util.List;

/**
 * UTF-8 JSON 消息转换器
 * Created by fanlychie on 2017/2/15.
 */
public class UTF8JsonHttpMessageConverter extends MappingJackson2HttpMessageConverter {

    /**
     * 构造器, 预设 UTF8 字符集编码
     */
    public UTF8JsonHttpMessageConverter() {
        List<MediaType> mediaTypes = new LinkedList<>();
        mediaTypes.add(MediaType.valueOf(MediaType.TEXT_PLAIN_VALUE + ";charset=UTF-8"));
        mediaTypes.add(MediaType.valueOf(MediaType.TEXT_HTML_VALUE + ";charset=UTF-8"));
        mediaTypes.add(MediaType.valueOf(MediaType.APPLICATION_XML_VALUE + ";charset=UTF-8"));
        mediaTypes.add(MediaType.APPLICATION_JSON_UTF8);
        setSupportedMediaTypes(mediaTypes);
    }

}