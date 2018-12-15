//Written by: Haley Chen
package com.profoundsadness;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import ca.on.senecac.prg556.common.StringHelper;

/**
 * Servlet Filter implementation class LoginValidationFilter
 */
public class LoginValidationFilter implements Filter
{
	private String welcomePage = "/welcome.jspx";
	
    /**
     * Default constructor. 
     */
    public LoginValidationFilter()
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
		HttpServletRequest req = (HttpServletRequest)request;
		String uriLogin = req.getContextPath() + getWelcomePage();
		
		if (null == req.getSession().getAttribute("user") && !uriLogin.equals(req.getRequestURI()))
			((HttpServletResponse)response).sendRedirect(uriLogin);
		
		else
			chain.doFilter(request, response);
	}

	/**
	 * @see Filter#init(FilterConfig)
	 */
	public void init(FilterConfig fConfig) throws ServletException
	{
		if (StringHelper.isNotNullOrEmpty(fConfig.getInitParameter("welcome")))
			setWelcomePage(StringHelper.stringPrefix(fConfig.getInitParameter("welcome"), "/"));
	}
	
	public synchronized String getWelcomePage()
	{
		return welcomePage;
	}

	private synchronized void setWelcomePage(String welcomePage)
	{
		this.welcomePage = welcomePage;
	}
}
