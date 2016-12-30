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
import model.Customer;
import model.RailCard;

public class CustomerDAO extends BaseDAO
{
	public final static String BASE_URL = "customer/";

	public CustomerDAO()
	{}

	public int insertOrUpdate(Customer c)
	{
		Customer exists = this.selectOne(c.getCustomerID().toString());

		if (exists == null)
			return this.insert(c);
		else
			return this.update(c);
	}
	
	public int insert(Customer c)
	{
		PreparedStatement ps = null;

		String sql = "INSERT INTO Customer VALUES(?,?,?,?,?,?,?,?)";

		try {

			if (getConnection().isClosed()) {
				throw new IllegalStateException("error unexpected");
			}
			ps = getConnection().prepareStatement(sql);

			ps.setString(1, c.getCustomerID().toString());
			ps.setString(2, c.getRailCard().getRailCardID().toString());
			ps.setString(3, c.getAddress().getAddressID().toString());
			ps.setString(4, c.getFirstName());
			ps.setString(5, c.getLastName());
			ps.setString(6, c.getBirthDate());
			ps.setString(7, c.getEmail());
			ps.setLong(8, c.getLastUpdated());

			if (!isSyncFunction)
			{
				params = new HashMap<String, String>();
				params.put("customerID", c.getCustomerID().toString());
				params.put("railCardID", c.getRailCard().getRailCardID().toString());
				params.put("addressID", c.getAddress().getAddressID().toString());
				params.put("firstName", c.getFirstName());
				params.put("lastName", c.getLastName());
				params.put("email", c.getEmail());
				params.put("birthDate", c.getBirthDate());
				params.put("lastUpdated", Long.toString(c.getLastUpdated()));
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
	

	public int update(Customer c)
	{
		PreparedStatement ps = null;

		String sql = "UPDATE `Customer` SET `RailCardID`=?,`AddressID`=?,"
				+ "`FirstName`=?,`LastName`=?,`BirthDate`=?,`Email`=?,"
				+ "`LastUpdated`=? WHERE CustomerID=?;";

		try {

			if (getConnection().isClosed()) {
				throw new IllegalStateException("error unexpected");
			}
			ps = getConnection().prepareStatement(sql);
			
			ps.setString(1, c.getRailCard().getRailCardID().toString());
			ps.setString(2, c.getAddress().getAddressID().toString());
			ps.setString(3, c.getFirstName());
			ps.setString(4, c.getLastName());
			ps.setString(5, c.getBirthDate().toString());
			ps.setString(6, c.getEmail());
			ps.setLong(7, c.getLastUpdated());
			ps.setString(8, c.getCustomerID().toString());

			if (!isSyncFunction)
			{
				params = new HashMap<String, String>();
				params.put("customerID", c.getCustomerID().toString());
				params.put("railCardID", c.getRailCard().getRailCardID().toString());
				params.put("addressID", c.getAddress().getAddressID().toString());
				params.put("firstName", c.getFirstName());
				params.put("lastName", c.getLastName());
				params.put("email", c.getEmail());
				params.put("birthDate", c.getBirthDate());
				params.put("lastUpdated", Long.toString(c.getLastUpdated()));
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
					syncMainDB(BASE_URL + "update/" + params.get("customerID"), RequestType.PUT, params);

			}
			catch (SQLException e) {
				System.out.println(e.getMessage());
				throw new RuntimeException("error.unexpected");
			}
		}

	}
	public ArrayList<Customer> selectAllSync()
	{
		ArrayList<Customer> list = null;

		PreparedStatement ps = null;
		ResultSet rs = null;

		String sql = "SELECT c.CustomerID, c.RailCardID, c.AddressID, c.FirstName, c.LastName, c.BirthDate, c.Email, c.LastUpdated as CustomerLastUpdated"
				+ " FROM Customer c";

		try {

			if (getConnection().isClosed()) {
				throw new IllegalStateException("error unexpected");
			}
			ps = getConnection().prepareStatement(sql);

			rs = ps.executeQuery();
			list = new ArrayList<Customer>();

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

	public ArrayList<Customer> selectAll()
	{
		ArrayList<Customer> list = null;

		PreparedStatement ps = null;
		ResultSet rs = null;

		String sql = "SELECT c.CustomerID, r.CardID, r.LastUpdated as CardLastUpdated, a.AddressID, a.Street, "
				+ " a.Number, a.City, a.ZipCode, a.Coordinates, a.LastUpdated as AddressLastUpdated, "
				+ "c.FirstName, c.LastName, c.BirthDate, c.Email, c.LastUpdated as CustomerLastUpdated "
				+ " FROM Customer c " 
				+ " INNER JOIN Address a ON a.AddressID = c.AddressID "
				+ " INNER JOIN RailCard r ON r.CardID = c.RailCardID;";

		try {

			if (getConnection().isClosed()) {
				throw new IllegalStateException("error unexpected");
			}
			ps = getConnection().prepareStatement(sql);

			rs = ps.executeQuery();
			list = new ArrayList<Customer>();

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

		String sql = "SELECT COUNT(DISTINCT CustomerID) as Count, MAX(LastUpdated) as LastUpdated FROM Customer";

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

	public Customer selectOne(String customerID)
	{
		PreparedStatement ps = null;
		ResultSet rs = null;

		String sql = "SELECT c.CustomerID, r.CardID, r.LastUpdated as CardLastUpdated, a.AddressID, a.Street, "
				+ " a.Number, a.City, a.ZipCode, a.Coordinates, a.LastUpdated as AddressLastUpdated, "
				+ "c.FirstName, c.LastName, c.BirthDate, c.Email, c.LastUpdated as CustomerLastUpdated "
				+ " FROM Customer c " 
				+ " INNER JOIN Address a ON a.AddressID = c.AddressID "
				+ " INNER JOIN RailCard r ON r.CardID = c.RailCardID " 
				+ " WHERE c.CustomerID = ?;";

		try {

			if (getConnection().isClosed()) {
				throw new IllegalStateException("error unexpected");
			}
			ps = getConnection().prepareStatement(sql);

			ps.setString(1, customerID);
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
	

	public Customer selectOneOnSubscriptionID(String subscriptionID)

	{
		PreparedStatement ps = null;
		ResultSet rs = null;

		String sql = "SELECT  c.CustomerID, r.CardID, r.LastUpdated as CardLastUpdated, a.AddressID, a.Street,  a.Number, a.City, a.ZipCode, a.Coordinates, a.LastUpdated as AddressLastUpdated, c.FirstName, "
				+ "c.LastName, c.BirthDate, c.Email, c.LastUpdated as CustomerLastUpdated "
				+ "FROM Subscription s "
				+ "INNER JOIN RailCard r on r.CardID = s.RailCardID "
				+ "INNER JOIN Customer c on c.RailCardID = s.RailCardID "
				+ "INNER JOIN Address a ON a.AddressID = c.AddressID "
				+ "WHERE s.SubscriptionID = ?";
        
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
	
	public Customer selectOneOnRailCardID(String railcardID)
	{
		PreparedStatement ps = null;
		ResultSet rs = null;

		String sql = "SELECT c.CustomerID, r.CardID, r.LastUpdated as CardLastUpdated, a.AddressID, a.Street,"
				+ " a.Number, a.City, a.ZipCode, a.Coordinates, a.LastUpdated as AddressLastUpdated, "
				+ "c.FirstName, c.LastName, c.BirthDate, c.Email, c.LastUpdated as CustomerLastUpdated"
				+ " FROM Customer c" + " INNER JOIN Address a ON a.AddressID = c.AddressID"
				+ " INNER JOIN RailCard r ON r.CardID = c.RailCardID" + " WHERE c.RailCardID = ?;";

		try {

			if (getConnection().isClosed()) {
				throw new IllegalStateException("error unexpected");
			}
			ps = getConnection().prepareStatement(sql);

			ps.setString(1, railcardID);
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
	
	public Customer selectOneOnLastName(String lastname)
	{
		PreparedStatement ps = null;
		ResultSet rs = null;

		String sql = "SELECT c.CustomerID, r.CardID, r.LastUpdated as CardLastUpdated, a.AddressID, a.Street,"
				+ " a.Number, a.City, a.ZipCode, a.Coordinates, a.LastUpdated as AddressLastUpdated, "
				+ "c.FirstName, c.LastName, c.BirthDate, c.Email, c.LastUpdated as CustomerLastUpdated"
				+ " FROM Customer c" + " INNER JOIN Address a ON a.AddressID = c.AddressID"
				+ " INNER JOIN RailCard r ON r.CardID = c.RailCardID" + " WHERE c.LastName = ?;";

		try {

			if (getConnection().isClosed()) {
				throw new IllegalStateException("error unexpected");
			}
			ps = getConnection().prepareStatement(sql);

			ps.setString(1, lastname);
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
	
	public Customer selectOneOnFirstName(String firstname)
	{
		PreparedStatement ps = null;
		ResultSet rs = null;

		String sql = "SELECT c.CustomerID, r.CardID, r.LastUpdated as CardLastUpdated, a.AddressID, a.Street,"
				+ " a.Number, a.City, a.ZipCode, a.Coordinates, a.LastUpdated as AddressLastUpdated, "
				+ "c.FirstName, c.LastName, c.BirthDate, c.Email, c.LastUpdated as CustomerLastUpdated"
				+ " FROM Customer c" + " INNER JOIN Address a ON a.AddressID = c.AddressID"
				+ " INNER JOIN RailCard r ON r.CardID = c.RailCardID" + " WHERE c.FirstName = ?;";

		try {

			if (getConnection().isClosed()) {
				throw new IllegalStateException("error unexpected");
			}
			ps = getConnection().prepareStatement(sql);

			ps.setString(1, firstname);
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

	static Customer resultToModel(ResultSet rs) throws SQLException
	{
		Address a = new Address();
		Customer c = new Customer();
		RailCard r = new RailCard();

		r.setRailCardID(UUID.fromString(rs.getString("CardID")));
		r.setLastUpdated(rs.getLong("CardLastUpdated"));

		a.setAddressID(UUID.fromString(rs.getString("AddressID")));
		a.setStreet(rs.getString("Street"));
		a.setNumber(rs.getInt("Number"));
		a.setCity(rs.getString("City"));
		a.setZipCode(rs.getInt("ZipCode"));
		a.setCoordinates(rs.getString("Coordinates"));
		a.setLastUpdated(rs.getLong("AddressLastUpdated"));

		c.setCustomerID(UUID.fromString(rs.getString("CustomerID")));
		c.setAddress(a);
		c.setRailCard(r);
		c.setFirstName(rs.getString("FirstName"));
		c.setLastName(rs.getString("LastName"));
		c.setBirthDate(rs.getString("BirthDate"));
		c.setEmail(rs.getString("Email"));
		c.setLastUpdated(rs.getLong("CustomerLastUpdated"));
		return c;
	}
	
	private Customer syncResultToModel(ResultSet rs) throws SQLException
	{
		Customer c = new Customer();

		c.setCustomerID(UUID.fromString(rs.getString("CustomerID")));
		c.setAddressID(UUID.fromString(rs.getString("AddressID")));
		c.setRailCardID(UUID.fromString(rs.getString("RailCardID")));
		c.setFirstName(rs.getString("FirstName"));
		c.setLastName(rs.getString("LastName"));
		c.setBirthDate(rs.getString("BirthDate"));
		c.setEmail(rs.getString("Email"));
		c.setLastUpdated(rs.getLong("CustomerLastUpdated"));
		return c;
	}

	public static void createTable(Connection con)
	{
		PreparedStatement ps = null;

		String sql = "CREATE TABLE IF NOT EXISTS `Customer` (" 
				+ "`CustomerID` varchar(36) NOT NULL,"
				+ "`RailCardID` varchar(36) NOT NULL," 
				+ "`AddressID` varchar(36) NOT NULL,"
				+ "`FirstName` varchar(20) NOT NULL," 
				+ "`LastName` varchar(20) NOT NULL,"
				+ "`BirthDate` varchar(20) NOT NULL," 
				+ "`Email` varchar(50) NOT NULL,"
				+ "`LastUpdated` bigint(14) NOT NULL," 
				+ "PRIMARY KEY (`CustomerID`), "
				+ "FOREIGN KEY (`AddressID`) REFERENCES `Address`(`AddressID`), "
				+ "FOREIGN KEY (`RailCardID`) REFERENCES `RailCard`(`RailCardID`)"
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