package org.talamonso.OMAPI.Examples;

import org.talamonso.OMAPI.Connection;
import org.talamonso.OMAPI.Exceptions.OmapiException;
import org.talamonso.OMAPI.MessageOperation;
import org.talamonso.OMAPI.Objects.Lease;

/**
 * Open a lease object
 * 
 * @author Talamonso
 */
public class Lease_Open {

  /**
   * Opens a lease object and print out some informations.
   * 
   * @param args
   */
  public static void main(final String[] args) {
    Connection c = Default.getC();
    try {
      Lease l = new Lease(c);
      //l.setIPAddress("192.168.1.23");
      l.setHardwareAddress("00:1c:42:db:33:a4");
      Lease remote = l.send(MessageOperation.OPEN);
      System.out.println(remote);
    } catch (OmapiException e) {
      System.err.println(e.getMessage());
    }
    c.close();
  }
}
