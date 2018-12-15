//Written by: Haley Chen
package com.profoundsadness;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.easywayout.RequestInvalidException;
import com.easywayout.data.ReservationDAOFactory;

import ca.on.senecac.prg556.common.StringHelper;
import ca.on.senecac.prg556.senhotel.bean.Guest;
import ca.on.senecac.prg556.senhotel.bean.Hotel;
import ca.on.senecac.prg556.senhotel.bean.Reservation;

/**
 * Servlet Filter implementation class ReserveRoomFilter
 */
public class ReserveRoomFilter implements Filter
{
    /**
     * Default constructor. 
     */
    public ReserveRoomFilter()
    {
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see Filter#destroy()
	 */
	public void destroy()
	{
		// TODO Auto-generated method stub
	}

	/**
	 * @see Filter#doFilter(ServletRequest, ServletResponse, FilterChain)
	 */
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException
	{
		int floor, unit;
		HttpServletRequest req = (HttpServletRequest)request;
		HttpServletResponse resp = (HttpServletResponse)response;
		
		ServletContext servletContext = request.getServletContext();
		Hotel htl = (Hotel)servletContext.getAttribute("Hotel");
		
		try
		{
			if ("POST".equals(req.getMethod()) && StringHelper.isNotNullOrEmpty((String)request.getParameter("submit")))
			{
				try
				{
					floor = Integer.parseInt(request.getParameter("available_room_floor"));
					unit = Integer.parseInt(request.getParameter("available_room_unit"));
				}
			
				catch (NumberFormatException nfe)
				{
					throw new RequestInvalidException(nfe);
				}
				
				if ((floor < 1 || floor > ((Hotel)htl).getFloors()) || (unit < 1 || unit > ((Hotel)htl).getRoomsPerFloor()))
					throw new RequestInvalidException();
				
				else
				{
					synchronized (this)
					{
						boolean isReserved;
						int roomNum = (floor * 100) + unit;
					
						isReserved = ReservationDAOFactory.getReservationDAO().isReserved(roomNum);
					
						if (isReserved == false)
						{
							int guestId = ((Guest) req.getSession().getAttribute("user")).getId();
							Reservation reserveRoom = ReservationDAOFactory.getReservationDAO().createReservation(roomNum, guestId);
							
							resp.sendRedirect(req.getContextPath() + "/");
						}
						
						else
						{
							List<Reservation> resList = ReservationDAOFactory.getReservationDAO().getReservations();
							Reservation[][] resArr = new Reservation[((Hotel)htl).getFloors()][((Hotel)htl).getRoomsPerFloor()];
						
							for (Reservation i: resList)
							{
								int currentFloor = i.getRoomNo() / 100;
								int currentUnit = i.getRoomNo() % 100;
							
								resArr[currentFloor-1][currentUnit-1] = i;
							}
						
							request.setAttribute("resArr", resArr);
						
							chain.doFilter(request, response);
						}
					}
				}
			}
			
			else
			{
				List<Reservation> resList = ReservationDAOFactory.getReservationDAO().getReservations();
				Reservation[][] resArr = new Reservation[((Hotel)htl).getFloors()][((Hotel)htl).getRoomsPerFloor()];
				
				for (Reservation i: resList)
				{
					int currentFloor = i.getRoomNo() / 100;
					int currentUnit = i.getRoomNo() % 100;
				
					resArr[currentFloor-1][currentUnit-1] = i;
				}
				
				request.setAttribute("resArr", resArr);
				
				chain.doFilter(request, response);
			}
		}
		
		catch(SQLException sqle)
		{
		   throw new ServletException(sqle);
		}
	}

	/**
	 * @see Filter#init(FilterConfig)
	 */
	public void init(FilterConfig fConfig) throws ServletException
	{
		// TODO Auto-generated method stub
	}

}
