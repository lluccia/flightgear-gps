package test;


import java.io.IOException;
import java.net.UnknownHostException;
import java.util.Map;

import org.flightgear.data.PropertyTreeTelnet;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class PropertyTreeTelnetIT {

	private PropertyTreeTelnet propertyTree;

	@Before
	public void setUp() throws UnknownHostException, IOException {
		this.propertyTree = new PropertyTreeTelnet("localhost", 9000);
	}

	@Test
	public void testGetExistentProperty() {
		String latitude = propertyTree.get("/position/latitude-deg");

		Assert.assertNotNull(latitude);

		System.out.println(latitude);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testGetInexistentProperty() {
		propertyTree.get("/position/notex");

		Assert.fail("Should have thrown exception");
	}

	@Test
	public void testGetListValues() {

		Map<String, String> positionProperties = propertyTree
				.dump("/position");

		Assert.assertNotNull(positionProperties);

		System.out.println(positionProperties.toString());
	}
}
