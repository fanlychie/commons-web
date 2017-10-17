package org.fanlychie.commons.logging;

import com.alibaba.fastjson.JSON;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.reflect.CodeSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * 基于方法级别的切面日志记录
 * Created by fanlychie on 2017/10/17.
 */
public class MethodLoggingAspect {

    private int positionIncrementGap = 32;

    private static String positionIncrementGapText;

    private Logger logger = LoggerFactory.getLogger(getClass());

    /**
     * 目标方法成功执行完成时记录日志, 日志信息格式：
     *
     * 目标方法的全路径名称 毫秒单位的耗时时间 [自定义信息]
     * <-- 方法的输入参数
     * --> 方法的输出结果
     *
     * 示例：
     *
     * com.service.DomainService.doSomething 19ms << 113.108.207.164
     * <-- {"name":"fanlychie","email":"fanlychie@yeah.net"}
     * --> (String) "Done"
     */
    public Object aroundTargetMethod(ProceedingJoinPoint joinPoint) throws Throwable {
        long startingTimestamp = System.currentTimeMillis();
        Object result = joinPoint.proceed();
        long endingTimestamp = System.currentTimeMillis();
        long spentTimeSecond = endingTimestamp - startingTimestamp;
        String targetClassName = joinPoint.getTarget().getClass().getName();
        String targetMethodName = joinPoint.getSignature().getName();
        String returnType = joinPoint.getSignature().toString().split(" ")[0];
        StringBuilder builder = new StringBuilder();
        builder.append(targetClassName).append(".").append(targetMethodName).append(" ").append(spentTimeSecond).append("ms ").append(onLogging())
                .append(getPositionIncrementGapText())
                .append("<-- ").append(getFormatParams(joinPoint))
                .append(getPositionIncrementGapText())
                .append("--> ").append("(").append(returnType).append(") ").append(JSON.toJSONString(result));
        logger.info(builder.toString());
        return result;
    }

    /**
     * 当目标方法在执行时抛出异常的时候记录日志, 日志信息格式：
     *
     * 目标方法的全路径名称 [自定义信息]
     * <-- 方法的输入参数
     * 具体异常信息 ...
     *
     * 示例：
     *
     * com.service.DomainService.doSomething << 113.108.207.164
     * <-- {"name":"fanlychie","email":"fanlychie@yeah.net"}
     * java.lang.RuntimeException: ....
     */
    public void whenTargetMethodThrowing(JoinPoint joinPoint, Exception e) {
        String targetClassName = joinPoint.getTarget().getClass().getName();
        String targetMethodName = joinPoint.getSignature().getName();
        StringBuilder builder = new StringBuilder();
        builder.append(targetClassName).append(".").append(targetMethodName).append(" ").append(onLogging())
                .append(getPositionIncrementGapText())
                .append("<-- ").append(getFormatParams(joinPoint));
        logger.error(builder.toString(), e);
    }

    /**
     * 记录日志时调用, 用于制定自定义的日志信息;
     * @return 返回的字符串内容将添加到日志信息
     */
    public String onLogging() {
        return "";
    }

    /**
     * 设置日志信息左边的空白占位符的数量
     *
     * @param positionIncrementGap 日志信息左边的空白占位符的数量, 默认32个
     */
    public void setPositionIncrementGap(int positionIncrementGap) {
        this.positionIncrementGap = positionIncrementGap;
    }

    /**
     * 获取空白格式串
     */
    private String getPositionIncrementGapText() {
        if (positionIncrementGapText == null) {
            positionIncrementGapText = "\r\n";
            for (int i = 0; i < positionIncrementGap; i++) {
                positionIncrementGapText += " ";
            }
        }
        return positionIncrementGapText;
    }

    /**
     * 获取格式化的参数
     */
    private String getFormatParams(JoinPoint joinPoint) {
        Object[] values = joinPoint.getArgs();
        String[] names = ((CodeSignature) joinPoint.getStaticPart().getSignature())
                .getParameterNames();
        Map<String, Object> params = new LinkedHashMap<>();
        for (int i = 0; i < names.length; i++) {
            params.put(names[i], values[i]);
        }
        return JSON.toJSONString(params);
    }

}