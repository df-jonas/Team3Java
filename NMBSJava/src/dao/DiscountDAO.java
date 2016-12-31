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


public class DiscountDAO extends BaseDAO
{
	public final static String BASE_URL = "discount/";

	public DiscountDAO()
	{

	}
	
	public int insertOrUpdate(Discount d)
	{
		Discount exists = this.selectOne(d.getDiscountID().toString());

		if (exists == null)
			return this.insert(d);
		else
			return this.update(d);
	}
	
	public int insert(Discount d)
	{
		PreparedStatement ps = null;

		String sql = "INSERT INTO Discount VALUES(?,?,?,?)";

		try {

			if (getConnection().isClosed()) {
				throw new IllegalStateException("error unexpected");
			}
			ps = getConnection().prepareStatement(sql);

			ps.setString(1, d.getDiscountID().toString());
			ps.setString(2, d.getName());
			ps.setDouble(3, d.getAmount());
			ps.setLong(4, d.getLastUpdated());

			if (!isSyncFunction)
			{
				params = new HashMap<String, String>();
				params.put("discountID", d.getDiscountID().toString());
				params.put("name", d.getName());
				params.put("amount", Double.toString(d.getAmount()));
				params.put("lastUpdated", Long.toString(d.getLastUpdated()));
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
					syncMainDB(BASE_URL + "create", RequestType.POST, params);

			} catch (SQLException e) {
				System.out.println(e.getMessage());
				throw new RuntimeException("error.unexpected");
			}
		}
	}
	public int update(Discount d)
	{
		PreparedStatement ps = null;

		String sql = "UPDATE `Discount` SET `Name`=?,"
				+ "`Amount`=?,`LastUpdated`=? WHERE DiscountID=?;";

		try {

			if (getConnection().isClosed()) {
				throw new IllegalStateException("error unexpected");
			}
			ps = getConnection().prepareStatement(sql);
			
			ps.setString(1, d.getName());
			ps.setDouble(2, d.getAmount());
			ps.setLong(3, d.getLastUpdated());
			ps.setString(4, d.getDiscountID().toString());

			if (!isSyncFunction)
			{
				params = new HashMap<String, String>();
				params.put("discountID", d.getDiscountID().toString());
				params.put("name", d.getName());
				params.put("amount", Double.toString(d.getAmount()));
				params.put("lastUpdated", Long.toString(d.getLastUpdated()));
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
					syncMainDB(BASE_URL + "update/" + params.get("discountID"), RequestType.PUT, params);

			}
			catch (SQLException e) {
				System.out.println(e.getMessage());
				throw new RuntimeException("error.unexpected");
			}
		}

	}

	public ArrayList<Discount> selectAll()
	{
		ArrayList<Discount> list = null;

		PreparedStatement ps = null;
		ResultSet rs = null;

		String sql = "SELECT d.DiscountID, d.Name as DiscountName, d.Amount as DiscountAmount, d.LastUpdated as DiscountLastUpdated FROM Discount d";

		try {

			if (getConnection().isClosed()) {
				throw new IllegalStateException("error unexpected");
			}
			ps = getConnection().prepareStatement(sql);

			rs = ps.executeQuery();
			list = new ArrayList<Discount>();

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

	public TreeMap<String, String> updateStatus()
	{
		TreeMap<String, String> map = null;

		PreparedStatement ps = null;
		ResultSet rs = null;

		String sql = "SELECT COUNT(DISTINCT DiscountID) as Count, MAX(LastUpdated) as LastUpdated FROM Discount";

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

	public Discount selectOne(String discountID)
	{
		PreparedStatement ps = null;
		ResultSet rs = null;

		String sql = "SELECT d.DiscountID, d.Name as DiscountName, d.Amount as DiscountAmount, d.LastUpdated as DiscountLastUpdated FROM Discount d WHERE DiscountID=?";

		try {

			if (getConnection().isClosed()) {
				throw new IllegalStateException("error unexpected");
			}
			ps = getConnection().prepareStatement(sql);

			ps.setString(1, discountID);
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
	
	public Discount selectOneOnName(String name)
	{
		PreparedStatement ps = null;
		ResultSet rs = null;

		String sql = "SELECT d.DiscountID, d.Name as DiscountName, d.Amount as DiscountAmount, d.LastUpdated as DiscountLastUpdated FROM Discount d WHERE d.Name=?";

		try {

			if (getConnection().isClosed()) {
				throw new IllegalStateException("error unexpected");
			}
			ps = getConnection().prepareStatement(sql);

			ps.setString(1, name);
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


	public static Discount resultToModel(ResultSet rs) throws SQLException
	{
		Discount d = new Discount();
		d.setDiscountID(UUID.fromString(rs.getString("DiscountID")));
		d.setName(rs.getString("DiscountName"));
		d.setAmount(rs.getDouble("DiscountAmount"));
		d.setLastUpdated(rs.getInt("DiscountLastUpdated"));

		return d;
	}


	public static void createTable(Connection con)
	{
		PreparedStatement ps = null;

		String sql = "CREATE TABLE IF NOT EXISTS `Discount` (" 
				+ "`DiscountID` varchar(36) NOT NULL DEFAULT '0',"
				+ "`Name` varchar(20) NOT NULL," 
				+ "`Amount` double NOT NULL,"
				+ "`LastUpdated` bigint(14) NOT NULL," 
				+ "PRIMARY KEY (`DiscountID`)"
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
