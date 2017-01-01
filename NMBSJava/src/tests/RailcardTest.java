package tests;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.UUID;

import org.junit.Before;
import org.junit.Test;

import model.RailCard;
import model.Subscription;

public class RailcardTest
{
	private RailCard railcard;
	private ArrayList<Subscription> subs;
	private long lastUpdated;
	@Before
	public void setUp() throws Exception
	{
		railcard = new RailCard();
		subs = new ArrayList<Subscription>();
		railcard.setLastUpdated(lastUpdated);
	}

	@Test
	public void RailcardIDTest()
	{
		railcard.setRailCardID(UUID.fromString("2cc46ba1-620b-40f4-b60f-97c909aecb3b"));
		assertEquals(UUID.fromString("2cc46ba1-620b-40f4-b60f-97c909aecb3b"), railcard.getRailCardID());
	}

	@Test
	public void SubscripionTest()
	{
		railcard.setSubscriptions(subs);
		assertEquals(subs, railcard.getSubscriptions());
	}

	@Test
	public void ArrayTest()
	{
		subs.add(new Subscription());
		assertEquals(1, subs.size());
		assertEquals(railcard, subs.get(0));
	}
	@Test
	public void LastUpdatedTest(){
		railcard.setLastUpdated(1482918942);
		assertEquals(1482918942,railcard.getLastUpdated());
	}

}
