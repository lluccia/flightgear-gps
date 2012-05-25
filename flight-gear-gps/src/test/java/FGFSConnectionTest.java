
import java.io.IOException;
import java.util.Map;

import junit.framework.Assert;

import org.flightgear.fggps.connection.FGFSConnection;
import org.junit.Test;

public class FGFSConnectionTest {

	@Test
	public void testExtractProperties() throws IOException {
		String xmlDump = "<?xml version=\"1.0\"?>"
				+"<PropertyList>"
					+"<longitude-deg type=\"double\">-122.3933073</longitude-deg>"
					+"<latitude-deg type=\"double\">37.62870148</latitude-deg>"
					+"<altitude-ft type=\"double\">6.480490213</altitude-ft>"
					+"<ground-elev-m type=\"double\">0.4841132964</ground-elev-m>"
					+"<ground-elev-ft type=\"double\">1.588298216</ground-elev-ft>"
				+"</PropertyList>";
		
		Map<String, String> properties = FGFSConnection.extractProperties(xmlDump);
		
		Assert.assertEquals(5, properties.size());
		
		Assert.assertEquals("-122.3933073", properties.get("longitude-deg"));
		Assert.assertEquals("37.62870148",  properties.get("latitude-deg"));
		Assert.assertEquals("6.480490213",  properties.get("altitude-ft"));
		Assert.assertEquals("0.4841132964", properties.get("ground-elev-m"));
		Assert.assertEquals("1.588298216",  properties.get("ground-elev-ft"));

	}
	
}
