// Written by: Armani Araujo

package com.easywayout;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

import javax.servlet.DispatcherType;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import ca.on.senecac.prg556.common.StringHelper;
import ca.on.senecac.prg556.senhotel.bean.Guest;
import ca.on.senecac.prg556.senhotel.bean.Reservation;
import ca.on.senecac.prg556.senhotel.dao.GuestDAO;

import com.easywayout.data.ReservationDAOFactory;
import com.profoundsadness.data.GuestDAOFactory;

/**
 * Servlet Filter implementation class WelcomeFilter
 */
public class WelcomeFilter implements Filter {

    /**
     * Default constructor. 
     */
    public WelcomeFilter() {
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
	public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain) throws IOException, ServletException 
	{
		
		String username;
		String password;

		HttpServletRequest request = (HttpServletRequest)req;
		HttpServletResponse response = (HttpServletResponse)resp;
		HttpSession session = request.getSession();
		
		try 
		{			
			Guest tempUser = (Guest) session.getAttribute("user");
			
			if(null == tempUser)
			{
				username = req.getParameter("username");
				password = req.getParameter("password");
				
				if ("POST".equals(request.getMethod()) && StringHelper.isNotNullOrEmpty(username) && StringHelper.isNotNullOrEmpty(password)) // request.getMethod requires an HttpServletRequest object
				{	
					Guest user = GuestDAOFactory.getGuestDAO().authenticateGuest(username, password);
					
					if(user != null)
					{
						session.setAttribute("user", user); // save new list in session-scope
						response.sendRedirect(request.getContextPath() + "/");		
					}
					else
					{
						request.setAttribute("error", 1);
						chain.doFilter(req, resp);
					}		
				}
				else
				{
					chain.doFilter(req, resp);	
				}
			}			
		} catch (SQLException sqle) {
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
