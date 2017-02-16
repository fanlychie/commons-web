package org.fanlychie.commons.web.servlet;

import javax.servlet.ServletRequestEvent;
import javax.servlet.ServletRequestListener;

/**
 * 请求上下文监听器
 * <p>
 * 可以通过 RequestContext 获取请求相关的上下文变量
 * <p>
 * Created by fanlychie on 2017/2/16.
 */
public class RequestContextListener implements ServletRequestListener {

    @Override
    public void requestInitialized(ServletRequestEvent event) {
        RequestContext.setLocal(event.getServletRequest());
    }

    @Override
    public void requestDestroyed(ServletRequestEvent event) {

    }

}