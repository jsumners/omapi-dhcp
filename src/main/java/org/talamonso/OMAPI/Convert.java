package org.talamonso.OMAPI;

import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

import com.google.common.collect.Range;
import com.google.common.net.InetAddresses;
import com.widget.util.Hex;
import org.talamonso.OMAPI.Exceptions.OmapiObjectException;

/**
 * Class to convert entries. A few methods used by the OMAPI implementation which are not covered by the imported
 * classes.
 * 
 * @author Talamonso
 */
public final class Convert {

  /**
   * Convert a byte array to an int value.
   * 
   * @param byteArray bytearray to convert
   * @return converted value as an int value
   */
  protected static int byteArrayToInt(byte[] byteArray) {
    return Convert.byteArrayToInt(byteArray, 0, byteArray.length);
  }

  /**
   * Convert a byte array to an int value.
   * 
   * @param byteArray to convert
   * @param startPos use this as the starting element.
   * @param length lenght of the bytes to read.
   * @return converted value as an int value
   */
  protected static int byteArrayToInt(byte[] byteArray, int startPos, int length) {
    int value = 0x00000000;
    switch (length) {
    case 1:
      value |= (0x000000FF & byteArray[startPos]);
      break;
    case 2:
      value |= ((0x000000FF & byteArray[startPos]) << 8) | (0x000000FF & byteArray[startPos + 1]);
      break;
    case 3:
      value |= ((0x000000FF & byteArray[startPos]) << 16) | ((0x000000FF & byteArray[startPos + 1]) << 8) | (0x000000FF & byteArray[startPos + 2]);
      break;
    case 4:
      value |= ((0x000000FF & byteArray[startPos]) << 24) | ((0x000000FF & byteArray[startPos + 1]) << 16) | ((0x000000FF & byteArray[startPos + 2]) << 8)
          | (0x000000FF & byteArray[startPos + 3]);
      break;
    }
    return value;
  }

  /**
   * converts the date value to a human readable String.<br />
   * Format: yyyy/MM/dd HH:mm:ss
   * 
   * @param b bytearray as commited by the server.
   * @return yyyy/MM/dd HH:mm:ss
   */
  protected static String hex2date(byte[] b) {
    long epochSeconds = Convert.unsignedIntToLong(b);
    Instant instant = Instant.ofEpochSecond(epochSeconds);
    ZonedDateTime zonedDateTime = ZonedDateTime.ofInstant(instant, ZoneId.of("UTC"));
    DateTimeFormatter formatter = DateTimeFormatter.RFC_1123_DATE_TIME;
    return formatter.format(zonedDateTime);
  }

  /**
   * converts a byte[4] to an ip address.
   * 
   * @param b ip addres as bytearray
   * @return String representation of the ip address
   */
  protected static String hex2ip(byte[] b) {
    if (b != null) {
      StringBuilder sb = new StringBuilder();
      sb.append((b[0] & 0xFF) + ".");
      sb.append((b[1] & 0xFF) + ".");
      sb.append((b[2] & 0xFF) + ".");
      sb.append((b[3] & 0xFF));
      return sb.toString();
    }
    return "";
  }

  /**
   * converts a byte[6] to an mac address
   * 
   * @param b mac addres as bytearray
   * @return String representation of the mac address
   */
  protected static String hex2mac(byte[] b) {
    if (b != null) {
      StringBuilder sb = new StringBuilder();
      for (int i = 0; i < b.length; i++) {
        sb.append(Hex.toHex(b[i]));
        if (i + 1 < b.length) {
          sb.append(":");
        }
      }
      return sb.toString();
    }
    return "";
  }

  /**
   * Converts an int value to a byte array with 2 elements.
   * 
   * @param value to convert
   * @return converted value as a byte array
   */
  protected static byte[] intTo2ByteArray(int value) {
    byte[] b = new byte[2];
    for (int i = 0; i < 2; i++) {
      int offset = (b.length - 1 - i) * 8;
      b[i] = (byte) ((value >>> offset) & 0xFF);
    }
    return b;
  }

  /**
   * Converts an int value to a byte array with 4 elements.
   * 
   * @param value to convert
   * @return converted value as a byte array
   */
  protected static byte[] intTo4ByteArray(int value) {
    byte[] b = new byte[4];
    for (int i = 0; i < 4; i++) {
      int offset = (b.length - 1 - i) * 8;
      b[i] = (byte) ((value >>> offset) & 0xFF);
    }
    return b;
  }

  /**
   * Converts an int value to a single byte
   * 
   * @param value to convert
   * @return converted value as a byte
   */
  protected static byte intToByte(int value) {
    return (byte) value;
  }

  /**
   * Converts an ip address to an byte array representing the hex writing used by OMAPI
   * 
   * @param strIPAddress ip address. For example 10.190.21.3
   * @return bytearray representing hex writing of the ip address.
   * @throws OmapiObjectException is thrown if the ip address is not a valid one.
   */
  protected static byte[] ip2hex(String strIPAddress) throws OmapiObjectException {
    if (!InetAddresses.isInetAddress(strIPAddress)) {
      throw new OmapiObjectException("invalid ip address.");
    }

    String[] parts = strIPAddress.split("\\.");
    Range<Integer> range = Range.closed(0, 255);
    byte[] hex = new byte[4];
    for (int i = 0, j = parts.length; i < j; i += 1) {
      int octet = Integer.parseInt(parts[i], 10);
      if ( range.contains(octet) ) {
        hex[i] = (byte) octet;
      }
    }

    return hex;
  }

  /**
   * Converts a hardware identifier (also known as MAC-address) in an bytearray
   * 
   * @param mac MAC-address in well known format as String (aa:bb:cc:dd:ee:ff)
   * @return Mac-address as a bytearray
   * @throws OmapiObjectException is thrown ifMAC address is not in the expected format
   */
  protected static byte[] mac2hex(String mac) throws OmapiObjectException {
    if (mac.matches("^([0-9a-fA-F]{2}[:-]){5}([0-9a-fA-F]{2})$")) {
      return Hex.toByteArr(mac.replaceAll(":", ""));
    }
    throw new OmapiObjectException("Illegal MAC Address: " + mac);
  }

  /**
   * Converts a 4 byte array of unsigned bytes to an long
   * 
   * @param b an array of 4 unsigned bytes
   * @return a long representing the unsigned int
   */
  protected static final long unsignedIntToLong(byte[] b) {
    long l = 0;
    l |= b[0] & 0xFF;
    l <<= 8;
    l |= b[1] & 0xFF;
    l <<= 8;
    l |= b[2] & 0xFF;
    l <<= 8;
    l |= b[3] & 0xFF;
    return l;
  }

  /**
   * Umwandlung der IP vom dotted-quad-Format (xxx.yyy.zzz.aaa) in das long-Format
   * 
   * @param IP IP-Adresse in dotted quad Formatierung
   * @return IP-Adresse im long Format
   * @throws OmapiObjectException
   */
  public static long ip2long(String IP) throws OmapiObjectException {
    if (!InetAddresses.isInetAddress(IP)) {
      throw new OmapiObjectException("invalid ip address.");
    }

    String[] octets = IP.split("\\.");
    long S0 = (long) (Integer.valueOf(octets[0]).doubleValue() * Math.pow(256, 3));
    long S1 = (long) (Integer.valueOf(octets[1]).doubleValue() * Math.pow(256, 2));
    long S2 = (long) (Integer.valueOf(octets[2]).doubleValue() * Math.pow(256, 1));
    long S3 = (long) (Integer.valueOf(octets[3]).doubleValue() * Math.pow(256, 0));
    long base10 = S0 + S1 + S2 + S3;
    return base10;
  }

  /**
   * Umwandlung der IP vom long-Format in das dotted-quad-Format (xxx.yyy.zzz.aaa)
   * 
   * @param l IP-Adresse im long Format
   * @return IP-Adresse in dotted quad Formatierung
   */
  public static String long2ip(long l) {
    String s = "";
    for (int i1 = 0; i1 < 4; i1++) {
      if (i1 > 0) {
        s = s + ".";
      }
      int k = (int) ((l / Math.pow(2D, (3 - i1) * 8)) % 256D);
      s = s + "" + k;
    }
    return s;
  }
}
