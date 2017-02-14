package org.fanlychie.commons.web.spring.converter;

import org.fanlychie.commons.web.exception.ParseStringToDateException;
import org.springframework.core.convert.converter.Converter;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

/**
 * 字符串到日期对象转换器
 * Created by fanlychie on 2017/2/15.
 */
public class StringToDateConverter implements Converter<String, Date> {

    /**
     * 时间日期格式化模式列表
     */
    private List<DateFormat> formats;

    /**
     * 构造器, 预设时间日期格式化模式
     */
    public StringToDateConverter() {
        formats = new LinkedList<>();
        formats.add(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"));
        formats.add(new SimpleDateFormat("yyyy/MM/dd HH:mm:ss"));
        formats.add(new SimpleDateFormat("yyyy/MM/dd"));
        formats.add(new SimpleDateFormat("yyyy-MM-dd"));
        formats.add(new SimpleDateFormat("HH:mm:ss"));
    }

    @Override
    public Date convert(String source) {
        for (DateFormat format : formats) {
            try {
                return format.parse(source);
            } catch (ParseException e) {} // 忽略异常
        }
        throw new ParseStringToDateException("can not parse \"" + source + "\" to Date");
    }

    /**
     * 设置时间日期格式化模式
     *
     * @param pattern 时间日期格式化字符串
     */
    public void setPattern(String pattern) {
        formats.add(new SimpleDateFormat(pattern));
    }

    /**
     * 设置时间日期格式化模式列表
     *
     * @param patterns 时间日期格式化字符串列表
     */
    public void setPatterns(List<String> patterns) {
        for (String pattern : patterns) {
            formats.add(new SimpleDateFormat(pattern));
        }
    }

}