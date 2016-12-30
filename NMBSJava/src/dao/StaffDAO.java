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
import model.Address;
import model.Staff;
import model.Station;

public class StaffDAO extends BaseDAO
{
	public final static String BASE_URL = "staff/";

	public StaffDAO()
	{}

	public int insertOrUpdate(Staff s)
	{
		Staff exists = this.selectOne(s.getStaffID().toString());

		if (exists == null)
			return this.insert(s);
		else
			return this.update(s);
	}
	
	public int insert(Staff s)
	{
		PreparedStatement ps = null;

		String sql = "INSERT INTO Staff VALUES(?,?,?,?,?,?,?,?,?,?,?,?);";

		try {

			if (getConnection().isClosed()) {
				throw new IllegalStateException("error unexpected");
			}
			ps = getConnection().prepareStatement(sql);

			ps.setString(1, s.getStaffID().toString());
			ps.setString(2, s.getAddressID().toString());
			ps.setString(3, s.getStationID().toString());
			ps.setString(4, s.getFirstName());
			ps.setString(5, s.getLastName());
			ps.setString(6, s.getUserName());
			ps.setString(7, s.getPassword());
			ps.setInt(8, s.getRights());
			ps.setString(9, s.getBirthDate().toString());
			ps.setString(10, s.getEmail());
			ps.setString(11, s.getApiToken());
			ps.setLong(12, s.getLastUpdated());

			if (!isSyncFunction)
			{
				params = new HashMap<String, String>();
				params.put("staffID", s.getStaffID().toString());
				params.put("addressID", s.getAddressID().toString());
				params.put("stationID", s.getStationID().toString());
				params.put("firstName", s.getFirstName());
				params.put("lastName", s.getLastName());
				params.put("userName", s.getUserName());
				params.put("password", s.getPassword());
				params.put("rights", Integer.toString(s.getRights()));
				params.put("birthDate", s.getBirthDate());
				params.put("email", s.getEmail());
				params.put("apiToken", s.getApiToken());
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
	
	public int update(Staff s)
	{
		PreparedStatement ps = null;

		String sql = "UPDATE `Staff` SET `AddressID`=?,`StationID`=?,`FirstName`=?,"
				+ "`LastName`=?,`UserName`=?,`Password`=?,`Rights`=?,`BirthDate`=?,`Email`=?,`Api_token`=?,`LastUpdated`=? "
				+ "WHERE `StaffID`=?;";

		try {

			if (getConnection().isClosed()) {
				throw new IllegalStateException("error unexpected");
			}
			ps = getConnection().prepareStatement(sql);

			ps.setString(1, s.getAddressID().toString());
			ps.setString(2, s.getStationID().toString());
			ps.setString(3, s.getFirstName());
			ps.setString(4, s.getLastName());
			ps.setString(5, s.getUserName());
			ps.setString(6, s.getPassword());
			ps.setInt(7, s.getRights());
			ps.setString(8, s.getBirthDate().toString());
			ps.setString(9, s.getEmail());
			ps.setString(10, s.getApiToken());
			ps.setLong(11, s.getLastUpdated());
			ps.setString(12, s.getStaffID().toString());

			if (!isSyncFunction)
			{
				params = new HashMap<String, String>();
				params.put("staffID", s.getStaffID().toString());
				params.put("addressID", s.getAddressID().toString());
				params.put("stationID", s.getStationID().toString());
				params.put("firstName", s.getFirstName());
				params.put("lastName", s.getLastName());
				params.put("userName", s.getUserName());
				params.put("password", s.getPassword());
				params.put("rights", Integer.toString(s.getRights()));
				params.put("birthDate", s.getBirthDate());
				params.put("email", s.getEmail());
				params.put("apiToken", s.getApiToken());
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
					syncMainDB(BASE_URL + "update/" + params.get("staffID"), RequestType.PUT, params);

			}
			catch (SQLException e) {
				System.out.println(e.getMessage());
				throw new RuntimeException("error.unexpected");
			}
		}

	}

	public ArrayList<Staff> selectAllSync()
	{
		ArrayList<Staff> list = null;

		PreparedStatement ps = null;
		ResultSet rs = null;

		String sql = "SELECT s.StaffID, s.AddressID, "
				+ "s.StationID, s.FirstName, s.LastName, s.UserName, s.Password, s.Rights, "
				+ "s.BirthDate, s.Email, s.Api_token, s.LastUpdated as StaffLastUpdated "
				+ "FROM Staff s;";

		try {

			if (getConnection().isClosed()) {
				throw new IllegalStateException("error unexpected");
			}
			ps = getConnection().prepareStatement(sql);

			rs = ps.executeQuery();
			list = new ArrayList<Staff>();

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

	public ArrayList<Staff> selectAll()
	{
		ArrayList<Staff> list = null;

		PreparedStatement ps = null;
		ResultSet rs = null;

		String sql = "SELECT Staff.StaffID, "
				+ "a.AddressID, a.Street, a.Number, a.City, a.ZipCode, a.Coordinates, a.LastUpdated as AddressLastUpdated, "
				+ "s.StationID, s.Name, s.CoX, s.CoY, s.LastUpdated as StationLastUpdated, "
				+ "Staff.FirstName, Staff.LastName, Staff.UserName, Staff.Password, Staff.Rights, "
				+ "Staff.BirthDate, Staff.Email, Staff.Api_token, Staff.LastUpdated as StaffLastUpdated "
				+ "FROM Staff "
				+ "INNER JOIN Address a on a.AddressID = Staff.AddressID "
				+ "INNER JOIN Station s on s.StationID = Staff.StationID;";

		try {

			if (getConnection().isClosed()) {
				throw new IllegalStateException("error unexpected");
			}
			ps = getConnection().prepareStatement(sql);

			rs = ps.executeQuery();
			list = new ArrayList<Staff>();

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

	public Staff selectOne(String staffID)
	{
		PreparedStatement ps = null;
		ResultSet rs = null;

		String sql = "SELECT Staff.StaffID, "
				+ "a.AddressID, a.Street, a.Number, a.City, a.ZipCode, a.Coordinates, a.LastUpdated as AddressLastUpdated, "
				+ "s.StationID, s.Name, s.CoX, s.CoY, s.LastUpdated as StationLastUpdated, "
				+ "Staff.FirstName, Staff.LastName, Staff.UserName, Staff.Password, Staff.Rights, "
				+ "Staff.BirthDate, Staff.Email, Staff.Api_token, Staff.LastUpdated as StaffLastUpdated "
				+ "FROM Staff "
				+ "INNER JOIN Address a on a.AddressID = Staff.AddressID "
				+ "INNER JOIN Station s on s.StationID = Staff.StationID "
				+ "WHERE StaffID=?;";

		try {

			if (getConnection().isClosed()) {
				throw new IllegalStateException("error unexpected");
			}
			ps = getConnection().prepareStatement(sql);

			ps.setString(1, staffID);
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
	
	public Staff selectOneOnUsername(String username)
	{
		PreparedStatement ps = null;
		ResultSet rs = null;

		String sql = "SELECT Staff.StaffID, "
				+ "a.AddressID, a.Street, a.Number, a.City, a.ZipCode, a.Coordinates, a.LastUpdated as AddressLastUpdated, "
				+ "s.StationID, s.Name, s.CoX, s.CoY, s.LastUpdated as StationLastUpdated, "
				+ "Staff.FirstName, Staff.LastName, Staff.UserName, Staff.Password, Staff.Rights, "
				+ "Staff.BirthDate, Staff.Email, Staff.Api_token, Staff.LastUpdated as StaffLastUpdated "
				+ "FROM Staff "
				+ "INNER JOIN Address a on a.AddressID = Staff.AddressID "
				+ "INNER JOIN Station s on s.StationID = Staff.StationID "
				+ "WHERE UserName = ?;";

		try {

			if (getConnection().isClosed()) {
				throw new IllegalStateException("error unexpected");
			}
			ps = getConnection().prepareStatement(sql);

			ps.setString(1, username);
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

	public TreeMap<String, String> updateStatus()
	{
		TreeMap<String, String> map = null;

		PreparedStatement ps = null;
		ResultSet rs = null;

		String sql = "SELECT COUNT(DISTINCT StaffID) as Count, MAX(LastUpdated) as LastUpdated FROM Staff";

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
	
	private Staff resultToModel(ResultSet rs) throws SQLException
	{
		Staff staff = new Staff();
		Address a = AddressDAO.resultToModel(rs);
		Station s = StationDAO.resultToModel(rs);
		
		staff.setStaffID(UUID.fromString(rs.getString("StaffID")));
		staff.setAddress(a);
		staff.setAddressID(a.getAddressID());
		staff.setStation(s);
		staff.setStaffID(s.getStationID());
		staff.setFirstName(rs.getString("FirstName"));
		staff.setLastName(rs.getString("LastName"));
		staff.setUserName(rs.getString("UserName"));
		staff.setPassword(rs.getString("Password"));
		staff.setRights(rs.getInt("Rights"));
		staff.setEmail(rs.getString("Email"));
		staff.setBirthDate(rs.getString("BirthDate"));
		staff.setApiToken(rs.getString("Api_token"));
		staff.setLastUpdated(rs.getLong("StaffLastUpdated"));


		return staff;
	}

	public static void createTable(Connection con)
	{
		PreparedStatement ps = null;

		String sql = "CREATE TABLE IF NOT EXISTS `Staff` ("
				+ "`StaffID` varchar(36) NOT NULL DEFAULT '0',"
				+ "`AddressID` varchar(36) NOT NULL DEFAULT '0',"
				+ "`StationID` varchar(36) NOT NULL DEFAULT '0',"
				+ "`FirstName` varchar(20) NOT NULL,"
				+ "`LastName` varchar(20) NOT NULL,"
				+ "`UserName` varchar(512) NOT NULL UNIQUE,"
				+ "`Password` varchar(512) NOT NULL,"
				+ "`Rights` int(1) NOT NULL,"
				+ "`BirthDate` varchar(40) NOT NULL,"
				+ "`Email` varchar(50) NOT NULL,"
				+ "`Api_token` varchar(60) DEFAULT NULL UNIQUE,"
				+ "`LastUpdated` bigint(14) NOT NULL,"
				+ "PRIMARY KEY (`StaffID`),"
				+ "FOREIGN KEY (`AddressID`) REFERENCES `Address`(`AddressID`),"
				+ "FOREIGN KEY (`StationID`) REFERENCES `Station`(`StationID`)"
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