package org.flightgear.data;

public class ConnectionException extends RuntimeException {

	private static final long serialVersionUID = -1029429435380050535L;

	public ConnectionException(Exception e) {
		 super(e);
	}

}
