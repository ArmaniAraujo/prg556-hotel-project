//Written by: Haley Chen
package com.profoundsadness;

import java.io.IOException;
import java.sql.SQLException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import ca.on.senecac.prg556.senhotel.bean.Guest;

import com.profoundsadness.data.GuestDAOFactory;

/**
 * Servlet Filter implementation class SenHotelMenuFilter
 */
public class SenHotelMenuFilter implements Filter
{
    /**
     * Default constructor. 
     */
    public SenHotelMenuFilter()
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
		try
		{
			HttpServletRequest req = (HttpServletRequest)request;
			
			int guestId = ((Guest)req.getSession().getAttribute("user")).getId();
			request.setAttribute("numRevs", GuestDAOFactory.getGuestDAO().getReservations(guestId).size());

			chain.doFilter(request, response);
		}
		catch (SQLException sqle)
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
