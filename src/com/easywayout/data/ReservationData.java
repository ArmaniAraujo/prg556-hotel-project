// Written by: Armani Araujo

package com.easywayout.data;

import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import javax.servlet.ServletException;
import javax.sql.DataSource;

import ca.on.senecac.prg556.senhotel.bean.Guest;
import ca.on.senecac.prg556.senhotel.bean.Reservation;
import ca.on.senecac.prg556.senhotel.dao.ReservationDAO;
import ca.on.senecac.prg556.senhotel.dao.GuestDAO;

class ReservationData implements ReservationDAO
{
	private DataSource ds;
	ReservationData(DataSource ds)				// ReservationDAOFactory doesnt work without this constructor
	{
		setDs(ds);
	}
	
	public ReservationData() 					// not sure about this
	{
		// TODO Auto-generated constructor stub
	}
	
	private void setDs(DataSource ds) 
	{
		// TODO Auto-generated method stub
		this.ds = ds;
	}
	private DataSource getDs()
	{
		return ds;
	}

	@Override
	public Reservation createReservation(int roomNo, int guestId) throws SQLException {
		
		try (Connection conn = getDs().getConnection())
		{
		
		//	Reservation res = new Reservation(roomNo, guestId);
		//	return res;
			
			try (Statement cmd = conn.createStatement(ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_UPDATABLE))
			{		
				try (ResultSet resSet = cmd.executeQuery("SELECT * FROM reservations"))
				{
					resSet.moveToInsertRow();
					resSet.updateInt("roomno", roomNo);
					resSet.updateInt("guestid", guestId);
					resSet.insertRow();
					
					return new Reservation(roomNo, guestId);
				}
			}
		}	
	}

	@Override
	public Reservation getReservation(int roomNo) throws SQLException 
	{
		try (Connection conn = getDs().getConnection())
		{
			try (PreparedStatement statement = conn.prepareStatement("SELECT roomno, guestid FROM reservations WHERE roomno = ?"))
			{
				statement.setInt(1, roomNo);		// sets ? to roomNo
				try (ResultSet resSet = statement.executeQuery())
				{
					if (resSet.next())
						return new Reservation(resSet.getInt("roomno"),resSet.getInt("guestid"));
					else
						return null;
				}
			}
		}	
	}

	@Override
	public List<Reservation> getReservations() throws SQLException 
	{
		
		try(Connection conn = getDs().getConnection())
		{
			try (PreparedStatement cmd = conn.prepareStatement("SELECT roomno, guestid FROM reservations"))
			{
				try(ResultSet resSet = cmd.executeQuery())
				{
					List<Reservation> tempList = new ArrayList<Reservation>();
					
					while (resSet.next())
					{	
						int roomNo = resSet.getInt("roomno");
						int guestId = resSet.getInt("guestid");
						
						tempList.add(new Reservation(roomNo, guestId));
					}
					return tempList;
				}
			}
		}
	}

	@Override
	public boolean isReserved(int roomNo) throws SQLException 
	{
		if(getReservation(roomNo) != null)
			return true;
		else
			return false;
	}
}
