package org.fanlychie.commons.web.spring.validator.handler;

import org.fanlychie.commons.web.exception.ArgumentVaildException;
import org.fanlychie.commons.web.servlet.ResponseContext;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.SimpleMappingExceptionResolver;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 注解异常处理器
 * Created by fanlychie on 2017/3/15.
 */
public class AnnotationMappingExceptionResolver extends SimpleMappingExceptionResolver {

    @Override
    protected ModelAndView doResolveException(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
        if (ex instanceof ArgumentVaildException) {
            ArgumentVaildException ave = (ArgumentVaildException) ex;
            if (ave.isApplicationJsonResponse()) {
                return writeJSONMessage(response, ave.getMessage());
            } else {
                return new ModelAndView(ave.getMessage());
            }
        }
        return null;
    }

    // 写出 JSON 到客户端
    private ModelAndView writeJSONMessage(HttpServletResponse response, String message) {
        if (ResponseContext.getResponse() == null) {
            ResponseContext.setLocal(response);
        }
        ResponseContext.write(message);
        return new ModelAndView();
    }

}