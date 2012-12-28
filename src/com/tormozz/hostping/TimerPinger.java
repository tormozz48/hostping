package com.tormozz.hostping;

import java.util.Timer;
import java.util.TimerTask;

import javax.servlet.ServletContext;

/**
 * Class for ping remote hosts with Timer <br>
 * @author Created by andrey.kuznetsov lt <br>
 * @author Last modified by $Author$ <br>
 * @author Last modified on $Date$ at revision $Revision$ <br>
 */
public class TimerPinger extends Pinger{
	
	private static TimerPinger timerPinger = null;
	
	/**
	 * Returns or create single instance of {@link TimerPinger} class <br>
	 * @param p_context - servlet context; {@link ServletContext} <br>
	 * @return {@link TimerPinger}
	 */
	public static TimerPinger getInstance(ServletContext p_context){
		if(timerPinger == null){
			timerPinger = new TimerPinger(p_context);
		}	
		return timerPinger;
	}
	
	/**
	 * Constructor of class {@link TimerPinger} <br>
	 * @param p_context - servlet context; {@link ServletContext}
	 */
	private TimerPinger(ServletContext p_context) {
		initialize(p_context);       
        (new Timer()).schedule(new TimerTask(){
            public void run() {
            	pingHosts();	   
            }
          }, startTime*MILLISECONDS_IN_SECOND, repeatInterval*MILLISECONDS_IN_SECOND);
    }  
}
