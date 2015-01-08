package org.talamonso.OMAPI;

public class MessageType {
  public static final int OPEN = 1;
  public static final int REFRESH = 2;
  public static final int UPD = 3;
  public static final int NOTIFY = 4;
  public static final int ERROR = 5;
  public static final int DEL = 6;
  public static final int CREATE = 7;
  public static final int DELETE = 8;
  public static final int UPDATE = 9;

  public static String nameForValue(int value) {
    String result = "unknown";

    switch (value) {
      case MessageType.OPEN:
        result = "OPEN";
        break;
      case MessageType.REFRESH:
        result = "REFRESH";
        break;
      case MessageType.UPDATE:
        result = "UPDATE";
        break;
      case MessageType.NOTIFY:
        result = "NOTIFY";
        break;
      case MessageType.ERROR:
        result = "ERROR";
        break;
      case MessageType.DELETE:
        result = "DELETE";
        break;
      case MessageType.CREATE:
        result = "CREATE";
        break;
      default:
        break;
    }

    return result;
  }
}