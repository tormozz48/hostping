package com.tormozz.hostping;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

/**
 * Quartz job for ping remote hosts <br>
 * @author Created by andrey.kuznetsov lt <br>
 * @author Last modified by $Author$ <br>
 * @author Last modified on $Date$ at revision $Revision$ <br>
 */
public class PingJob implements Job{
	
	public void execute(JobExecutionContext arg0) throws JobExecutionException {
		QuartzPinger.getInstance(null).pingHosts();
	}

}
