package dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.UUID;

import model.Staff;

public class StaffDAO extends BaseDAO {

	public StaffDAO() {
		// TODO Auto-generated constructor stub
	}

	public int insert(Staff s) {
		PreparedStatement ps = null;

		String sql = "INSERT INTO Staff VALUES(?,?,?,?,?,?,?,?,?,?,?,?)";

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
			ps.setString(1, s.getApiToken());
			ps.setLong(12, s.getUnixTimestamp());

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

	public ArrayList<Reservation> selectAllSync() {
		ArrayList<Reservation> list = null;

		PreparedStatement ps = null;
		ResultSet rs = null;

		String sql = "SELECT * FROM Reservation";

		try {

			if (getConnection().isClosed()) {
				throw new IllegalStateException("error unexpected");
			}
			ps = getConnection().prepareStatement(sql);

			rs = ps.executeQuery();
			list = new ArrayList<Reservation>();

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

	public ArrayList<Reservation> selectAll() {
		ArrayList<Reservation> list = null;

		PreparedStatement ps = null;
		ResultSet rs = null;

		String sql = "SELECT r.RouteID, s.StationID as DepartStation, s.StationID as ArrivalStation, a.AddressID, a.Street,"
				+ " a.Number, a.City, a.ZipCode, a.Coordinates, a.LastUpdated as AddressLastUpdated,"
				+ " s.Name, s.CoX,s.CoY, s.LastUpdated as StationLastUpdated, "
				+ "r.LastUpdated as RouteLastUpdated, re.ReservationID, re.PassengerCount, re.TrainID, re.Price, re.LastUpdated as ReservationLastUpdated FROM Reservation re"
				+ "INNER JOIN Route r ON r.RouteID = re.RouteID"
				+ "INNER JOIN Station s ON s.StationID = r.DepartureStationID"
				+ "INNER JOIN Address a ON a.AddressID = s.AddressID;";

		try {

			if (getConnection().isClosed()) {
				throw new IllegalStateException("error unexpected");
			}
			ps = getConnection().prepareStatement(sql);

			rs = ps.executeQuery();
			list = new ArrayList<Reservation>();

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

	public Reservation selectOne(String reservationID) {
		PreparedStatement ps = null;
		ResultSet rs = null;

		String sql = "SELECT r.RouteID, s.StationID as DepartStation, s.StationID as ArrivalStation, a.AddressID, a.Street,"
				+ " a.Number, a.City, a.ZipCode, a.Coordinates, a.LastUpdated as AddressLastUpdated,"
				+ " s.Name, s.CoX,s.CoY, s.LastUpdated as StationLastUpdated, "
				+ "r.LastUpdated as RouteLastUpdated, re.ReservationID, re.PassengerCount, re.TrainID, re.Price, re.LastUpdated as ReservationLastUpdated FROM Reservation re"
				+ "INNER JOIN Route r ON r.RouteID = re.RouteID"
				+ "INNER JOIN Station s ON s.StationID = r.DepartureStationID"
				+ "INNER JOIN Address a ON a.AddressID = s.AddressID" + "WHERE re.ReservationID = ?;";

		try {

			if (getConnection().isClosed()) {
				throw new IllegalStateException("error unexpected");
			}
			ps = getConnection().prepareStatement(sql);

			ps.setString(1, reservationID);
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

	private Reservation resultToModel(ResultSet rs) throws SQLException {
		Reservation re = new Reservation();

		Route r = RouteDAO.resultToModel(rs);
		re.setReservationID(UUID.fromString(rs.getString("ReservationID")));
		re.setPassengerCount(rs.getInt("PassengerCount"));
		re.setTrainID(UUID.fromString(rs.getString("TrainID")));
		re.setPrice(rs.getDouble("Price"));
		re.setRoute(r);
		re.setLastUpdated(rs.getLong("ReservationLastUpdated"));

		return re;
	}

	public static void createTable() {
		PreparedStatement ps = null;
		ResultSet rs = null;

		String sql = "CREATE TABLE IF NOT EXISTS `Reservation` (  "
				+ "`ReservationID` varchar(36) NOT NULL DEFAULT '0', " + "`PassengerCount` int(11) NOT NULL,  "
				+ "`TrainID` varchar(36) NOT NULL DEFAULT '0',  "
				+ "`Price` double NOT NULL,  `RouteID` varchar(36) NOT NULL DEFAULT '0',  "
				+ "`LastUpdated` bigint(14) DEFAULT NULL,  PRIMARY KEY (`ReservationID`), "
				+ "KEY `routeID` (`RouteID`)) ENGINE=InnoDB DEFAULT CHARSET=latin1;";

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