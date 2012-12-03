package com.tormozz.hostping;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.logging.Logger;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.apache.commons.collections.CollectionUtils;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;

/**
 * @author Created by andrey.kuznetsov lt <br>
 * @author Last modified by $Author$ <br>
 * @author Last modified on $Date$ at revision $Revision$ <br>
 */
public class PingListener implements ServletContextListener{
	
	private final Long DEFAULT_START_TIME = 10L;
	private final Long DEFAULT_REPEAT_INTERVAL = 60L;
	private final Long MILLISECONDS_IN_SECOND = 1000L;
	
	private final String CONFIG_FILE_NAME = "/WEB-INF/hosts.xml";
	private final String XML_ELEMENT_HOSTS = "hosts";
	private final String XML_ELEMENT_HOST = "host";
	private final String XML_START_TIME = "start-time";
	private final String XML_REPEAT_INTERVAL = "repeat-interval";
	
	Logger logger = Logger.getLogger(getClass().getName());
	
	private List<String> hosts = null;
	private Long startTime = null;
	private Long repeatInterval = null;
	
    public void contextInitialized(ServletContextEvent p_event){
       logger.info("Initializing Application successfully");
       
       initialize(p_event);
      
       Timer timer=  new Timer();
       
       timer.schedule(new TimerTask(){
           public void run() {
        	   pingHosts();	   
           }
         }, startTime*MILLISECONDS_IN_SECOND, repeatInterval*MILLISECONDS_IN_SECOND);

       }
    
    public void contextDestroyed(ServletContextEvent p_event){
    	logger.info("Stopping Application successfully");
    }
    
    /**
	 * Parses list of hosts from xml configuration file <br>
	 * @param p_event - servlet context event <br>
	 * @return list of parsed hosts; {@link List}<{@link String}> <br>
	 * @throws JDOMException - if parsing exception occur; {@link JDOMException} <br>
	 * @throws IOException - if IO exception occur; {@link IOException}
	 */
	@SuppressWarnings("unchecked")
	private void initialize(ServletContextEvent p_event){
		
		try{
			InputStream stream = p_event.getServletContext().getResourceAsStream(CONFIG_FILE_NAME);
			
	        Element rootElement = new SAXBuilder().build(stream).getRootElement();
	        
	        //check for root element
	        if(rootElement == null){
	        	return;
	        }
	        
	        Element startTimeElement = rootElement.getChild(XML_START_TIME);
	        if(startTimeElement == null){
	        	startTime = DEFAULT_START_TIME;
	        }
	        try{
	        	startTime = Long.parseLong(startTimeElement.getValue());
	        }catch (NumberFormatException error) {
				startTime = DEFAULT_START_TIME;
			}
	        	
	        Element repeatIntervalElement = rootElement.getChild(XML_REPEAT_INTERVAL);
	        if(repeatIntervalElement == null){
	        	repeatInterval = DEFAULT_REPEAT_INTERVAL;
	        }
	        try{
	        	repeatInterval = Long.parseLong(repeatIntervalElement.getValue());
	        }catch(NumberFormatException error){
	        	repeatInterval = DEFAULT_REPEAT_INTERVAL;
	        }
	        	
	        //check for hosts element
	        Element hostsElement = rootElement.getChild(XML_ELEMENT_HOSTS);
	        if(hostsElement == null){
	        	return;
	        }
	        
	        //get hosts list
	        List<Element> hostsElements = hostsElement.getChildren(XML_ELEMENT_HOST);
	        if(CollectionUtils.isEmpty(hostsElements)){
	        	return;
	        }else{
	        	hosts = new ArrayList<String>();
	        	for(Element hostElement : hostsElements){
	        		hosts.add(hostElement.getValue());
	        		logger.info("host: "+hostElement.getValue());
	        	}
	        }
		}catch (JDOMException error) {
			
		}catch (IOException error) {
			
		}
	}
	
	private void pingHosts(){
		logger.info("-- ping start -- ");
		if(CollectionUtils.isNotEmpty(hosts)){
			for(String host : hosts){
				ping(host);
			}
		}
		logger.info("-- ping end -- ");
	}
	
	/**
	 * Ping remote host
	 * @param p_host - host name; {@link String}
	 */
	private void ping(String p_host){
		String message = null;
		try {
			message = URLEncoder.encode("my message", "UTF-8");
		} catch (UnsupportedEncodingException e1) {
			//TODO nothing
		} 
        try {
            URL url = new URL("http://"+p_host);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoOutput(true);
            connection.setRequestMethod("GET");

            OutputStreamWriter writer = new OutputStreamWriter(connection.getOutputStream());
            writer.write("message=" + message);
            writer.close();
    
            if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                logger.info(p_host + " returned response OK"); // OK
            } else {
            	logger.info(p_host + " returned response Failure"); // Server returned HTTP error code.
            }
        } catch (MalformedURLException e) {
            // TODO noting
        } catch (IOException e) {
            // TODO noting
        }
	}
}
