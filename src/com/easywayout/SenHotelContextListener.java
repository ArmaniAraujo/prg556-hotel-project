// Written by: Armani Araujo

package com.easywayout;

import javax.annotation.Resource;
import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

import ca.on.senecac.prg556.senhotel.bean.Guest;
import ca.on.senecac.prg556.senhotel.bean.Hotel;

import javax.sql.DataSource;

import com.easywayout.data.DataSourceFactory;


/**
 * Application Lifecycle Listener implementation class SenHotelContextListener
 *
 */
@WebListener
public class SenHotelContextListener implements ServletContextListener 
{

	@Resource(name="SenHotelDB")
	private DataSource ds;
	
	@Override			// needed?
    public void contextDestroyed(ServletContextEvent arg0) 
    {
    	DataSourceFactory.setDataSource(null);
    }

	/**
     * @see ServletContextListener#contextInitialized(ServletContextEvent)
     */
	@Override			// needed?
    public void contextInitialized(ServletContextEvent sce) 
    {
    	try
    	
		{
    		DataSourceFactory.setDataSource(ds);
    		
    		ServletContext context = sce.getServletContext();
    		
			String hotelName = context.getInitParameter("hotelName");
			
			System.out.println(hotelName);
			int numFloors = Integer.parseInt(context.getInitParameter("numFloors"));
			System.out.println(numFloors);
			int numRooms = Integer.parseInt(context.getInitParameter("numRooms"));
			System.out.println(numRooms);
			
			Hotel htl = new Hotel(hotelName, numFloors, numRooms);
			
			sce.getServletContext().setAttribute("Hotel", htl);

		}
		catch (Exception e)
		{
			// Do nothing, leave default value
		}
    }
    

    public SenHotelContextListener() 
    {
        // TODO Auto-generated constructor stub
    }

}
