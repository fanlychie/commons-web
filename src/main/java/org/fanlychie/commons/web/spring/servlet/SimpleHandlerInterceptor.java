package org.fanlychie.commons.web.spring.servlet;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 简单的处理器拦截
 * <p>
 * 可以通过 RequestContext 和 ResponseContext 获取请求相关的上下文变量
 * <p>
 * Created by fanlychie on 2017/2/15.
 */
public class SimpleHandlerInterceptor extends ServletHandlerInterceptor {

    @Override
    public boolean handle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        return true;
    }

}