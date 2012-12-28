package com.tormozz.hostping;

import java.io.File;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;

import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.PropertyConfigurator;

/**
 * Main application servlet <br>
 * @author Created by andrey.kuznetsov lt <br>
 * @author Last modified by $Author$ <br>
 * @author Last modified on $Date$ at revision $Revision$ <br>
 */
@WebServlet(name = "InitServlet")
public class InitServlet extends HttpServlet {

	private static final long serialVersionUID = 7060972055709014587L;
	
	private final String PROPERTY_FILE_NAME = "log4j-properties-location";
	
	public void init(ServletConfig config) throws ServletException {
		String log4jLocation = config.getInitParameter(PROPERTY_FILE_NAME);

		ServletContext servletContext = config.getServletContext();
		if (log4jLocation == null) {
			BasicConfigurator.configure();
		} else {
			String path = servletContext.getRealPath("/") + log4jLocation;
			File file = new File(path);
			if (file.exists()) {
				PropertyConfigurator.configure(path);
			} else {
				BasicConfigurator.configure();
			}
		}
		
		//TimerPinger.getInstance(servletContext);
		QuartzPinger.getInstance(servletContext);
		
		super.init(config);
	}
}
