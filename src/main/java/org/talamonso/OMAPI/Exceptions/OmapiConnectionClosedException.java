package org.talamonso.OMAPI.Exceptions;

public class OmapiConnectionClosedException extends OmapiConnectionException {
  public OmapiConnectionClosedException() {
    super("Remote host closed the connection. Possible authentication rejection.");
  }
}