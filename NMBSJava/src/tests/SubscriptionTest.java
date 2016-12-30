
package tests;

import static org.junit.Assert.assertEquals;

import java.util.Date;
import java.util.UUID;

import org.junit.Before;
import org.junit.Test;

import model.Subscription;




public class SubscriptionTest {
	private Subscription sub;
	private Subscription sub2;
	private UUID subscriptionID = UUID.fromString("a379845f-bb4f-4a1f-9748-9d1bb7dfc431");
	private UUID railID = UUID.fromString("71acee22-8c4d-464e-85e1-058e7c9306ce");
	private UUID routeID= UUID.fromString("03e20178-ea89-4450-9680-3b20855f2947");
	private UUID discountID= UUID.fromString("8eafe702-311b-4ec2-be3a-f46c1a95b681");
	private String datumvan ="2017,03,02";
	private String datumtot ="2018,03,02";
	private long lastUpdated = 1482931466;
	@Before
	public void setUp() throws Exception {
		sub = new Subscription(railID, routeID, discountID, datumvan, datumtot);
		sub.setRailCardID(railID);
		sub.setRouteID(routeID);
		sub.setDiscountID(discountID);
		sub.setLastUpdated(lastUpdated);
	}
	
	@Test
	public void ConstructorTest1(){
		assertEquals(UUID.fromString("71acee22-8c4d-464e-85e1-058e7c9306ce"),sub.getRailCardID());
		assertEquals(UUID.fromString("03e20178-ea89-4450-9680-3b20855f2947"),sub.getRouteID());
		assertEquals(discountID,sub.getDiscountID());
		assertEquals(datumvan,sub.getValidFrom());
		assertEquals(datumtot,sub.getValidUntil());
	}
	
	@Test
	public void subIDtest(){
		sub.setSubscriptionID(subscriptionID);
		assertEquals(subscriptionID,sub.getSubscriptionID());
	}
	
	@Test
	public void RailIDTest(){
		sub.setRailCardID(railID);
		assertEquals(railID,sub.getRailCardID());
	}
	
	@Test
	public void RouteIDTest(){
		sub.setRouteID(routeID);
		assertEquals(routeID,sub.getRouteID());
	}
	
	@Test
	public void DiscountTest(){
		sub.setDiscountID(discountID);
		assertEquals(discountID,sub.getDiscountID());
	}
		
	@Test
	public void ValidFromTest(){
		sub.setValidFrom(datumvan);
		assertEquals(datumvan,sub.getValidFrom());
	}
	
	@Test
	public void ValidUntilTest(){
		sub.setValidUntil(datumtot);
		assertEquals(datumtot,sub.getValidUntil());
	}
	@Test
	public void LastUpdatedTest(){
		sub.setLastUpdated(1482918942);
		assertEquals(1482918942,sub.getLastUpdated());
	}
}

