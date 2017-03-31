# commons-web
应用于 Java Web 开发

# 下载依赖

```xml
<repositories>
	<repository>
		<id>github-maven-repo</id>
		<url>https://raw.github.com/fanlychie/maven-repo/releases</url>
	</repository>
</repositories>

<dependency>
	<groupId>org.fanlychie</groupId>
	<artifactId>commons-web</artifactId>
	<version>1.0.0</version>
</dependency>
```

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

curl -X GET http://localhost/testMethod1

效果示例：

浏览器看到 404.jsp 页面的内容

访问示例：

curl -X GET http://localhost/testMethod2

效果示例：

浏览器看到 500.jsp 页面的内容

访问示例：

curl -X GET http://localhost/formalMethod1

效果示例：

{"success":false,"data":null,"errmsg":"非法参数请求"}

访问示例：

curl -X GET http://localhost/formalMethod2

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

输入 DTO 对象转换器, 意在将当前对象作为输入对象, 并将其转换输出为业务对象, 即 DTO -> BO 的转换借助 org.springframework.beans.BeanUtils#copyProperties 工具类, 采用 Java 内省的方式内部使用安全的强缓存实现。

```java
public class User {

    private Long id;

    private String username;

    private String password;

    private Double accountBalance;

    // getter and setter
    
}
```

```java
public class UserInputDTO extends InputDTOConverter<User> {

    private String username;

    private String password;

    // getter and setter

}
```

```java
@RequestMapping(value = "/user/login", method = RequestMethod.POST)
public String login(UserInputDTO userInputDTO) {
    User user = userInputDTO.convert();
    // do something
    return "home";
}
```

UserInputDTO 只接收用户名和密码参数，并且可轻松的转换成目标对象，可有效的防止恶意修改账户余额等重要字段（注：此此仅做示例用）。

# OutputDTOConverter

输出 DTO 对象转换器, 意在将输入的参数对象转换输出为数据传输对象, 即 BO -> DTO 的转换借助 org.springframework.beans.BeanUtils#copyProperties 工具类, 采用 Java 内省的方式内部使用安全的强缓存实现。

```java
public class UserOutputDTO extends OutputDTOConverter<User> {

    private String username;

    private Double accountBalance;

    public UserOutputDTO(User input) {
        super(input);
    }

    // getter and setter

}
```

```java
@ResponseBody
@RequestMapping(value = "/user/info/{userId}", method = RequestMethod.GET)
public UserOutputDTO info(Long userId) {
    User user = userService.selectUserById(userId);
    // do something
    return new UserOutputDTO(user);
}
```

UserOutputDTO 只输出用户名和账户余额信息，可以向外部隐藏一些不想暴露的属性，并且可轻松的转换（注：此此仅做示例用）。

# spring mvc 请求参数检验

与 Hibernate Validator 注解类似，但又不同于 Hibernate Validator。内部是采用抛异常的机制，在参数校验失败时正常响应客户端的请求。

依赖：

```xml
<properties>
    <aspectj.version>1.6.12</aspectj.version>
    <jreflect.version>1.2.0</jreflect.version>
    <fastjson.version>1.2.28</fastjson.version>
</properties>

<dependency>
    <groupId>org.aspectj</groupId>
    <artifactId>aspectjweaver</artifactId>
    <version>${aspectj.version}</version>
</dependency>
<dependency>
    <groupId>org.aspectj</groupId>
    <artifactId>aspectjrt</artifactId>
    <version>${aspectj.version}</version>
</dependency>
<dependency>
    <groupId>org.fanlychie</groupId>
    <artifactId>jreflect</artifactId>
    <version>${jreflect.version}</version>
</dependency>
<dependency>
    <groupId>com.alibaba</groupId>
    <artifactId>fastjson</artifactId>
    <version>${fastjson.version}</version>
</dependency>
```

使用注解的方式，通过标注在字段属性上，支持的校验注解有：

### @Valid

唯一一个方法参数的注解，表示验证此参数对象

### @Alphabetic

英文字母, 大小写混合, 仅当标注在 {@link java.lang.CharSequence} 类型的属性时进行值的验证

### @Alphanumeric

英文字母(可含大小写)和数字混合, 仅当标注在 {@link java.lang.CharSequence} 类型的属性时进行值的验证

### @ErrorType

唯一一个用于标注在类上的注解，参数值验证失败时的返回值类型

### @Length

长度, 仅当标注在 {@link java.lang.CharSequence} 类型的属性时进行值的验证

### @NotBlank

非空白(除去首尾的空白部分长度不为0), 仅当标注在 {@link java.lang.CharSequence} 类型的属性时进行值的验证

### @NotEmpty

非空, 仅当标注在 {@link java.lang.CharSequence} 类型的属性时进行值的验证

### @NotNull

Not Null

### @Numeric

数字, 仅当标注在 {@link java.lang.CharSequence} 类型的属性时进行值的验证

### @Pattern

正则表达式模式匹配, 仅当标注在 {@link java.lang.CharSequence} 类型的属性时进行值的验证

### @SafeHtml

安全的HTML, 仅当标注在 {@link java.lang.CharSequence} 类型的属性时进行值的验证和矫正

使用校验时，先在 spring 配置文件中注册注解处理器：

```xml
<bean class="org.fanlychie.commons.web.spring.validator.AnnotationHandlerRegistry" />
```

假设我们用 Response 对象统一响应客户端JSON数据请求：

```java
public class Response {

    private boolean success;

    private Object data;

    private String errmsg;

    public Response() {
        
    }

    public Response(String errmsg) {
        this.success = false;
        this.errmsg = errmsg;
    }

    public Response(Object data) {
        this.success = true;
        this.data = data;
    }

    // getter and setter
    
}
```

基于上面的 UserInputDTO，做如下修改：

```java
public class UserInputDTO extends InputDTOConverter<User> {

    @NotEmpty(errmsg = "用户名称不能为空", errtype = Response.class)
    @SafeHtml
    private String username;

    @Alphanumeric(errmsg = "密码格式错误，请使用英文字母和数字的组合", errtype = Response.class)
    @Length(errmsg = "密码长度错误，请输入6-18个字符作为密码", min = 6, max = 18, errtype = Response.class)
    private String password;

    // getter and setter

}
```

当检验参数不通过时，会向客户端返回 errmsg 信息。errmsg 可以是一个文字描述信息（用于JSON数据的接口），或是一个视图路径如 errmsg = "/error/404"。

在请求参数校验失败时，仅当控制器类标注了 @RestController 注解，或方法标注了 @ResponseBody 注解，校验器会使用 JSON 数据的方式响应客户端，除此之外，均用视图响应客户端。

errtype 是校验失败时响应客户端的错误信息类型，默认是 errtype = String.css。它要求指向的类拥有一个 String 类型参数的构造器，这点很重要。

errtype 在校验对象参数时若统一指向相同的一个类，我们可以使用 @ErrorType 注解代替：

```java
@ErrorType(Response.class)
public class UserInputDTO extends InputDTOConverter<User> {

    @NotEmpty(errmsg = "用户名称不能为空")
    @SafeHtml
    private String username;

    @Alphanumeric(errmsg = "密码格式错误，请使用英文字母和数字的组合")
    @Length(errmsg = "密码长度错误，请输入6-18个字符作为密码", min = 6, max = 18)
    private String password;

    // getter and setter

}
```

@SafeHtml 注解提供两种策略，一种是清空所有的脚本内容仅保留HTML文本部分（默认策略），另一种是直接转义符号，使用时配置 @SafeHtml(Strategy.ESCAPE)。

注册用户的简单示例：

```java
@ResponseBody
@RequestMapping(value = "/user/register", method = RequestMethod.POST)
public String register(@Valid UserInputDTO userInputDTO) {
    User user = userInputDTO.convert();
    user.setId(100001L);
    // do something
    return new Response(user.getId());
}
```

访问示例：

curl -X POST http://localhost/user/register --data-urlencode "username=&password=123"

效果示例：

{
  "errmsg": "用户名称不能为空",
  "success": false
}


访问示例：

curl -X POST http://localhost/user/register --data-urlencode "username=fanlychie&password=123"

效果示例：

{
  "errmsg": "密码长度错误，请输入6-18个字符作为密码",
  "success": false
}

访问示例：

curl -X POST http://localhost/user/register --data-urlencode "username=fanlychie&password=123!#321"

效果示例：

{
  "errmsg": "密码格式错误，请使用英文字母和数字的组合",
  "success": false
}

访问示例：

curl -X POST http://localhost/user/register --data-urlencode "username=<script>alert('我是一个弹框');</script>&password=123654abc"

效果示例：

{
  "errmsg": "用户名称不能为空",
  "success": false
}（注：脚本内容已经被清空）

访问示例：

curl -X POST http://localhost/user/register --data-urlencode "username=<script>alert('我是一个弹框');</script>&lt;p&gt;我是一个段落&lt;/p&gt;&password=123654abc"

效果示例：

{
  "success": true,
  "data": "你注册的用户名是：我是一个段落",
  "errmsg": null
}

# spring 计划任务

内部使用 Quartz，意在简化XML配置。

依赖：

```xml
<properties>
    <quartz.version>2.2.3</quartz.version>
    <spring.version>4.2.5.RELEASE</spring.version>
</properties>

<dependency>
	<groupId>org.springframework</groupId>
	<artifactId>spring-context-support</artifactId>
	<version>${spring.version}</version>
</dependency>
<dependency>
	<groupId>org.quartz-scheduler</groupId>
	<artifactId>quartz</artifactId>
	<version>${quartz.version}</version>
</dependency>
```

在 spring 配置文件中添加：

```xml
<bean class="org.fanlychie.commons.web.spring.scheduling.Scheduler">
	<constructor-arg>
		<list>
			<bean class="org.fanlychie.commons.web.spring.scheduling.CronJob" p:cronExpression="*/5 * * * * ?">
				<constructor-arg>
					<bean class="com.domain.MyJob">
						<property name="dataMap">
							<map>
								<entry key="name" value="fanlychie" />
							</map>
						</property>
					</bean>
				</constructor-arg>
			</bean>
		</list>
	</constructor-arg>
</bean>
```

```java
public class MyJob extends AbstractJob {

    private Map<String, Object> dataMap;

    @Override
    public void execute() {
        System.out.println("-------- " + dataMap.get("name") + " --------");
    }

    public void setDataMap(Map<String, Object> dataMap) {
        this.dataMap = dataMap;
    }

}
```

