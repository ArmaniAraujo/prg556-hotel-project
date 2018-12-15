//Written by: Haley Chen

package com.profoundsadness.data;

import ca.on.senecac.prg556.senhotel.dao.GuestDAO;
import com.easywayout.data.DataSourceFactory;

public class GuestDAOFactory
{
	public static GuestDAO getGuestDAO()
	{
		return new GuestData(DataSourceFactory.getDataSource());
	}
}