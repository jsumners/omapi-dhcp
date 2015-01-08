package org.talamonso.OMAPI;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Random;

import com.widget.util.Hex;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.talamonso.OMAPI.Exceptions.*;
import org.xbill.DNS.utils.HMAC;

/**
 * This abstract class contains the main functions for the communication. It also validates and signs the Messages.
 * @author   Talamonso
 * @version   1.0
 */
public abstract class Message {
  private static final Logger log = LoggerFactory.getLogger(Message.class);

  /**
   * The Answer of the dhcp server. Filled if this message was sent.
   */
  private byte[] answer = new byte[0];

  /**
   * Is set to 0x00000001 if the authentication is used
   */
  private byte[] authid = new byte[4];

  /**
   * length of the authenticator behind the payload (standard: 16 = 0x00000010)
   */
  private byte[] authlen = new byte[4];

  /**
   * Each answer of the Server contains an handle. So the server can recognise an update/delete.
   */
  private byte[] handle = new byte[4];

  /**
   * Saves the message keys and values.
   */
  private LinkedHashMap msg = new LinkedHashMap();

  /**
   * Is the message an open (1), error (5) or something else. (as described in api+protocol) message
   */
  private byte[] opcode = new byte[4];

  /**
   * Stores if and to wich message this is a response.
   */
  private byte[] rid = new byte[4];

  /**
   * Stores the signature of the message
   */
  private byte[] signature = new byte[16];

  /**
   * The transaction id
   */
  private byte[] tid = new byte[4];

  private LinkedHashMap upd_obj = new LinkedHashMap();

  /**
   * Stores the whole Connection informations needed for the communication
   */
  protected Connection connection;

  /**
   * Saves the object keys and values.
   */
  protected LinkedHashMap obj = new LinkedHashMap();

  /**
   * The Message Constructor. Sends an 8byte init message if it is the first message of this connection.
   * 
   * @param con Connection to the OMAPI Server
   */
  protected Message(Connection con) {
    log.debug("Creating emtpy Message object");
    this.connection = con;
    if (con.useAuth) {
      this.authid = Convert.intTo4ByteArray(1);
      this.authlen = Convert.intTo4ByteArray(16);
    }
  }

  /**
   * With Connection and an valid ByteArray this Constructor initialises an Message Object.
   * 
   * @param con Connection to the OMAPI Server
   * @param m Valid ByteArray from InputStream
   * @throws OmapiObjectException if the construction fails
   */
  protected Message(Connection con, byte[] m) throws OmapiException {
    log.debug("Attempting to assign incoming data to Message object");
    this.connection = con;
    if (m.length < 24 + 4 + 16) {
      throw new OmapiObjectException("Sorry. Incoming Message too small.");
    }
    ByteBuffer bb = ByteBuffer.allocate(m.length);
    bb.put(m);
    bb.position(0);
    bb.get(this.authid, 0, 4);
    bb.get(this.authlen, 0, 4);
    bb.get(this.opcode, 0, 4);
    bb.get(this.handle, 0, 4);
    bb.get(this.tid, 0, 4);
    bb.get(this.rid, 0, 4);
    int size = bb.limit() - bb.position();
    int plSize = size - Convert.byteArrayToInt(this.authlen);
    byte[] pl = new byte[plSize];
    bb.get(pl, 0, plSize);
    bb.get(this.signature, 0, Convert.byteArrayToInt(this.authlen));
    this.assignPayload(pl);
    if (Convert.byteArrayToInt(this.authlen) > 0) {
      this.checkMsg();
    }
    if (Convert.byteArrayToInt(this.opcode) == 5) {
      if (!this.getErrorCode().equals("success")) {
        throw new OmapiCallException(this.getErrorMsg(),
          this.getErrorCode());
      }
    }
  }

  /**
   * Try to delete this object on the server.
   * 
   * @throws OmapiConnectionException if the connection fails
   * @throws OmapiInitException if the initialisation fails
   * @throws OmapiObjectException if the Object has an error
   */
  public void delete() throws OmapiException {
    this.opcode = Convert.intTo4ByteArray(MessageType.DELETE);
    this.tid = this.newTid();
    this.rid = Convert.intTo4ByteArray(0);
    // We first need to have a handle!
    new EmptyMessage(
      this.connection,
      this.sendMessage(new MessageType(MessageType.OPEN))
    ).sendMessage(new MessageType(MessageType.DEL));
  }

  /**
   * Some objects examine an handle as an attribute. This method retrieves the objects represented by the handle.
   * 
   * @param h handle nr.
   * @return Message Object of the handle
   * @throws OmapiObjectException
   * @throws OmapiInitException
   * @throws OmapiConnectionException
   */
  public Message getMessageViaHandle(int h) throws OmapiException {
    Message x = new EmptyMessage(this.connection);
    x.handle = Convert.intTo4ByteArray(h);
    x = new EmptyMessage(
      this.connection,
      x.sendMessage(new MessageType(MessageType.REFRESH))
    );
    return x;
  }

  /**
   * This Message Object as a readable String
   * 
   * @return This Message Object as a readable String
   */
  public String toStringAll() {
    StringBuilder sb = new StringBuilder();
    sb.append("\nMessage:  " + "\n");
    sb.append("=========\n");
    sb.append("Header" + "\n");
    sb.append(" AuthID:          " + Hex.toHexF(this.authid) + "\n");
    sb.append(" AuthLength:      " + Hex.toHexF(this.authlen) + "\n");
    sb.append(" OpCode:          " + Hex.toHexF(this.opcode) + "\n");
    sb.append(" Handle:          " + Hex.toHexF(this.handle) + "\n");
    sb.append(" TransactionID:   " + Hex.toHexF(this.tid) + "\n");
    sb.append(" ResponseID:      " + Hex.toHexF(this.rid) + "\n");
    sb.append("Payload" + "\n");
    sb.append(this.decodePayload());
    sb.append("\n");
    return sb.toString();
  }

  /**
   * Add an key - value pair to the Msg payload.
   * 
   * @param key eg. type
   * @param value as an bytearray
   */
  private void addMsg(String key, byte[] value) {
    this.msg.put(key, value);
    log.trace(
      "{} extended with Message: `{key: '{}', value: '{}'}`",
      this.getClass().getName(),
      key,
      Hex.toHexF(value)
    );
  }

  /**
   * Add an key - value pair to the Obj payload.
   * 
   * @param key eg. ip-address
   * @param value as an bytearray
   */
  private void addObj(String key, byte[] value) {
    this.obj.put(key, value);
    log.trace(
      "{} extended with attribute: `{key: '{}', value: '{}}`",
      this.getClass().getName(),
      key,
      Hex.toHexF(value)
    );
  }

  /**
   * Assigns the given bytearray to the msg and obj Maps
   * 
   * @param pl
   */
  private void assignPayload(byte[] pl) {
    ByteBuffer bb = ByteBuffer.wrap(pl);
    int l1, l2 = 0;
    while (true) {
      byte[] KeyLength = new byte[2];
      bb.get(KeyLength, 0, 2);
      if (Convert.byteArrayToInt(KeyLength) == 0) {
        break;
      }
      l1 = Convert.byteArrayToInt(KeyLength);
      byte[] Key = new byte[l1];
      bb.get(Key, 0, l1);
      byte[] ValueLength = new byte[4];
      bb.get(ValueLength, 0, 4);
      l1 = Convert.byteArrayToInt(ValueLength);
      byte[] Value = new byte[l1];
      bb.get(Value, 0, l1);
      this.addMsg(new String(Key), Value);
    }
    while (true) {
      byte[] KeyLength = new byte[2];
      bb.get(KeyLength, 0, 2);
      if (Convert.byteArrayToInt(KeyLength) == 0) {
        break;
      }
      l2 = Convert.byteArrayToInt(KeyLength);
      byte[] Key = new byte[l2];
      bb.get(Key, 0, l2);
      if (new String(Key).equals("flags")) {
        // TODO remove this stupid workaround if patch is available
        break;
      }
      byte[] ValueLength = new byte[4];
      bb.get(ValueLength, 0, 4);
      l2 = Convert.byteArrayToInt(ValueLength);
      byte[] Value = new byte[l2];
      bb.get(Value, 0, l2);
      this.addObj(new String(Key), Value);
    }
  }

  /**
   * Checks the signature of the incoming messages from the server. Trust noone!
   * 
   * @return true if signature is valid, false if not
   */
  private boolean checkMsg() {
    ByteBuffer bb = ByteBuffer.allocate(20 + this.getPayload().length);
    bb.put(this.authlen);
    bb.put(this.opcode);
    bb.put(this.handle);
    bb.put(this.tid);
    bb.put(this.rid);
    bb.put(this.getPayload());
    HMAC hmac = new HMAC("md5", this.connection.getKey());
    hmac.update(bb.array());
    return Hex.toHex(hmac.sign()).equals(Hex.toHex(this.signature));
  }

  /**
   * Sends this Message to the Server.
   * 
   * @param action int Value.
   * @return Server answer as bytearray
   * @throws OmapiConnectionException is thrown if Server connection failes
   */
  private byte[] commit(int action) throws OmapiConnectionException {
    log.debug(
      "Trying to {} {}",
      MessageType.nameForValue(action),
      this.getClass().getName()
    );

    try {
      this.connection.out.write(this.toByteArray());
      this.connection.out.flush();
      byte[] b = new byte[1024];
      int count = (this.connection.in.read(b));
      ByteBuffer bb = ByteBuffer.allocate(count);
      bb.put(b, 0, count);
      this.answer = bb.array();
    } catch (IOException e) {
      throw new OmapiConnectionException(e.getMessage());
    }

    this.connection.updateInit();
    log.debug(
      " - Successfully {}ed ...",
      MessageType.nameForValue(action)
    );

    return this.answer;
  }

  /**
   * Converts the payload to a readable String
   * 
   * @return the decoded Message
   */
  private String decodePayload() {
    StringBuilder sb = new StringBuilder();
    Iterator it;
    sb.append(" Message:\n");
    it = this.msg.entrySet().iterator();
    while (it.hasNext()) {
      Map.Entry pairs = (Map.Entry) it.next();
      sb.append("  " + pairs.getKey() + ":");
      sb.append(Hex.toHexF((byte[]) pairs.getValue()) + "\n");
    }
    sb.append(" Objects:\n");
    it = this.obj.entrySet().iterator();
    while (it.hasNext()) {
      Map.Entry pairs = (Map.Entry) it.next();
      sb.append("  " + pairs.getKey() + ":");
      sb.append(Hex.toHexF((byte[]) pairs.getValue()) + "\n");
    }
    return sb.toString();
  }

  /**
   * Get the Error code from the server out of the Message if available.
   * 
   * @return Error code from the server
   */
  private Integer getErrorCode() {
    byte[] b = this.getMsg("result");
    if (b != null) {
      return Convert.byteArrayToInt(b);
    }
    return null;
  }

  /**
   * Get the Error Message from the server out of the Message if available.
   * 
   * @return Error Message from the server
   */
  private String getErrorMsg() {
    byte[] b = this.getMsg("message");
    if (b != null) {
      return new String(b);
    }
    return "";
  }

  /**
   * @return The payload of this Message object ready for transmitting
   */
  private byte[] getPayload() {
    ByteBuffer bb = ByteBuffer.allocate(1024);
    Iterator it;
    it = this.msg.entrySet().iterator();
    while (it.hasNext()) {
      Map.Entry pairs = (Map.Entry) it.next();
      bb.put(Convert.intTo2ByteArray(pairs.getKey().toString().length()));
      bb.put(pairs.getKey().toString().getBytes());
      bb.put(Convert.intTo4ByteArray(((byte[]) pairs.getValue()).length));
      bb.put((byte[]) pairs.getValue());
    }
    bb.put(Convert.intTo2ByteArray(0));
    it = this.obj.entrySet().iterator();
    while (it.hasNext()) {
      Map.Entry pairs = (Map.Entry) it.next();
      bb.put(Convert.intTo2ByteArray(pairs.getKey().toString().length()));
      bb.put(pairs.getKey().toString().getBytes());
      bb.put(Convert.intTo4ByteArray(((byte[]) pairs.getValue()).length));
      bb.put((byte[]) pairs.getValue());
    }
    bb.put(Convert.intTo2ByteArray(0));
    byte[] ba = new byte[bb.position()];
    bb.position(0);
    bb.get(ba);
    return ba;
  }

  /**
   * Calculates a random transaction id
   * 
   * @return transaction id as bytearray
   */
  private byte[] newTid() {
    Random generator = new Random();
    return Convert.intTo4ByteArray(generator.nextInt());
  }

  /**
   * Signs this Message object with the key stored in the connection.
   * 
   * @return The signature as a ByteArray
   */
  private byte[] signMsg() {
    ByteBuffer bb = ByteBuffer.allocate(20 + this.getPayload().length);
    bb.put(this.authlen);
    bb.put(this.opcode);
    bb.put(this.handle);
    bb.put(this.tid);
    bb.put(this.rid);
    bb.put(this.getPayload());
    HMAC hmac = new HMAC("md5", this.connection.getKey());
    hmac.update(bb.array());
    return hmac.sign();
  }

  /**
   * Convert the Message object for tranfer to the server
   * 
   * @return ByteArray representing this Message object
   */
  private byte[] toByteArray() {
    int hl = this.connection.getHeaderLength();
    int pll = this.getPayload().length;
    int al = Convert.byteArrayToInt(this.authlen);
    int size = hl + pll + al;
    if (!this.connection.init) {
      size = size + 8;
    }
    ByteBuffer bb = ByteBuffer.allocate(size);
    if (!this.connection.init) {
      bb.put(this.connection.initValue());
    }
    bb.put(this.authid);
    bb.put(this.authlen);
    bb.put(this.opcode);
    bb.put(this.handle);
    bb.put(this.tid);
    bb.put(this.rid);
    bb.put(this.getPayload());
    if (this.connection.useAuth) {
      bb.put(this.signMsg());
    }
    return bb.array();
  }

  private void updObj(String key, byte[] value) {
    this.upd_obj.put(key, value);
    log.trace(
      "{} updated attribute with: `{key: '{}', value: '{}'}`",
      this.getClass().getName(),
      Hex.toHexF(value)
    );
  }

  /**
   * Get the specified message of this Message object
   * 
   * @param key The key of the requested value
   * @return allways as a bytearray (as transferred)
   */
  protected byte[] getMsg(String key) {
    return (byte[]) this.msg.get(key);
  }

  /**
   * Convert an attribute giben via key to a date (in UTF)
   * 
   * @param key Object representing a date value
   * @return yyyy/MM/dd HH:mm:ss
   */
  protected String getObjectAsDate(String key) {
    byte[] b = (byte[]) this.obj.get(key);
    if (b != null) {
      return Convert.hex2date(b);
    }
    return "";
  }

  /**
   * This function returns the attribute value as an hex formatted String
   * 
   * @param key name of the attribute
   * @return hex representation of the attribute
   */
  protected String getObjectAsHex(String key) {
    byte[] b = (byte[]) this.obj.get(key);
    if (b != null) {
      return Hex.toHexF(b);
    }
    return "";
  }

  /**
   * Get the specified object of this Message object
   * 
   * @param key The key of the requested value
   * @return allways as a bytearray (as transferred)
   */
  protected int getObjectAsInt(String key) {
    byte[] b = (byte[]) this.obj.get(key);
    if (b != null) {
      return Convert.byteArrayToInt((byte[]) this.obj.get(key));
    }
    return 0;
  }

  /**
   * This function returns the attribute value as an ip-address String
   * 
   * @param key name of the attribute
   * @return ip address representation of the attribute
   */
  protected String getObjectAsIpAddress(String key) {
    byte[] b = (byte[]) this.obj.get(key);
    if (b == null || b.length % 4 != 0) {
      return "";
    }
    StringBuilder sb = new StringBuilder();
    ByteBuffer bb = ByteBuffer.wrap(b);
    byte[] x = new byte[4];
    while (bb.position() < bb.limit()) {
      bb.get(x, 0, 4);
      sb.append(Convert.hex2ip(x) + ",");
    }
    sb.deleteCharAt(sb.length() - 1);
    return sb.toString();
  }

  protected byte[] getObjectAsByteArray(String key) {
    return (byte[]) this.obj.get(key);
  }

  /**
   * This function returns the attribute value as an mac formatted String
   * 
   * @param key name of the attribute
   * @return mac address representation of the attribute
   */
  protected String getObjectAsMacAddress(String key) {
    byte[] b = (byte[]) this.obj.get(key);
    if (b != null) {
      return Convert.hex2mac(b);
    }
    return "";
  }

  /**
   * This function returns the attribute value as a String
   * 
   * @param key name of the attribute
   * @return String representation of the attribute
   */
  protected String getObjectAsString(String key) {
    byte[] b = this.getObjectRaw(key);
    if (b != null) {
      return new String(b);
    }
    return "";
  }

  /**
   * This function returns the attribute value as a bytearray
   * 
   * @param key name of the attribute
   * @return bytearray representation of the attribute
   */
  protected byte[] getObjectRaw(String key) {
    byte[] b = (byte[]) this.obj.get(key);
    if (b != null) {
      return b;
    }
    return new byte[0];
  }

  /**
   * <p>Commits the Data to the server and returns the byteArray from the
   * answer.</p>
   *
   * <p>There are severall options for the parameter action.
   * Use these constants:</p>
   *
   * <ul>
   *   <li>{@link org.talamonso.OMAPI.MessageType#OPEN}</li>
   *   <li>{@link org.talamonso.OMAPI.MessageType#REFRESH}</li>
   *   <li>{@link org.talamonso.OMAPI.MessageType#UPDATE}</li>
   *   <li>{@link org.talamonso.OMAPI.MessageType#CREATE}</li>
   * </ul>
   * 
   * @param messageType shown above
   * @return the answer of the server as a ByteArray
   * @throws OmapiConnectionException if the connection fails
   * @throws OmapiInitException if the initialisation fails
   * @throws OmapiObjectException if the Object has an error
   */
  protected byte[] sendMessage(MessageType messageType) throws OmapiException {
    this.opcode = Convert.intTo4ByteArray(messageType.getType());
    this.tid = this.newTid();
    this.rid = Convert.intTo4ByteArray(0);

    switch (messageType.getType()) {
      case MessageType.DEL:
      case MessageType.REFRESH:
        // We don't need to have the payload for DELETE and REFRESH
        this.msg = new LinkedHashMap();
        this.obj = new LinkedHashMap();
        // break for REFRESH!
        break;

      case MessageType.CREATE:
        this.handle = Convert.intTo4ByteArray(1);
        this.opcode = Convert.intTo4ByteArray(MessageType.OPEN);
        this.addMsg("create", Convert.intTo4ByteArray(1));
        this.addMsg("exclusive", Convert.intTo4ByteArray(1));
        break;

      case MessageType.UPDATE:
        EmptyMessage in = new EmptyMessage(
          this.connection,
          this.sendMessage(new MessageType(MessageType.OPEN))
        );
        in.obj = this.upd_obj;
        in.sendMessage(new MessageType(MessageType.UPD));
        // FIXME: I don't like this return. ~ jsumners
        return in.sendMessage(new MessageType(MessageType.REFRESH));
        //break;

      case MessageType.OPEN:
      case MessageType.UPD:
        break;

      default:
        throw new OmapiConnectionException("Unknown command");
    }

    return this.commit(messageType.getType());
  }

  /**
   * Update the "key" Message with the text from "value".
   * 
   * @param key key of the attribute to change.
   * @param value value for this attribute.
   */
  protected void setMessage(String key, String value) {
    this.msg.put(key, value.getBytes());
  }

  /**
   * Set key with integer value.
   * 
   * @param key name of attribute
   * @param value integer value
   */
  protected void setObjectAsInt(String key, int value) {
    this.addObj(key, Convert.intTo4ByteArray(value));
  }

  /**
   * Set key as an ip address. Multiple ip addresses could be seperated by an comma.
   * 
   * @param key name of the attribute
   * @param value ip address as String like "192.168.0.1"
   * @throws OmapiObjectException if String isn't an ip address.
   */
  protected void setObjectAsIpAddresses(String key, String value) throws OmapiObjectException {
    String[] s = value.split(",");
    ByteBuffer bb = ByteBuffer.allocate(s.length * 4);
    for (int i = 0; i < s.length; i++) {
      bb.put(Convert.ip2hex(s[i]));
    }
    this.addObj(key, bb.array());
  }

  /**
   * Set key as mac address.
   * 
   * @param key name of the attribute
   * @param value mac address as hex string like "aa:bb:aa:bb:aa:bb"
   * @throws OmapiObjectException if not well formatted
   */
  protected void setObjectAsMacAddress(String key, String value) throws OmapiObjectException {
    this.addObj(key, Convert.mac2hex(value));
  }

  /**
   * set the "key" attribute with "value".
   * 
   * @param key key of the attribute to set.
   * @param value value for this attribute.
   */
  protected void setObjectAsString(String key, String value) {
    this.addObj(key, value.getBytes());
  }

  /**
   * Update the "key" attribute with "value" as integer.
   * 
   * @param key key of the attribute to change.
   * @param value integer value for this attribute.
   */
  protected void updateObjectAsInt(String key, int value) {
    this.updObj(key, Convert.intTo4ByteArray(value));
  }

  /**
   * Update the "key" attribute with the comma seperated ip addresses from "value".
   * 
   * @param key key of the attribute to change.
   * @param value comma seperated ip addresses for this attribute.
   * @throws OmapiObjectException if any ip address isn't valid.
   */
  protected void updateObjectAsIpAddresses(String key, String value) throws OmapiObjectException {
    String[] s = value.split(",");
    ByteBuffer bb = ByteBuffer.allocate(s.length * 4);
    for (int i = 0; i < s.length; i++) {
      bb.put(Convert.ip2hex(s[i]));
    }
    this.updObj(key, bb.array());
  }

  /**
   * Update the "key" attribute with "value".
   * 
   * @param key key of the attribute to change.
   * @param value value for this attribute.
   */
  protected void updateObjectAsString(String key, String value) {
    this.updObj(key, value.getBytes());
  }
}
