package com.tormozz.hostping;

import javax.servlet.ServletContext;

import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.SimpleTrigger;
import org.quartz.impl.StdSchedulerFactory;

/**
 * 
 * @author Created by andrey.kuznetsov lt <br>
 * @author Last modified by $Author$ <br>
 * @author Last modified on $Date$ at revision $Revision$ <br>
 */
public class QuartzPinger extends Pinger{

	private static QuartzPinger quartzPinger = null;
	
	private final String JOB_NAME = "HOST_PING_JOB";
	private final String JOB_GROUP = "HOST_PING_GROUP";
	private final String JOB_TRIGGER = "HOST_PING_TRIGGER";
	
	public static QuartzPinger getInstance(ServletContext p_context){
		if(quartzPinger == null){
			quartzPinger = new QuartzPinger(p_context);
		}
		return quartzPinger;
	} 
	
	private QuartzPinger(ServletContext p_context) {
		initialize(p_context);
	
		try {
			Scheduler scheduler = new StdSchedulerFactory().getScheduler();
			scheduler.start();
			scheduler.scheduleJob(new JobDetail(JOB_NAME, JOB_GROUP, PingJob.class), 
								new SimpleTrigger(JOB_TRIGGER, JOB_GROUP, 
										SimpleTrigger.REPEAT_INDEFINITELY, repeatInterval*MILLISECONDS_IN_SECOND));
		} catch (SchedulerException e) {
			//TODO noting
		}
	}
}
