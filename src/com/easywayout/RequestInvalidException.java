// Written by: Armani Araujo

package com.easywayout;

import javax.servlet.ServletException;

public class RequestInvalidException extends ServletException
{
	
	public RequestInvalidException()
	{
		
	}

	public RequestInvalidException(String message)
	{
		super(message);
	}

	public RequestInvalidException(Throwable rootCause)
	{
		super(rootCause);
	}

	public RequestInvalidException(String message, Throwable rootCause)
	{
		super(message, rootCause);
	}	
}
