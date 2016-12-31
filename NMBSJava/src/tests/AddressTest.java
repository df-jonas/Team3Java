package tests;

import static org.junit.Assert.assertEquals;

import java.util.UUID;

import org.json.JSONObject;
import org.junit.Before;
import org.junit.Test;

import model.Address;

public class AddressTest
{
	private Address adres;
	JSONObject temp;
	private long lastUpdated = 1482918942;
	@Before
	public void setUp() throws Exception
	{
		adres = new Address("Nijverheidskaai", 170, "Brussel", 1000, "50.8410136 - 4.322051299999998");
		adres.setLastUpdated(lastUpdated);
	}

	@Test
	public void constructorTest()
	{
		assertEquals("Nijverheidskaai", adres.getStreet());
		assertEquals(170, adres.getNumber());
		assertEquals("Brussel", adres.getCity());
		assertEquals(1000, adres.getZipCode());
		assertEquals("50.8410136 - 4.322051299999998", adres.getCoordinates());
		assertEquals(lastUpdated,adres.getLastUpdated());
	}

	@Test
	public void AddressIDTest()
	{
		adres.setAddressID(UUID.fromString("4fdf41b4-f8da-413d-b73f-484821c936b8"));
		assertEquals(UUID.fromString("4fdf41b4-f8da-413d-b73f-484821c936b8"), adres.getAddressID());
	}

	@Test
	public void StreetTest()
	{
		adres.setStreet("Nijverheidskaai");
		assertEquals("Nijverheidskaai", adres.getStreet());
	}

	@Test
	public void NumberTest()
	{
		adres.setNumber(170);
		assertEquals(170, adres.getNumber());
	}

	@Test
	public void CityTest()
	{
		adres.setCity("Brussel");
		assertEquals("Brussel", adres.getCity());
	}

	@Test
	public void ZipTest()
	{
		adres.setZipCode(1000);
		assertEquals(1000, adres.getZipCode());
	}

	@Test
	public void CoordinatesTest()
	{
		adres.setCoordinates("50.8410136 - 4.322051299999998");
		assertEquals("50.8410136 - 4.322051299999998", adres.getCoordinates());
	}

	@Test
	public void ToStringTest()
	{
		adres.setAddressID(UUID.fromString("4fdf41b4-f8da-413d-b73f-484821c936b8"));
		assertEquals(
				"Address [addressID=4fdf41b4-f8da-413d-b73f-484821c936b8, street=Nijverheidskaai, number=170, city=Brussel, zipCode=1000, coordinates=50.8410136 - 4.322051299999998]",
				adres.toString());
	}
	@Test
	public void LastUpdatedTest(){
		adres.setLastUpdated(1482918942);
		assertEquals(1482918942,adres.getLastUpdated());
	}

	/*@Test
	public void GetAPItest()
	{
		// adres.setAddressID(3);
		try {
			temp = new JSONObject(URLCon.readUrl("http://nmbs-team.tk/api/address/3", "GET"));
		}
		catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		assertEquals(3, temp.getInt("AddressID"));
	}*/
}
