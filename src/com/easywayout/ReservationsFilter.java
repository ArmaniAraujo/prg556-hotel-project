// Written by: Armani Araujo

package com.easywayout;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.DispatcherType;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import ca.on.senecac.prg556.common.StringHelper;
import ca.on.senecac.prg556.senhotel.bean.Guest;
import ca.on.senecac.prg556.senhotel.bean.Reservation;
import ca.on.senecac.prg556.senhotel.dao.GuestDAO;

import com.easywayout.data.ReservationDAOFactory;
import com.profoundsadness.data.GuestDAOFactory;

/**
 * Servlet Filter implementation class ReservationsFilter
 */
public class ReservationsFilter implements Filter {

    /**
     * Default constructor. 
     */
    public ReservationsFilter() {
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see Filter#destroy()
	 */
	public void destroy() {
		// TODO Auto-generated method stub
	}

	/**
	 * @see Filter#doFilter(ServletRequest, ServletResponse, FilterChain)
	 */
	public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain) throws IOException, ServletException {
		
		HttpServletRequest request = (HttpServletRequest)req;
		HttpSession session = request.getSession();
		
		String tempRoomNum = "";
		int roomNum = 0;
		Reservation tempRes = null;
		Guest user = (Guest) session.getAttribute("user"); 
		int userId = user.getId();
		
		
		try {
			
			List<Reservation> resList = GuestDAOFactory.getGuestDAO().getReservations(userId);
			List<Integer> roomList = new ArrayList<Integer>();
			List<Integer> floorList = new ArrayList<Integer>();
			
			for(int i = 0; i < resList.size(); i++)
			{
				// GETS ALL ROOM NUMBERS IN THE RESLIST OBJECT AND ADDS IT TO ROOMLIST TO SEND TO JSPX
				roomList.add(resList.get(i).getRoomNo());
				//roomList.add(Integer.toString(resList.get(i).getRoomNo()));
				System.out.println(roomList.get(i));
				
				String tempRoom = roomList.get(i).toString();
				
				int number = Integer.parseInt(tempRoom);
				number /= 100;
				System.out.println("number: "+number);
				floorList.add(number);
				
			//	floorList.add(Integer.parseInt(tempRoom.substring(0, 1)));
				System.out.println("Floor #: " + floorList.get(i));
			}
			
			req.setAttribute("resList", resList);
			req.setAttribute("roomList", roomList);
			req.setAttribute("floorList", floorList);
			
			if ("POST".equals(request.getMethod())&& StringHelper.isNotNullOrEmpty(req.getParameter("cancel"))) // request.getMethod requires an HttpServletRequest object
			{
				try 
				{
				tempRoomNum = req.getParameter("cancel_booking");
				}
				catch(NumberFormatException nfe)
				{
					throw new RequestInvalidException(nfe);
				}
				
				if (StringHelper.isNullOrEmpty((tempRoomNum)))
				{
					System.out.println("hi!");
					throw new RequestInvalidException();
				}
				else			
				{
					roomNum = Integer.parseInt(tempRoomNum);
	
				}
			
				tempRes = ReservationDAOFactory.getReservationDAO().getReservation(roomNum);
				
				if (tempRes == null)
				{
					throw new RequestInvalidException();		
				}
				
				else
				{
					GuestDAOFactory.getGuestDAO().cancelReservation(roomNum, userId);
					System.out.println("Room number is: " + roomNum);
					
					resList = new GuestDAOFactory().getGuestDAO().getReservations(userId);
					roomList = new ArrayList<Integer>();
					floorList = new ArrayList<Integer>();
					
					for(int i = 0; i < resList.size(); i++)
					{
						// GETS ALL ROOM NUMBERS IN THE RESLIST OBJECT AND ADDS IT TO ROOMLIST TO SEND TO JSPX
						roomList.add(resList.get(i).getRoomNo());
						//roomList.add(Integer.toString(resList.get(i).getRoomNo()));
						System.out.println(roomList.get(i));
						
						String tempRoom = roomList.get(i).toString();
						
						int number = Integer.parseInt(tempRoom);
						number /= 100;
						System.out.println("number: "+number);
						floorList.add(number);
						
				//		floorList.add(Integer.parseInt(tempRoom.substring(0, 1)));
						System.out.println("Floor #: " + floorList.get(i));
					}
					
					req.setAttribute("resList", resList);
					req.setAttribute("roomList", roomList);
					req.setAttribute("floorList", floorList);
				}
			}	

			chain.doFilter(req, resp);
			
		} catch (SQLException sqle) 
		{
			throw new ServletException(sqle);
		}
	}


	/**
	 * @see Filter#init(FilterConfig)
	 */
	public void init(FilterConfig fConfig) throws ServletException {
		// TODO Auto-generated method stub
	}

}
