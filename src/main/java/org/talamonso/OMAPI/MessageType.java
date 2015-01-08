package org.talamonso.OMAPI;

import java.io.Serializable;

public class MessageType implements Comparable<MessageType>, Serializable {
  private static final long serialVersionUID = 2L;

  public static final int UNKNOWN_VALUE = -1;
  public static final int OPEN_VALUE = 1;
  public static final int REFRESH_VALUE = 2;
  public static final int UPD_VALUE = 3;
  public static final int NOTIFY_VALUE = 4;
  public static final int ERROR_VALUE = 5;
  public static final int DEL_VALUE = 6;
  public static final int CREATE_VALUE = 7;
  public static final int DELETE_VALUE = 8;
  public static final int UPDATE_VALUE = 9;

  public static final MessageType UNKNOWN;
  public static final MessageType OPEN;
  public static final MessageType REFRESH;
  public static final MessageType UPD;
  public static final MessageType NOTIFY;
  public static final MessageType ERROR;
  public static final MessageType DEL;
  public static final MessageType CREATE;
  public static final MessageType DELETE;
  public static final MessageType UPDATE;

  static {
    UNKNOWN = typeOf(UNKNOWN_VALUE);
    OPEN    = typeOf(OPEN_VALUE);
    REFRESH = typeOf(REFRESH_VALUE);
    UPD     = typeOf(UPD_VALUE);
    NOTIFY  = typeOf(NOTIFY_VALUE);
    ERROR   = typeOf(ERROR_VALUE);
    DEL     = typeOf(DEL_VALUE);
    CREATE  = typeOf(CREATE_VALUE);
    DELETE  = typeOf(DELETE_VALUE);
    UPDATE  = typeOf(UPDATE_VALUE);
  }

  private final String type;

  public MessageType(String type) {
    this.type = type.toUpperCase();
  }

  public MessageType(int typeValue) {
    this.type = MessageType.typeForValue(typeValue);
  }

  public String getType() {
    return this.type;
  }

  public int value() {
    return MessageType.valueForType(this.type);
  }

  public static MessageType typeOf(int value) {
    return new MessageType(value);
  }

  public static String typeForValue(int value) {
    String result = "UNKNOWN";

    switch (value) {
      case OPEN_VALUE:
        result = "OPEN";
        break;
      case REFRESH_VALUE:
        result = "REFRESH";
        break;
      case  UPD_VALUE:
        result = "UPD";
        break;
      case NOTIFY_VALUE:
        result = "NOTIFY";
        break;
      case ERROR_VALUE:
        result = "ERROR";
        break;
      case DEL_VALUE:
        result = "DEL";
        break;
      case DELETE_VALUE:
        result = "DELETE";
        break;
      case CREATE_VALUE:
        result = "CREATE";
        break;
      case UPDATE_VALUE:
        result = "UPDATE";
        break;
      default:
        break;
    }

    return result;
  }

  public static int valueForType(String type) {
    int result;

    switch (type) {
      case "OPEN":
        result = MessageType.OPEN_VALUE;
        break;
      case "REFRESH":
        result = MessageType.REFRESH_VALUE;
        break;
      case "UPD":
        result = MessageType.UPD_VALUE;
        break;
      case "NOTIFY":
        result = MessageType.NOTIFY_VALUE;
        break;
      case "ERROR":
        result = MessageType.ERROR_VALUE;
        break;
      case "DEL":
        result = MessageType.DEL_VALUE;
        break;
      case"DELETE":
        result = MessageType.DELETE_VALUE;
        break;
      case "CREATE":
        result = MessageType.CREATE_VALUE;
        break;
      case "UPDATE":
        result = MessageType.UPDATE_VALUE;
        break;
      default:
        result = MessageType.UNKNOWN_VALUE;
        break;
    }

    return result;
  }

  @Override
  public int compareTo(MessageType o) {
    int result = 0;
    int oValue = o.value();
    int tValue = this.value();

    if (oValue < tValue) {
      result = -1;
    } else if (oValue > tValue) {
      result = 1;
    }

    return result;
  }
}