package org.talamonso.OMAPI.Examples;

import org.talamonso.OMAPI.Connection;
import org.talamonso.OMAPI.Exceptions.OmapiException;
import org.talamonso.OMAPI.MessageType;
import org.talamonso.OMAPI.Objects.FailoverPair;

/**
 * Example of an FailoverState information.
 * 
 * @author Talamonso
 */
public class FailoverState_Open {

  /**
   * Print out some handy informations about the state and the peer of the failover system.
   * 
   * @param args
   */
  public static void main(final String[] args) {
    Connection c = Default.getC();
    try {
      FailoverPair fos = new FailoverPair(c);
      fos.setName("dhcpd-sv");
      FailoverPair remote = fos.send(MessageType.OPEN);
      System.out.println(remote.toString());
    } catch (OmapiException e) {
      System.err.println(e.getMessage());
    }
    c.close();
  }
}
