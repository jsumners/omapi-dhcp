package org.talamonso.OMAPI;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.util.Base64;

import com.widget.util.Hex;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.talamonso.OMAPI.Exceptions.OmapiConnectionException;
import org.talamonso.OMAPI.Exceptions.OmapiInitException;
import org.talamonso.OMAPI.Objects.Authenticator;

/**
 * A class which implements the Interface for object manipulation (OMAPI). <p> This interface is important if you want to change the ISC DHCP server while its running. </p>
 * @author  Talamonso
 * @version  1.0
 */
public class Connection {
  private static final Logger log = LoggerFactory.getLogger(Connection.class);

  private final int headerlength = 24;

  private InetAddress host;

  /**
   * @uml.property  name="key"
   */
  private byte[] key = null;

  private String keyName = "";

  private int port = 7911;

  private Socket socket;

  private final int version = 100;

  /**
   * InputStream of the connection
   */
  protected InputStream in;

  /**
   * Is set true, if the connection is successfully initialised
   */
  protected boolean init = false;

  /**
   * OutputStream of the connection.
   */
  protected OutputStream out;

  /**
   * True if an valid key for signed communication is set.
   */
  protected boolean useAuth = false;

  /**
   * Set up a connection to a DHCP server on the default port (7911);
   * 
   * @param server The ISC DHCP Server as IP addresses (dotted notation) or domain name
   * @throws OmapiConnectionException if connection fails.
   */
  public Connection(String server) throws OmapiConnectionException {
    new Connection(server, this.port);
  }

  /**
   * Connects to a DHCP server.
   * 
   * @param server For example host.tld or 192.168.1.254
   * @param portnr For example 7911
   * @throws OmapiConnectionException if connection fails.
   */
  public Connection(String server, int portnr) throws OmapiConnectionException {
    log.debug("Trying to Connect to: `{}:{}`", server, portnr);
    try {
      this.host = InetAddress.getByName(server);
      this.port = portnr;
      this.socket = new Socket(this.host, portnr);
      this.in = this.socket.getInputStream();
      this.out = this.socket.getOutputStream();
      byte[] b = new byte[8];
      while ((this.in.read(b)) != -1) {
        break;
      }
      if (!Hex.toHex(b).equals(Hex.toHex(this.initValue()))) {
        throw new OmapiConnectionException("unknown server version: " + Hex.toHex(b));
      }
    } catch (IOException e) {
      throw new OmapiConnectionException(e.getMessage());
    }
    log.debug(" - Connection established");
  }

  /**
   * Shut down this connection.
   */
  public void close() {
    log.debug("Trying to close the connection");
    try {
      this.in.close();
      this.out.close();
      this.socket.close();
    } catch (IOException e) {
      // an error?
      // this could only happen,
      // if the connection is already closed!
    }
    log.debug(" - Connection successfully closed");
  }

  /**
   * Sets the parameters used for the authentication and commits it to the server.
   * 
   * @param name Name of the secred key as in dhcpd.conf
   * @param k Secret key in Base64 as in dhcpd.conf
   * @throws OmapiInitException
   */
  public void setAuth(String name, String k) throws OmapiInitException {
    log.debug("Trying to set the authenticator");
    try {
      if (name.length() > 16) {
        throw new OmapiInitException("Name of the Key exceeds 16 byte");
      }
      this.keyName = name;
      this.key = Base64.getDecoder().decode(k);
      Authenticator a = new Authenticator(this, name);
      a.send(MessageType.OPEN);
      this.useAuth = true;
    } catch (Exception e) {
      this.key = null;
      this.useAuth = false;
      this.keyName = "";
      throw new OmapiInitException("Key is no valid Base64\n" + e.getMessage());
    }
    log.debug(" - Authenticator successfully set");
  }

  /**
   * Details of the connection for the console.
   * 
   * @return Connection details
   */
  public String toString() {
    StringBuilder sb = new StringBuilder();

    sb.append("OMAPI Connection Informations:\n");
    sb.append("===============================\n");
    sb.append("Server address:          " + this.host.getHostAddress() + "\n");
    sb.append("Server port:             " + this.port + "\n");
    sb.append("OMAPI Version (Client):  " + this.version + "\n");
    sb.append("Header length:           " + this.headerlength + "\n");
    sb.append("Connection initialised?: " + this.init + "\n");
    sb.append("Use Authentification:    " + this.useAuth + "\n");
    if (this.useAuth) {
      sb.append(" Key Name:               " + this.keyName + "\n");
      sb.append(" Secret Key:            " + Hex.toHexF(this.key));
    }

    return sb.toString();
  }

  /**
   * Returns the length of the header.
   * 
   * @return headerlenght as an int value
   */
  protected int getHeaderLength() {
    return this.headerlength;
  }

  /**
   * Returns the secret key used for signing
   * @return  secret key used for signing
   * @uml.property  name="key"
   */
  protected byte[] getKey() {
    return this.key;
  }

  /**
   * Returns the required init value as a bytearray. The init value consists of the version (100) and the header
   * length (24) in byte.
   * 
   * @return init value as a bytearray
   */
  protected byte[] initValue() {
    ByteBuffer bb = ByteBuffer.allocate(8);
    bb.put(Convert.intTo4ByteArray(this.version));
    bb.put(Convert.intTo4ByteArray(this.headerlength));
    return bb.array();
  }

  /**
   * The first sent message object inits the session
   */
  protected void updateInit() {
    if (!this.init) {
      this.init = true;
    }
  }
}
