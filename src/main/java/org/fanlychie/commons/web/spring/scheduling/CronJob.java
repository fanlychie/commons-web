package org.fanlychie.commons.web.spring.scheduling;

import org.quartz.CronTrigger;
import org.quartz.JobDataMap;
import org.quartz.Scheduler;
import org.quartz.impl.JobDetailImpl;
import org.quartz.impl.triggers.CronTriggerImpl;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.support.ArgumentConvertingMethodInvoker;
import org.springframework.scheduling.quartz.MethodInvokingJobDetailFactoryBean.StatefulMethodInvokingJob;

import java.util.TimeZone;

/**
 * Quartz 作业/任务
 * Created by fanlychie on 2017/2/24.
 */
public class CronJob extends ArgumentConvertingMethodInvoker implements FactoryBean<CronTrigger>, InitializingBean {

    /**
     * 目标对象
     */
    private Object target;

    /**
     * 方法名称
     */
    private String method = "execute";

    /**
     * Cron 表达式
     */
    private String cronExpression;

    /**
     * Cron 触发器
     */
    private CronTrigger cronTrigger;

    /**
     * 创建一个作业/任务
     */
    public CronJob() {

    }

    /**
     * 创建一个作业/任务
     *
     * @param target 目标对象, 执行计划任务的 POJO 类的对象
     */
    public CronJob(Object target) {
        this.target = target;
    }

    /**
     * 设置目标对象
     *
     * @param target 目标对象, 执行计划任务的 POJO 类的对象
     */
    public void setTarget(Object target) {
        this.target = target;
    }

    /**
     * 设置方法名称
     *
     * @param method 方法名称, 执行计划任务的 POJO 类的对象的方法名称, 默认是 execute
     */
    public void setMethod(String method) {
        this.method = method;
    }

    /**
     * 设置 Cron 表达式
     *
     * @param cronExpression Cron 表达式
     */
    public void setCronExpression(String cronExpression) {
        this.cronExpression = cronExpression;
    }

    @Override
    public CronTrigger getObject() throws Exception {
        return cronTrigger;
    }

    @Override
    public Class<?> getObjectType() {
        return CronTrigger.class;
    }

    @Override
    public boolean isSingleton() {
        return true;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        setTargetObject(target);
        setTargetMethod(method);
        prepare();
        String name = target.getClass().getSimpleName();
        JobDetailImpl jobDetailImpl = new JobDetailImpl();
        jobDetailImpl.setName(name + "Job");
        jobDetailImpl.setGroup(Scheduler.DEFAULT_GROUP);
        jobDetailImpl.setJobClass(StatefulMethodInvokingJob.class);
        jobDetailImpl.setDurability(true);
        jobDetailImpl.getJobDataMap().put("methodInvoker", this);
        JobDataMap jobDataMap = new JobDataMap();
        jobDataMap.put("jobDetail", jobDetailImpl);
        CronTriggerImpl cronTriggerImpl = new CronTriggerImpl();
        cronTriggerImpl.setName(name + "Trigger");
        cronTriggerImpl.setGroup(Scheduler.DEFAULT_GROUP);
        cronTriggerImpl.setJobKey(jobDetailImpl.getKey());
        cronTriggerImpl.setJobDataMap(jobDataMap);
        cronTriggerImpl.setCronExpression(cronExpression);
        cronTriggerImpl.setTimeZone(TimeZone.getDefault());
        cronTrigger = cronTriggerImpl;
    }

}