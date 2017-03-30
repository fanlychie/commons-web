# commons-web
应用于 Java Web 开发

# 将客户端请求记录到日志

在 web.xml 添加如下配置：

```xml
<filter>
    <filter-name>requestLoggerFilter</filter-name>
    <filter-class>org.fanlychie.commons.web.filter.RequestLoggingFilter</filter-class>
</filter>
<filter-mapping>
    <filter-name>requestLoggerFilter</filter-name>
    <url-pattern>/*</url-pattern>
</filter-mapping>
```

RequestLoggingFilter 使用的日志级别为 INFO 级别，若应用的日志级别高于 INFO，可在 log4j 配置文件中添加：

```properties
log4j.logger.org.fanlychie.commons.web.filter = INFO
```

效果示例：

```html
INFO  [RequestLoggingFilter:52] - GET /demo/test1?name=fanlychie&sex=male HTTP/1.1 127.0.0.1
INFO  [RequestLoggingFilter:62] - POST /demo/test2 HTTP/1.1 127.0.0.1 [Content-Type=application/x-www-form-urlencoded, Content-Encoding=null, Content-Length=23]
INFO  [RequestLoggingFilter:63] - param: {name=fanlychie, sex=male}
```

若某些请求不想要记录到日志，可以通过 ignore 参数配置：

```xml
<filter>
    <filter-name>requestLoggerFilter</filter-name>
    <filter-class>org.fanlychie.commons.web.filter.RequestLoggingFilter</filter-class>
    <init-param>
        <param-name>ignore</param-name>
        <param-value>*/test/*</param-value>
    </init-param>
</filter>
<filter-mapping>
    <filter-name>requestLoggerFilter</filter-name>
    <url-pattern>/*</url-pattern>
</filter-mapping>
```

多个可以使用空格或英文逗号分隔开：

```xml
<init-param>
    <param-name>ignore</param-name>
    <param-value>*/test/*,*/testing/*</param-value>
</init-param>
```

# spring mvc 统一异常处理

在 spring mvc 配置文件中添加如下配置：

```xml
<bean class="org.fanlychie.commons.web.spring.handler.ExceptionLoggingHandler">
    <!-- 默认异常处理方式 -->
    <property name="defaultExceptionResolver" value="json" />
    <!-- 默认异常返回的 JSON 信息 -->
    <property name="defaultExceptionJsonMessage">
        <value>{"success":false,"data":null,"errmsg":"服务器内部异常"}</value>
    </property>
    <!-- 异常返回的 JSON 信息映射表 -->
    <property name="exceptionJsonMessageMappings">
        <map>
            <entry key="java.lang.IllegalAccessException" value="fail" />
            <entry key="java.lang.IllegalArgumentException">
                <value>{"success":false,"data":null,"errmsg":"非法参数请求"}</value>
            </entry>
        </map>
    </property>
    <!-- 异常返回视图的位置映射列表 -->
    <property name="exceptionViewMappings">
        <list>
            <value>com.domain.controller.*Controller.test*</value>
            <value>com.domain.controller.*Controller.other*</value>
        </list>
    </property>
    <!-- 异常返回的视图映射列表 -->
    <property name="exceptionMappings">
        <props>
            <prop key="java.lang.NullPointerException">/error/500</prop>
            <prop key="java.lang.IllegalArgumentException">/error/404</prop>
        </props>
    </property>
</bean>
```

defaultExceptionResolver = json 适用于以提供JSON数据接口服务为主导的应用。若应用同时和提供少量的页面服务，可配置 exceptionViewMappings 和 exceptionMappings，否则可以不用配置。exceptionJsonMessageMappings 也不是必须配置项，它是根据具体业务制定的。

Controller 示例：

```java
@RequestMapping(value = "/testMethod1", method = RequestMethod.GET)
public void testMethod1(String name) {
    if (name == null) {
        throw new IllegalArgumentException();
    }
    // do something
}

@RequestMapping(value = "/testMethod2", method = RequestMethod.GET)
public void testMethod2(String name) {
    if (name != null) {
        name = null;
    }
    name.trim(); // NullPointerException
}

@ResponseBody
@RequestMapping(value = "/formalMethod1", method = RequestMethod.GET)
public Object formalMethod1(String name) {
    if (name == null) {
        throw new IllegalArgumentException();
    }
    // do something
    return null;
}

@ResponseBody
@RequestMapping(value = "/formalMethod2", method = RequestMethod.GET)
public Object formalMethod2(String name) {
    if (name != null) {
        name = null;
    }
    name.trim(); // NullPointerException
    return null;
}
```

访问示例：

GET http://localhost/demo/testMethod1

效果示例：

浏览器看到 404.jsp 页面的内容

访问示例：

GET http://localhost/demo/testMethod2

效果示例：

浏览器看到 500.jsp 页面的内容

访问示例：

GET http://localhost/demo/formalMethod1

效果示例：

{"success":false,"data":null,"errmsg":"非法参数请求"}

访问示例：

GET http://localhost/demo/formalMethod2

效果示例：

{"success":false,"data":null,"errmsg":"服务器内部异常"}

若应用是以提供页面服务为主导，基于以上配置将 defaultExceptionResolver = json 改成 defaultExceptionResolver = view 即可。通常提供页面服务的应用同时还会提供JSON接口服务，基于以上配置将 exceptionViewMappings 改成 exceptionJsonMappings 即可。具体配置如下：

```xml
<bean class="org.fanlychie.commons.web.spring.handler.ExceptionLoggingHandler">
    <!-- 默认异常处理方式 -->
    <property name="defaultExceptionResolver" value="view" />
    <!-- 默认异常返回的 JSON 信息 -->
    <property name="defaultExceptionJsonMessage">
        <value>{"success":false,"data":null,"errmsg":"服务器内部异常"}</value>
    </property>
    <!-- 异常返回的 JSON 信息映射表 -->
    <property name="exceptionJsonMessageMappings">
        <map>
            <entry key="java.lang.IllegalAccessException" value="fail" />
            <entry key="java.lang.IllegalArgumentException">
                <value>{"success":false,"data":null,"errmsg":"非法参数请求"}</value>
            </entry>
        </map>
    </property>
    <!-- 异常返回视图的位置映射列表 -->
    <property name="exceptionJsonMappings">
        <list>
            <value>com.domain.controller.*Controller.test*</value>
            <value>com.domain.controller.*Controller.other*</value>
        </list>
    </property>
    <!-- 异常返回的视图映射列表 -->
    <property name="exceptionMappings">
        <props>
            <prop key="java.lang.NullPointerException">/error/500</prop>
            <prop key="java.lang.IllegalArgumentException">/error/404</prop>
        </props>
    </property>
</bean>
```

以上出现的配置项都是非必须的，根据实际具体的业务删减配置即可。

需要提醒的是，配置 ExceptionLoggingHandler 之后，异常不再需要捕捉，相反的，而应该往外抛出。ExceptionLoggingHandler 在捕捉到异常之后会使用 ERROR 级别记录到日志，同时根据配置最大可能的保证在服务器抛出异常之后客户端能得到正常的响应。如果在业务中捕捉了异常，除非手工抛出，否则 ExceptionLoggingHandler 无法感知异常，配置也就无法起到作用。

# UTF8StringHttpMessageConverter & UTF8JsonHttpMessageConverter

内部做了简单的封装（用于简化XML配置），分别用于解决服务器端返回数据字符串中文乱码和JSON数据中文乱码。以前你可能是这样配置的：

```xml
<bean id="stringHttpMessageConverter" class="org.springframework.http.converter.StringHttpMessageConverter">
    <constructor-arg value="UTF-8" />
    <property name="supportedMediaTypes">
        <list>
            <value>text/html;charset=utf-8</value>
            <value>application/xml;charset=utf-8</value>
            <value>application/json;charset=utf-8</value>
        </list>
    </property>
</bean>

<bean id="mappingJackson2HttpMessageConverter" class="org.springframework.http.converter.json.MappingJackson2HttpMessageConverter">
    <property name="supportedMediaTypes">
        <list>
            <value>text/html;charset=utf-8</value>
            <value>application/xml;charset=utf-8</value>
            <value>application/json;charset=utf-8</value>
        </list>
    </property>
</bean>

<bean class="org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerAdapter">
    <property name="messageConverters">
        <list>
            <ref bean="stringHttpMessageConverter" />
            <ref bean="mappingJackson2HttpMessageConverter" />
        </list>
    </property>
</bean>
```

现在可以这样配置：

```xml
<bean class="org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerAdapter">
    <property name="messageConverters">
        <list>
            <bean class="org.fanlychie.commons.web.spring.converter.UTF8StringHttpMessageConverter" />
            <bean class="org.fanlychie.commons.web.spring.converter.UTF8JsonHttpMessageConverter" />
        </list>
    </property>
</bean>
```

# StringToDateConverter

用于将请求参数中的字符串类型转换成java.util.Date类型。内部使用正则提取子表达式的方式能满足日常常用的日期时间字符串转换。

字符串示例（数字之间的分隔符可以任意替换）：

2017-03-30 21:54:08

2017年3月30日

21:54:08

对于日期的字符串，除了年份要求是4位数值，月份和天数不做要求，可以是一位或两位的数值。

对于时间的字符串，只要求含有时分秒，其余不做要求，无论是12小时制还是24小时制，无论时分秒的数值是一位还是两位。

对于日期时间字符串，内部是采用日期和时间的组合，因此以上规则同样适用于日期时间字符串类型。

另外，数字与数字之间的分隔符可以任意替换，如：2017-03-30 与 2017/03/30，2017年3月30日 与 2017年3月30。

```xml
<bean id="conversionService" class="org.springframework.format.support.FormattingConversionServiceFactoryBean">
    <property name="converters">
        <set>
            <bean class="org.fanlychie.commons.web.spring.converter.StringToDateConverter" />
        </set>
    </property>
</bean>

<bean id="webBindingInitializer" class="org.springframework.web.bind.support.ConfigurableWebBindingInitializer">
    <property name="conversionService" ref="conversionService" />
</bean>

<bean class="org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerAdapter">
    <property name="webBindingInitializer" ref="webBindingInitializer" />
</bean>
```

# ServletHandlerInterceptor

继承于 org.springframework.web.servlet.handler.HandlerInterceptorAdapter，用于拦截处理请求。

实现未登录访问站点时跳转到登录页面的简单示例：

```java
public class AccessInterceptor extends ServletHandlerInterceptor {

    @Override
    public boolean handle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if (request.getSession().getAttribute("user") == null) {
            return sendRedirect(request.getContextPath() + "/user/login");
        }
        return true;
    }

}
```

spring mvc 配置文件中添加：

```xml
<bean class="org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping">
    <property name="interceptors">
        <list>
            <bean class="com.domain.interceptor.AccessInterceptor">
                <property name="skipUrls">
                    <list>
                        <value>*/user/login*</value>
                    </list>
                </property>
            </bean>
        </list>
    </property>
</bean>
```

# SimpleHandlerInterceptor

继承于 ServletHandlerInterceptor，是 ServletHandlerInterceptor 的一个简单的实现类。

配置 SimpleHandlerInterceptor 可以使用 RequestContext 和 ResponseContext 提供的服务。具体参考 RequestContext 和 ResponseContext 描述部分。

spring mvc 配置文件中添加：

```xml
<bean class="org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping">
    <property name="interceptors">
        <list>
            <bean class="org.fanlychie.commons.web.spring.servlet.SimpleHandlerInterceptor"/>
        </list>
    </property>
</bean>
```

# RequestContextListener

配置 SimpleHandlerInterceptor 可以使用 RequestContext 提供的服务。具体参考 RequestContext 描述部分。

在 web.xml 中添加：

```xml
<listener>
    <listener-class>org.fanlychie.commons.web.servlet.RequestContextListener</listener-class>
</listener>
```

# RequestContext

通过配置 RequestLoggingFilter、RequestContextListener、ServletHandlerInterceptor、SimpleHandlerInterceptor 其中的任意一个时生效。

### getRequest()

获取本次请求的 HttpServletRequest 对象

### getSession()

获取 HttpSession 对象

### getRequestParam(String name)

获取请求参数值

### setSessionAttribute(String name, Object value)

设置会话属性值

### getSessionAttribute(String name)

获取会话属性的值

### getInputStream()

获取输入流对象

### getReader()

获取读对象

### forward(String path)

转发

### getClientIPAddress()

获取客户端 IP 地址

### getServerPath()

获取服务器路径地址

# ResponseContext

通过配置 RequestLoggingFilter、ServletHandlerInterceptor、SimpleHandlerInterceptor 其中的任意一个时生效。

### getResponse()

获取本次请求的 HttpServletResponse 对象

### getOutputStream()

获取输出流

### getWriter()

获取写对象

### write(String text)

向客户端写出响应消息

### sendRedirect(String location)

重定向

# InputDTOConverter