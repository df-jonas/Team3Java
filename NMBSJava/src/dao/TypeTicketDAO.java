package dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.UUID;

import model.TypeTicket;

public class TypeTicketDAO extends BaseDAO {

	public TypeTicketDAO() {

	}

	public int insert(TypeTicket t) {
		PreparedStatement ps = null;

		String sql = "INSERT INTO TypeTicket VALUES(?,?,?,?,?)";

		try {

			if (getConnection().isClosed()) {
				throw new IllegalStateException("error unexpected");
			}
			ps = getConnection().prepareStatement(sql);

			ps.setString(1, t.getTypeTicketID().toString());
			ps.setString(2, t.getName());
			ps.setDouble(3, t.getPrice());
			ps.setInt(4, t.getComfortClass());
			ps.setLong(5, t.getUnixTimestamp());

			// api call

			return ps.executeUpdate();

		} catch (SQLException e) {
			System.out.println(e.getMessage());
			throw new RuntimeException(e.getMessage());
		} finally {
			try {
				if (ps != null)
					ps.close();

			} catch (SQLException e) {
				System.out.println(e.getMessage());
				throw new RuntimeException("error.unexpected");
			}
		}

	}

	public ArrayList<TypeTicket> selectAllSync() {
		ArrayList<TypeTicket> list = null;

		PreparedStatement ps = null;
		ResultSet rs = null;

		String sql = "SELECT * FROM TypeTicket";

		try {

			if (getConnection().isClosed()) {
				throw new IllegalStateException("error unexpected");
			}
			ps = getConnection().prepareStatement(sql);

			rs = ps.executeQuery();
			list = new ArrayList<TypeTicket>();

			while (rs.next()) {
				list.add(resultToModel(rs));
			}

			return list;
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

	public ArrayList<TypeTicket> selectAll() {
		ArrayList<TypeTicket> list = null;

		PreparedStatement ps = null;
		ResultSet rs = null;

		String sql = "SELECT t.TypeTicketID, t.Name, t.Price, t.ComfortClass, t.LastUpdated as TypeTikcetLastUpdated"
				+ "FROM TypeTicket t;";

		try {

			if (getConnection().isClosed()) {
				throw new IllegalStateException("error unexpected");
			}
			ps = getConnection().prepareStatement(sql);

			rs = ps.executeQuery();
			list = new ArrayList<TypeTicket>();

			while (rs.next()) {
				list.add(resultToModel(rs));
			}

			return list;
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

	public TypeTicket selectOne(String typePassID) {
		PreparedStatement ps = null;
		ResultSet rs = null;

		String sql = "SELECT t.TypeTicketID, t.Name, t.Price, t.ComfortClass, "
				+ "t.LastUpdated as TypeTikcetLastUpdated FROM TypeTicket t" + "WHERE TypeTicketID = ?;";

		try {

			if (getConnection().isClosed()) {
				throw new IllegalStateException("error unexpected");
			}
			ps = getConnection().prepareStatement(sql);

			ps.setString(1, typePassID);
			rs = ps.executeQuery();
			if (rs.next())
				return resultToModel(rs);
			else
				return null;
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

	private TypeTicket resultToModel(ResultSet rs) throws SQLException {
		TypeTicket t = new TypeTicket();

		t.setTypeTicketID(UUID.fromString(rs.getString("TypeTicketID")));
		t.setName(rs.getString("Name"));
		t.setPrice(rs.getDouble("Price"));
		t.setComfortClass(rs.getInt("ComfortClass"));
		t.setLastUpdated(rs.getLong("TypePassLastUpdated"));

		return t;
	}

	public static void createTable() {
		PreparedStatement ps = null;
		ResultSet rs = null;

		String sql = "CREATE TABLE IF NOT EXISTS `TypeTicket` (  "
				+ "`TypeTicketID` varchar(36) NOT NULL DEFAULT '0',  "
				+ "`Name` varchar(50) CHARACTER SET utf8 COLLATE utf8_unicode_ci NOT NULL,  "
				+ "`Price` double NOT NULL,  `ComfortClass` int(11) NOT NULL,  "
				+ "`LastUpdated` bigint(14) DEFAULT NULL,  PRIMARY KEY (`TypeTicketID`)) "
				+ "ENGINE=InnoDB DEFAULT CHARSET=latin1;";

		try {

			if (getConnection().isClosed()) {
				throw new IllegalStateException("error unexpected");
			}
			ps = getConnection().prepareStatement(sql);
			rs = ps.executeQuery();
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
}