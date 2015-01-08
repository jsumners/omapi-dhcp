package org.talamonso.OMAPI;

import java.io.Serializable;

public class MessageType implements Comparable<MessageType>, Serializable {
  private static final long serialVersionUID = 1L;

  public static final int OPEN = 1;
  public static final int REFRESH = 2;
  public static final int UPD = 3;
  public static final int NOTIFY = 4;
  public static final int ERROR = 5;
  public static final int DEL = 6;
  public static final int CREATE = 7;
  public static final int DELETE = 8;
  public static final int UPDATE = 9;

  private final int type;

  public MessageType(int type) {
    this.type = type;
  }

  public int getType() {
    return this.type;
  }

  public static String nameForType(MessageType messageType) {
    return MessageType.nameForValue(messageType.getType());
  }

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

  @Override
  public int compareTo(MessageType o) {
    int result = 0;
    int oType = o.getType();

    if (oType < this.type) {
      result = -1;
    } else if (oType > this.type) {
      result = 1;
    }

    return result;
  }
}