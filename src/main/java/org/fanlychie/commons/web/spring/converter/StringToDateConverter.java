package org.fanlychie.commons.web.spring.converter;

import org.fanlychie.commons.web.exception.ParseStringToDateException;
import org.fanlychie.commons.web.exception.RuntimeCastException;
import org.springframework.core.convert.converter.Converter;
import org.springframework.util.StringUtils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 字符串到日期对象转换器
 * Created by fanlychie on 2017/2/15.
 */
public class StringToDateConverter implements Converter<String, Date> {

    private static final Pattern DATE_STRING_REGEX = Pattern.compile("(\\d{4})(\\S)(\\d{1,2})(\\S)(\\d{1,2})([^ \\f\\n\\r\\t\\v\\d]?)");

    private static final Pattern TIME_STRING_REGEX = Pattern.compile("(\\d{1,2})(\\S)(\\d{1,2})(\\S)(\\d{1,2})([^ \\f\\n\\r\\t\\v\\d]?)");

    private static final Pattern DATETIME_STRING_REGEX = Pattern.compile(DATE_STRING_REGEX + "(\\s)" + TIME_STRING_REGEX);

    private static final Pattern TIMESTAMP_STRING_REGEX = Pattern.compile("[1-9]\\d{12,}");

    private static final ConcurrentHashMap<String, DateFormat> PATTERN_FORMAT = new ConcurrentHashMap<>();

    @Override
    public Date convert(String source) {
        if (StringUtils.isEmpty(source)) {
            return null;
        }
        Matcher matcher = DATETIME_STRING_REGEX.matcher(source);
        if (matcher.matches()) {
            String pattern = matcher.replaceAll("yyyy$2MM$4dd$6$7HH$9mm$11ss$13");
            return parseStringToDate(source, pattern);
        }
        matcher = DATE_STRING_REGEX.matcher(source);
        if (matcher.matches()) {
            String pattern = matcher.replaceAll("yyyy$2MM$4dd$6");
            return parseStringToDate(source, pattern);
        }
        matcher = TIMESTAMP_STRING_REGEX.matcher(source);
        if (matcher.matches()) {
            return new Date(Long.parseLong(source));
        }
        matcher = TIME_STRING_REGEX.matcher(source);
        if (matcher.matches()) {
            String pattern = matcher.replaceAll("HH$2mm$4ss$6");
            return parseStringToDate(source, pattern);
        }
        throw new ParseStringToDateException("can not parse \"" + source + "\" to Date");
    }

    private Date parseStringToDate(String source, String pattern) {
        if (!PATTERN_FORMAT.containsKey(pattern)) {
            PATTERN_FORMAT.put(pattern, new SimpleDateFormat(pattern));
        }
        try {
            return PATTERN_FORMAT.get(pattern).parse(source);
        } catch (ParseException e) {
            throw new RuntimeCastException(e);
        }
    }

}