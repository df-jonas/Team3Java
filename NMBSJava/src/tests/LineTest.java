package tests;

import static org.junit.Assert.assertEquals;

import java.util.UUID;

import org.junit.Before;
import org.junit.Test;

import model.Line;

public class LineTest {
	private UUID lineID = UUID.fromString("066a0147-674e-478f-b1c8-7a21e266c170");
	private UUID routeID = UUID.fromString("79cbbd0d-7ea2-4366-a5a8-2ad2ff2f95a8");
	private String trainType = "p";
	private long lastUpdated = 1482918942;
	private Line line;
	
	@Before
	public void setUp() throws Exception
	{
		line = new Line(routeID, trainType);
		line.setLineID(UUID.fromString("066a0147-674e-478f-b1c8-7a21e266c170"));
		line.setLastUpdated(lastUpdated);
	}
	
	@Test
	public void ConstructorTest(){
		assertEquals(lineID,line.getLineID());
		assertEquals(routeID,line.getRouteID());
		assertEquals(trainType,line.getTrainType());
		assertEquals(lastUpdated,line.getLastUpdated());
	}
	
	@Test
	public void LineIDTest(){
		line.setLineID(UUID.fromString("066a0147-674e-478f-b1c8-7a21e266c170"));
		assertEquals(UUID.fromString("066a0147-674e-478f-b1c8-7a21e266c170"),line.getLineID());
	}
	
	@Test
	public void RouteIDTest(){
		line.setRouteID(UUID.fromString("f654ba8d-0e11-4d47-8c5a-8aebd5cd8dcf"));
		assertEquals(UUID.fromString("f654ba8d-0e11-4d47-8c5a-8aebd5cd8dcf"),line.getRouteID());
	}
	
	@Test
	public void TrainTypeTest(){
		line.setTrainType("IC");
		assertEquals("IC",line.getTrainType());
	}
	
	@Test
	public void LastUpdatedTest(){
		line.setLastUpdated(1482918942);
		assertEquals(1482918942,line.getLastUpdated());
	}
}
