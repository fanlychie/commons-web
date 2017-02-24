package org.fanlychie.commons.web.spring.scheduling;

import org.quartz.Trigger;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;

/**
 * 计划任务
 * Created by fanlychie on 2017/2/24.
 */
public class Scheduler extends SchedulerFactoryBean {

    public Scheduler(Trigger... triggers) {
        this.setTriggers(triggers);
    }

}