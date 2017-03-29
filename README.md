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