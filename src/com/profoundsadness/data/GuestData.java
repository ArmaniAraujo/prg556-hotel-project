//Written by: Haley Chen

package com.profoundsadness.data;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import ca.on.senecac.prg556.common.StringHelper;
import ca.on.senecac.prg556.senhotel.bean.Guest;
import ca.on.senecac.prg556.senhotel.bean.Reservation;
import ca.on.senecac.prg556.senhotel.dao.GuestDAO;

class GuestData implements GuestDAO
{	
	private DataSource ds;

	GuestData(DataSource ds)
	{
		super();
		setDs(ds);
	}
	/**
	 * @return the ds
	 */
	private DataSource getDs()
	{
		return ds;
	}
	/**
	 * @param ds the ds to set
	 */
	private void setDs(DataSource ds)
	{
		this.ds = ds;
	}
	
	@Override
	public Guest authenticateGuest(String username, String password) throws SQLException
	{
		if (StringHelper.isNotNullOrEmpty(username) && StringHelper.isNotNullOrEmpty(password))
		{
			try (Connection conn = getDs().getConnection())
			{
				try (PreparedStatement statement = conn.prepareStatement("SELECT id, firstname, lastname FROM guests WHERE username = ? AND password = ?"))
				{
					statement.setString(1, username);
					statement.setString(2, password);
					try (ResultSet rs = statement.executeQuery())
					{
						if (rs.next())
							return new Guest(rs.getInt("id"), rs.getString("firstname"), rs.getString("lastname"));
					}
				}
			}
		}

		return null;
	}
	
	public void cancelReservation(int roomNo, int guestId) throws SQLException
	{
		try (Connection conn = getDs().getConnection())
		{
			try (PreparedStatement statement = conn.prepareStatement("SELECT roomno FROM reservations WHERE guestid = ? and roomno = ?", ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_UPDATABLE))
			{
				statement.setInt(1, guestId);
				statement.setInt(2, roomNo);
				try (ResultSet rs = statement.executeQuery())
				{
					if (rs.next())
					{
						rs.deleteRow();
					}
				}
			}
		}
	}	
	
	@Override
	public Guest getGuest(int guestId) throws SQLException
	{
		try (Connection conn = getDs().getConnection())
		{
			try (PreparedStatement statement = conn.prepareStatement("SELECT firstname, lastname FROM guests WHERE id = ?"))
			{
				statement.setInt(1, guestId);
				try (ResultSet rs = statement.executeQuery())
				{
					if (rs.next())
						return new Guest(guestId, rs.getString("firstname"), rs.getString("lastname"));
					else
						return null;
				}
			}
		}
	}
	
	public List<Reservation> getReservations(int guestId) throws SQLException
	{
		try (Connection conn = getDs().getConnection())
		{
			try (PreparedStatement statement = conn.prepareStatement("SELECT roomno, guestid FROM reservations INNER JOIN guests ON reservations.guestid = guests.id WHERE guestid = ?"))
			{
				statement.setInt(1, guestId);
				
				try (ResultSet rs = statement.executeQuery())
				{
					List<Reservation> revList = new ArrayList<>();
					
					while (rs.next())
					{
						revList.add(new Reservation(rs.getInt("roomno"), rs.getInt("guestid")));
					}
					
					return revList;
				}
			}
		}
	}
}