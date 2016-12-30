package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.TreeMap;
import java.util.UUID;

import controller.APIController.RequestType;
import model.Discount;
import model.Route;
import model.Subscription;

public class SubscriptionDAO extends BaseDAO
{
	public final static String BASE_URL = "subscription/";

	public SubscriptionDAO()
	{}
	
	public int insertOrUpdate(Subscription a) {
		Subscription exists = this.selectOne(a.getSubscriptionID().toString());

		if (exists == null)
			return this.insert(a);
		else
			return this.update(a);
	}

	public int insert(Subscription s)
	{
		PreparedStatement ps = null;

		String sql = "INSERT INTO Subscription VALUES(?,?,?,?,?,?,?)";

		try {

			if (getConnection().isClosed()) {
				throw new IllegalStateException("error unexpected");
			}
			ps = getConnection().prepareStatement(sql);

			ps.setString(1, s.getSubscriptionID().toString());
			ps.setString(2, s.getRailCardID().toString());
			ps.setString(3, s.getRouteID().toString());
			ps.setString(4, s.getDiscountID().toString());
			ps.setString(5, s.getValidFrom());
			ps.setString(6, s.getValidUntil());
			ps.setLong(7, s.getLastUpdated());

			if (!isSyncFunction)
			{
				params = new HashMap<String, String>();
				params.put("subscriptionID", s.getSubscriptionID().toString());
				params.put("railCardID", s.getRailCardID().toString());
				params.put("routeID", s.getRouteID().toString());
				params.put("discountID", s.getDiscountID().toString());
				params.put("validFrom", s.getValidFrom());
				params.put("validUntil", s.getValidUntil());
				params.put("lastUpdated", Long.toString(s.getLastUpdated()));
			}

			return ps.executeUpdate();

		}
		catch (SQLException e) {
			System.out.println(e.getMessage());
			throw new RuntimeException(e.getMessage());
		}
		finally {
			try {
				if (ps != null)
					ps.close();
				if (!isSyncFunction)
					syncMainDB(BASE_URL + "create", RequestType.POST, params);

			}
			catch (SQLException e) {
				System.out.println(e.getMessage());
				throw new RuntimeException("error.unexpected");
			}
		}

	}
	
	public int update(Subscription s) {
		PreparedStatement ps = null;

		String sql = "UPDATE Subscription SET `RailCardID`=?, `RouteID`=?, `DiscountID`=?, `ValidFrom`=?, `ValidUntil`=?, `LastUpdated`=? WHERE SubscriptionID = ?";

		try {

			if (getConnection().isClosed()) {
				throw new IllegalStateException("error unexpected");
			}
			ps = getConnection().prepareStatement(sql);

			ps.setString(1, s.getRailCardID().toString());
			ps.setString(2, s.getRouteID().toString());
			ps.setString(3, s.getDiscountID().toString());
			ps.setString(4, s.getValidFrom());
			ps.setString(5, s.getValidUntil());
			ps.setLong(6, s.getLastUpdated());
			ps.setString(7, s.getSubscriptionID().toString());

			if (!isSyncFunction)
			{
				params = new HashMap<String, String>();
				params.put("subscriptionID", s.getSubscriptionID().toString());
				params.put("railCardID", s.getRailCardID().toString());
				params.put("routeID", s.getRouteID().toString());
				params.put("discountID", s.getDiscountID().toString());
				params.put("validFrom", s.getValidFrom());
				params.put("validUntil", s.getValidUntil());
				params.put("lastUpdated", Long.toString(s.getLastUpdated()));
			}

			return ps.executeUpdate();

		} catch (SQLException e) {
			System.out.println(e.getMessage());
			throw new RuntimeException(e.getMessage());
		} finally {
			try {
				if (ps != null)
					ps.close();
				if (!isSyncFunction)
					syncMainDB(BASE_URL + "update/" + params.get("subscriptionID"), RequestType.PUT, params);
				
			} catch (SQLException e) {
				System.out.println(e.getMessage());
				throw new RuntimeException("error.unexpected");
			}
		}
	}
	
	public TreeMap<String, String> updateStatus() {
		TreeMap<String, String> map = null;

		PreparedStatement ps = null;
		ResultSet rs = null;

		String sql = "SELECT COUNT(DISTINCT SubscriptionID) as Count, MAX(LastUpdated) as LastUpdated FROM Subscription";

		try {

			if (getConnection().isClosed()) {
				throw new IllegalStateException("error unexpected");
			}
			ps = getConnection().prepareStatement(sql);

			rs = ps.executeQuery();
			map = new TreeMap<String, String>();

			while (rs.next()) {
				map.put("Count", rs.getString("Count"));
				map.put("LastUpdated", rs.getString("LastUpdated"));
			}

			return map;
		} catch (SQLException e) {
			System.out.println(e.getMessage());
			throw new RuntimeException(e.getMessage());
		} finally {
			try {
				if (ps != null)
					ps.close();
				if (rs != null)
					rs.close();
			} catch (SQLException e) {
				System.out.println(e.getMessage());
				throw new RuntimeException("error.unexpected");
			}
		}
	}

	public ArrayList<Subscription> selectAllSync()
	{
		ArrayList<Subscription> list = null;

		PreparedStatement ps = null;
		ResultSet rs = null;

		String sql = "SELECT * FROM LostObject";

		try {

			if (getConnection().isClosed()) {
				throw new IllegalStateException("error unexpected");
			}
			ps = getConnection().prepareStatement(sql);

			rs = ps.executeQuery();
			list = new ArrayList<Subscription>();

			while (rs.next()) {
				list.add(syncResultToModel(rs));
			}

			return list;
		}
		catch (SQLException e) {
			System.out.println(e.getMessage());
			throw new RuntimeException(e.getMessage());
		}
		finally {
			try {
				if (ps != null)
					ps.close();
				if (rs != null)
					rs.close();
			}
			catch (SQLException e) {
				System.out.println(e.getMessage());
				throw new RuntimeException("error.unexpected");
			}
		}

	}

	public ArrayList<Subscription> selectAll()
	{
		ArrayList<Subscription> list = null;

		PreparedStatement ps = null;
		ResultSet rs = null;

		String sql = "SELECT s.SubscriptionID, c.CardID, c.LastUpdated as RailCardLastUpdated, "
				+ "r.RouteID, "
				+ "r.DepartureStationID, s1.Name as DepartureName, s1.CoX as DepartureCoX, s1.CoY as DepartureCoY, s1.LastUpdated as DepartureLastUpdated,"
				+ "r.ArrivalStationID, s2.Name as ArrivalName, s2.CoX as ArrivalCoX, s2.CoY as ArrivalCoY, s2.LastUpdated as ArrivalLastUpdated, r.LastUpdated as RouteLastUpdated, "
				+ "r.LastUpdated as RouteLastUpdated, "
				+ "d.DiscountID, d.Name as DiscountName, d.Amount as DiscountAmount, d.LastUpdated as DiscountLastUpdated, "
				+ "s.ValidFrom, s.ValidUntil, s.LastUpdated as SubscriptionLastUpdated " 
				+ "FROM Subscription s "
				+ "INNER JOIN RailCard c ON c.CardID = s.RailCardID "
				+ "INNER JOIN Route r ON r.RouteID = s.RouteID "
				+ "INNER JOIN Station s1 on s1.StationID = r.DepartureStationID "
				+ "INNER JOIN Station s2 on s2.StationID = r.ArrivalStationID "
				+ "INNER JOIN Discount d ON d.DiscountID = s.DiscountID;";

		try {

			if (getConnection().isClosed()) {
				throw new IllegalStateException("error unexpected");
			}
			ps = getConnection().prepareStatement(sql);

			rs = ps.executeQuery();
			list = new ArrayList<Subscription>();

			while (rs.next()) {
				list.add(resultToModel(rs));
			}

			return list;
		}
		catch (SQLException e) {
			System.out.println(e.getMessage());
			throw new RuntimeException(e.getMessage());
		}
		finally {
			try {
				if (ps != null)
					ps.close();
				if (rs != null)
					rs.close();
			}
			catch (SQLException e) {
				System.out.println(e.getMessage());
				throw new RuntimeException("error.unexpected");
			}
		}

	}

	public Subscription selectOne(String subscriptionID)
	{
		PreparedStatement ps = null;
		ResultSet rs = null;

		String sql = "SELECT s.SubscriptionID, c.CardID, c.LastUpdated as RailCardLastUpdated, "
				+ "r.RouteID, "
				+ "r.DepartureStationID, s1.Name as DepartureName, s1.CoX as DepartureCoX, s1.CoY as DepartureCoY, s1.LastUpdated as DepartureLastUpdated,"
				+ "r.ArrivalStationID, s2.Name as ArrivalName, s2.CoX as ArrivalCoX, s2.CoY as ArrivalCoY, s2.LastUpdated as ArrivalLastUpdated, r.LastUpdated as RouteLastUpdated, "
				+ "r.LastUpdated as RouteLastUpdated, "
				+ "d.DiscountID, d.Name as DiscountName, d.Amount as DiscountAmount, d.LastUpdated as DiscountLastUpdated, "
				+ "s.ValidFrom, s.ValidUntil, s.LastUpdated as SubscriptionLastUpdated " 
				+ "FROM Subscription s "
				+ "INNER JOIN RailCard c ON c.CardID = s.RailCardID "
				+ "INNER JOIN Route r ON r.RouteID = s.RouteID "
				+ "INNER JOIN Station s1 on s1.StationID = r.DepartureStationID "
				+ "INNER JOIN Station s2 on s2.StationID = r.ArrivalStationID "
				+ "INNER JOIN Discount d ON d.DiscountID = s.DiscountID "
				+ "Where s.SubscriptionID =?;";
		try {

			if (getConnection().isClosed()) {
				throw new IllegalStateException("error unexpected");
			}
			ps = getConnection().prepareStatement(sql);

			ps.setString(1, subscriptionID);
			rs = ps.executeQuery();
			if (rs.next())
				return resultToModel(rs);
			else
				return null;
		}
		catch (SQLException e) {
			System.out.println(e.getMessage());
			throw new RuntimeException(e.getMessage());
		}
		finally {
			try {
				if (ps != null)
					ps.close();
				if (rs != null)
					rs.close();
			}
			catch (SQLException e) {
				System.out.println(e.getMessage());
				throw new RuntimeException("error.unexpected");
			}
		}
	}
	
	private Subscription resultToModel(ResultSet rs) throws SQLException
	{
		Subscription s = new Subscription();
		Route r = new Route();
		Discount d = new Discount();
		
		r = RouteDAO.resultToModel(rs);
		d = DiscountDAO.resultToModel(rs);

		s.setSubscriptionID(UUID.fromString(rs.getString("SubscriptionID")));
		s.setRailCardID(UUID.fromString(rs.getString("CardID")));
		s.setRoute(r);
		s.setDiscount(d);
		s.setValidFrom(rs.getString("ValidFrom"));
		s.setValidUntil(rs.getString("ValidUntil"));
		s.setLastUpdated(rs.getLong("SubscriptionLastUpdated"));

		return s;
	}
	
	private Subscription syncResultToModel(ResultSet rs) throws SQLException
	{
		Subscription s = new Subscription();

		s.setSubscriptionID(UUID.fromString(rs.getString("SubscriptionID")));
		s.setRailCardID(UUID.fromString(rs.getString("CardID")));
		s.setRouteID(UUID.fromString(rs.getString("RouteID")));
		s.setDiscountID(UUID.fromString(rs.getString("DiscountID")));
		s.setValidFrom(rs.getString("ValidFrom"));
		s.setValidUntil(rs.getString("ValidUntil"));
		s.setLastUpdated(rs.getLong("LastUpdated"));

		return s;
	}

	public static void createTable(Connection con)
	{
		PreparedStatement ps = null;

		String sql = "CREATE TABLE IF NOT EXISTS `Subscription` ("
				+ "`SubscriptionID` varchar(36) NOT NULL DEFAULT '0',"
				+ "`RailCardID` varchar(36) NOT NULL DEFAULT '0'," 
				+ "`RouteID` varchar(36) NOT NULL DEFAULT '0',"
				+ "`DiscountID` varchar(36) NOT NULL DEFAULT '0'," 
				+ "`ValidFrom` varchar(20) NOT NULL,"
				+ "`ValidUntil` varchar(20) NOT NULL," 
				+ "`LastUpdated` bigint(14) NOT NULL,"
				+ "PRIMARY KEY (`SubscriptionID`)"
				+ ");";

		try {

			if (con.isClosed()) {
				throw new IllegalStateException("error unexpected");
			}
			ps = con.prepareStatement(sql);
			ps.executeUpdate();
		}
		catch (SQLException e) {
			System.out.println(e.getMessage());
			throw new RuntimeException(e.getMessage());
		}
		finally {
			try {
				if (ps != null)
					ps.close();
			}
			catch (SQLException e) {
				System.out.println(e.getMessage());
				throw new RuntimeException("error.unexpected");
			}
		}
	}

}