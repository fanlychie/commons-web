package org.fanlychie.commons.web.spring.scheduling;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 抽象作业/任务
 * Created by fanlychie on 2017/2/24.
 */
public abstract class AbstractJob {

    /**
     * 日志对象
     */
    protected Logger logger = LoggerFactory.getLogger(getClass());

    /**
     * 执行任务
     */
    public abstract void execute();

}