package org.talamonso.OMAPI.Examples;

import org.talamonso.OMAPI.Connection;
import org.talamonso.OMAPI.Exceptions.OmapiException;

/**
 * This object holds the connection informations used by the examples.
 * 
 * @author Talamonso
 */
public class Default {

  /**
   * This is just a Default methode...
   * 
   * @return a connection object
   */
  public static Connection getC() {
    Connection c = null;
    try {
      c = new Connection("10.37.129.3", 7911);
      c.setAuth("omapi_key", "fm2zoJB6CRHCdb//ilMYxiIkfcLFJ0nLaX6EAynK85Wl1diycVMnJ3uie7bMWwSdf4u2y31t+XvjPnVfqNJekw==");
    } catch (OmapiException e) {
      System.err.println(e.getMessage());
    }
    return c;
  }
}
