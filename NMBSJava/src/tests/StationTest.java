
package tests;

import static org.junit.Assert.assertEquals;

import java.util.UUID;

import org.junit.Before;
import org.junit.Test;

import model.Station;

public class StationTest {

	private Station station;
	private UUID stationID = UUID.fromString("3a795185-c01c-4a5d-9a32-606faabceb0f");
	private long lastUpdated = 1482929273;

	@Before
	public void setUp() throws Exception {
		station = new Station( "Brussel-Zuid","4.024407","50.948377");
		station.setLastUpdated(lastUpdated);
		station.setStationID(stationID);
	}

	@Test
	public void ConstructorTest() {
		assertEquals("Brussel-Zuid", station.getStationName());
		assertEquals("4.024407",station.getCoX());
		assertEquals("50.948377",station.getCoY());
	}


	@Test
	public void StationNameTest() {
		station.setStationName("Brussel-Centraal");
		assertEquals("Brussel-Centraal", station.getStationName());
	}

	@Test
	public void StationIDTest() {
		station.setStationID(stationID);
		assertEquals(stationID, station.getStationID());
	}
	
	@Test
	public void CoxTest(){
		station.setCoX("4.024407");
		assertEquals("4.024407",station.getCoX());
	}
	
	@Test
	public void CoyTest(){
		station.setCoY("50.948377");
		assertEquals("50.948377",station.getCoY());
	}
	
	@Test
	public void LastUpdatedTest(){
		station.setLastUpdated(1482918942);
		assertEquals(1482918942,station.getLastUpdated());
	}
}
