package test.java;

import java.io.IOException;
import java.util.Map;

import org.flightgear.fggps.connection.FGFSConnection;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class FGClientPerformanceTest {

	private FGFSConnection fgfsConnection;
	private StopWatch stopWatch;

	@Before
	public void setUp() throws IOException {
		fgfsConnection = new FGFSConnection("localhost", 9000);
		stopWatch = new StopWatch();
	}
	
	@After
	public void tearDown() throws IOException {
		fgfsConnection.close();
	}

	@Test
	public void testGetSingleStringProperty() throws IOException {
		int samples = 10;

		System.out.println("GetSingleStringProperty with "
				+ samples + " samples");
		long totalTime = 0L;
		for (int i = 0; i < samples; i++) {

			stopWatch.start();
			String position = fgfsConnection.get("/position/altitude-ft");
			stopWatch.stop();

			System.out.println("[" + i + "] value: " + position + " time:"
					+ stopWatch.getElapsedTime() + "ms");
			totalTime += stopWatch.getElapsedTime();

			stopWatch.reset();
		}
		System.out.println("total: " + totalTime + "ms " + "average: "
				+ totalTime / samples + "ms");
		System.out.println("\n");
	}

	@Test
	public void testSetSingleStringProperty() throws IOException {
		int samples = 10;

		System.out.println("SetSingleStringProperty with "
				+ samples + " samples");
		long totalTime = 0L;
		for (int i = 0; i < samples; i++) {

			stopWatch.start();
			fgfsConnection.set("/position/altitude-ft","56.006854664");
			stopWatch.stop();

			System.out.println("[" + i + "] time:" + stopWatch.getElapsedTime()
					+ "ms");
			totalTime += stopWatch.getElapsedTime();

			stopWatch.reset();
		}
		System.out.println("total: " + totalTime + "ms " + "average: "
				+ totalTime / samples + "ms");
		System.out.println("\n");
	}
	
	@Test
	public void testDumpPropertyTree() throws IOException {
		int samples = 5;

		System.out.println("DumpPropertyTree with "
				+ samples + " samples");
		long totalTime = 0L;
		for (int i = 0; i < samples; i++) {

			stopWatch.start();
			Map<String, String> position = fgfsConnection.dump("/instrumentation/gps/");
			stopWatch.stop();

			System.out.println("[" + i + "] value: " + position + " time:"
					+ stopWatch.getElapsedTime() + "ms");
			
			totalTime += stopWatch.getElapsedTime();

			stopWatch.reset();
		}
		System.out.println("total: " + totalTime + "ms " + "average: "
				+ totalTime / samples + "ms");
		System.out.println("\n");
	}
}
