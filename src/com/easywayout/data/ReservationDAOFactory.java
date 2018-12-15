// Written by: Armani Araujo

package com.easywayout.data;

import ca.on.senecac.prg556.senhotel.dao.ReservationDAO;
import ca.on.senecac.prg556.senhotel.dao.GuestDAO;
import com.easywayout.data.DataSourceFactory;
import javax.sql.DataSource;


public class ReservationDAOFactory 
{

	public static ReservationDAO getReservationDAO() 
	{
		return new ReservationData(DataSourceFactory.getDataSource());
	}

}






