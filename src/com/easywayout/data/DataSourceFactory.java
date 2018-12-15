// Written by: Armani Araujo

package com.easywayout.data;

import javax.naming.InitialContext;
import javax.sql.DataSource;

import com.sun.jndi.ldap.Connection;

public class DataSourceFactory 
{
	private static DataSource dataSource;
	
	public static DataSource getDataSource()
	{
		return dataSource;	
	}
	
	public static void setDataSource(DataSource dataSource)
	{
		DataSourceFactory.dataSource = dataSource;
	}
	
	
}
