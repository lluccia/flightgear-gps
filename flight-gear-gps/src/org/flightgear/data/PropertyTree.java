package org.flightgear.data;

import java.io.IOException;
import java.util.Map;

public interface PropertyTree {
	
	String get(String path);
	
	void set(String path, String value);

	Map<String, String> dump(String path);

	Boolean getBoolean(String name);

	Integer getInt(String name);

	Long getLong(String name);

	Float getFloat(String name);

	Double getDouble(String name);

	void setBoolean(String name, Boolean value);

	void setInt(String name, Integer value);

	void setLong(String name, Long value);

	void setFloat(String name, Float value);

	void setDouble(String name, Double value);

	boolean isConnected();

	void connect() throws IOException;

	void close();
}