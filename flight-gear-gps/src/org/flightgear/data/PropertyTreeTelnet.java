package org.flightgear.data;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.util.Log;


public class PropertyTreeTelnet implements PropertyTree {

	private Socket socket;
	private BufferedReader in;

	private PrintWriter out;
	private String host;
	private Integer port;

	public PropertyTreeTelnet(String host, Integer port)
			throws UnknownHostException, IOException {
		this.host = host;
		this.port = port;
		
		socket = new Socket(host, port);
		in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
		out = new PrintWriter(socket.getOutputStream(), true);
		setDataMode();
	}
	
	private void setDataMode() {
		out.println("data\r");
	}

	public synchronized void close() {
		out.println("quit\r");
		try {
			in.close();
			out.close();
			socket.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public synchronized String get(String path) throws IllegalArgumentException {
		out.println("get " + path + '\r');
		String propertyValue;
		propertyValue = tryToReadBufferLine();
		
		if (propertyValue == null || "".equals(propertyValue)) {
			throw new IllegalArgumentException("Property does not exist: "
					+ path);
		}
		return propertyValue;
	}

	public synchronized void set(String path, String value) {
		out.println("set " + path + ' ' + value + '\r');
	}

	public synchronized Map<String, String> dump(String path) {
		out.println("dump " + path + '\r');

		String parseDump = parseDump();
		return extractProperties(parseDump);
	}

	/**
	 * Read XML data from buffer
	 * 
	 * @param ignoreLF
	 * @return
	 * @throws IOException
	 */
	private synchronized String parseDump() {

		StringBuilder output = new StringBuilder();
		output.append(tryToReadBufferLine());

		if (!output.toString().startsWith("<?xml")) {
			throw new IllegalArgumentException("Property path not found: "
					+ output.toString());
		}

		String currentLine;

		do {
			currentLine = tryToReadBufferLine();
			output.append(currentLine.trim());
		} while (!currentLine.equals("</PropertyList>"));

		// discard last blank line
		tryToReadBufferLine();

		return output.toString();
	}

	/**
	 * Extracts properties from a xml dump string
	 * 
	 * @param xmlDump
	 *            xml returned by dump command
	 * @return Map<String,String> map containing properties names/values
	 */
	private Map<String, String> extractProperties(String xmlDump) {

		String regexPatternProperty = "<([\\w-]*) type=\"\\w*\">([^<]*)";

		Pattern pattern = Pattern.compile(regexPatternProperty);
		Matcher matcher = pattern.matcher(xmlDump);

		Map<String, String> properties = new HashMap<String, String>();

		while (matcher.find()) {
			String name = matcher.group(1);
			String value = matcher.group(2);
			properties.put(name, value);
		}

		return properties;
	}
	
	/**
	 * Get a property value as a boolean.
	 * 
	 * @param name
	 *            The property name to look up.
	 * @return The property value as a boolean.
	 * @see #get(String)
	 */
	public Boolean getBoolean(String name) {
		return get(name).equals("true");
	}

	/**
	 * Get a property value as an Integereger.
	 * 
	 * @param name
	 *            The property name to look up.
	 * @return The property value as an Integer.
	 * @see #get(String)
	 */
	public Integer getInt(String name) {
		return Integer.parseInt(get(name));
	}

	/**
	 * Get a property value as a Long.
	 * 
	 * @param name
	 *            The property name to look up.
	 * @return The property value as a Long.
	 * @see #get(String)
	 */
	public Long getLong(String name) {
		return Long.parseLong(get(name));
	}

	/**
	 * Get a property value as a Float.
	 * 
	 * @param name
	 *            The property name to look up.
	 * @return The property value as a Float.
	 * @see #get(String)
	 */
	public Float getFloat(String name) {
		return Float.parseFloat(get(name));
	}

	/**
	 * Get a property value as a double.
	 * 
	 * @param name
	 *            The property name to look up.
	 * @return The property value as a double.
	 * @see #get(String)
	 */
	public Double getDouble(String name) {
		return Double.parseDouble(get(name));
	}

	/**
	 * Set a property value from a boolean.
	 * 
	 * @param name
	 *            The property name to create or modify.
	 * @param value
	 *            The new property value as a boolean.
	 * @see #set(String,String)
	 */
	public void setBoolean(String name, Boolean value) {
		set(name, (value ? "true" : "false"));
	}

	/**
	 * Set a property value from an Integer.
	 * 
	 * @param name
	 *            The property name to create or modify.
	 * @param value
	 *            The new property value as an Integer.
	 * @see #set(String,String)
	 */
	public void setInt(String name, Integer value) {
		set(name, Integer.toString(value));
	}

	/**
	 * Set a property value from a Long.
	 * 
	 * @param name
	 *            The property name to create or modify.
	 * @param value
	 *            The new property value as a Long.
	 * @see #set(String,String)
	 */
	public void setLong(String name, Long value) {
		set(name, Long.toString(value));
	}

	/**
	 * Set a property value from a Float.
	 * 
	 * @param name
	 *            The property name to create or modify.
	 * @param value
	 *            The new property value as a Float.
	 * @see #set(String,String)
	 */
	public void setFloat(String name, Float value) {
		set(name, Float.toString(value));
	}

	/**
	 * Set a property value from a double.
	 * 
	 * @param name
	 *            The property name to create or modify.
	 * @param value
	 *            The new property value as a double.
	 * @see #set(String,String)
	 */
	public void setDouble(String name, Double value) {
		set(name, Double.toString(value));
	}

	private String tryToReadBufferLine() throws ConnectionException {
		String bufferLine = null;
		try{
			bufferLine = in.readLine();
		} catch (IOException e) {
			handleException(e);
		}
		return bufferLine;
	}

	private void handleException(Exception e) throws ConnectionException {
		Log.e(PropertyTreeTelnet.class.getName(), "Error during data access", e);
		throw new ConnectionException(e);
	}

	public boolean isConnected() {
		return socket.isConnected();
	}

	public void connect() throws IOException {
		socket.connect(new InetSocketAddress(host, port));
	}
}