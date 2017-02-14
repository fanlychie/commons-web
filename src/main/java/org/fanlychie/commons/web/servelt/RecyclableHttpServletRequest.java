package org.fanlychie.commons.web.servelt;

import org.fanlychie.commons.web.exception.RuntimeCastException;

import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * 扩展 HttpServletRequest 的 IO 资源可重复使用
 * Created by fanlychie on 2017/2/15.
 */
public class RecyclableHttpServletRequest extends HttpServletRequestWrapper {

    /**
     * 资源载体
     */
    private final String payload;

    /**
     * 构造器, 取出 IO 资源
     *
     * @param request HttpServletRequest 对象
     */
    public RecyclableHttpServletRequest(HttpServletRequest request) {
        super(request);
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(request.getInputStream(), "UTF-8"))) {
            String read;
            StringBuilder builder = new StringBuilder();
            while ((read = reader.readLine()) != null) {
                builder.append(read);
            }
            payload = builder.toString();
        } catch (IOException e) {
            throw new RuntimeCastException(e);
        }
    }

    /**
     * 获取请求中的 IO 资源字符串
     *
     * @return 返回请求中的 IO 资源字符串信息
     */
    public String getPayload() {
        return payload;
    }

    @Override
    public ServletInputStream getInputStream() throws IOException {
        final ByteArrayInputStream is = new ByteArrayInputStream(payload.getBytes("UTF-8"));
        return new ServletInputStream() {
            @Override
            public int read() throws IOException {
                return is.read();
            }
        };
    }

}