package org.fanlychie.commons.web.spring.validator.validation;

import org.fanlychie.commons.web.spring.validator.constraint.SafeHtml;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by fanlychie on 2017/3/18.
 */
public class SafeHtmlValidator {

    private static final Map<String, String> HTML_ESCAPE_MAP = new HashMap<>();

    private static final String HTML_TAG_REGEX = "[\\s\\S]*<(S*?)[^>]*>.*?|<.*? />";

    private static final String HTML_REPLACE_REGEX = "<(S*?)[^>]*>.*?|<.*? />";

    private static final String JS_REPLACE_REGEX = "<[\\s]*?script[^>]*?>.*?<[\\s]*?\\/[\\s]*?script[\\s]*?>";

    private SafeHtmlValidator() {

    }

    public static String validCorrect(String value, SafeHtml safeHtml) {
        switch (safeHtml.value()) {
            case CLEAR:
                return makeClearHtml(value);
            case ESCAPE:
                return makeEscapeHtml(value);
            default:
                return value;
        }
    }

    private static String makeEscapeHtml(String content) {
        if (content == null || content.length() == 0) {
            return content;
        }
        if (!content.matches(HTML_TAG_REGEX)) {
            return content;
        }
        for (String mark : HTML_ESCAPE_MAP.keySet()) {
            if (content.contains(mark)) {
                content = content.replaceAll(mark, HTML_ESCAPE_MAP.get(mark));
            }
        }
        return content;
    }

    private static String makeClearHtml(String content) {
        if (content == null || content.length() == 0) {
            return content;
        }
        if (!content.matches(HTML_TAG_REGEX)) {
            return content;
        }
        return content.replaceAll(JS_REPLACE_REGEX, "").replaceAll(HTML_REPLACE_REGEX, "");
    }

    static {
        HTML_ESCAPE_MAP.put("<", "&lt;");
        HTML_ESCAPE_MAP.put(">", "&gt;");
        HTML_ESCAPE_MAP.put("'", "&#039;");
        HTML_ESCAPE_MAP.put("\"", "&quot;");
    }

}