package org.talamonso.OMAPI;

import java.io.Serializable;

public class MessageOperation implements Comparable<MessageOperation>, Serializable {
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

  public static final MessageOperation UNKNOWN;
  public static final MessageOperation OPEN;
  public static final MessageOperation REFRESH;
  public static final MessageOperation UPD;
  public static final MessageOperation NOTIFY;
  public static final MessageOperation ERROR;
  public static final MessageOperation DEL;
  public static final MessageOperation CREATE;
  public static final MessageOperation DELETE;
  public static final MessageOperation UPDATE;

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

  public MessageOperation(String type) {
    this.type = type.toUpperCase();
  }

  public MessageOperation(int typeValue) {
    this.type = MessageOperation.typeForValue(typeValue);
  }

  public String getType() {
    return this.type;
  }

  public int value() {
    return MessageOperation.valueForType(this.type);
  }

  public static MessageOperation typeOf(int value) {
    return new MessageOperation(value);
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
        result = MessageOperation.OPEN_VALUE;
        break;
      case "REFRESH":
        result = MessageOperation.REFRESH_VALUE;
        break;
      case "UPD":
        result = MessageOperation.UPD_VALUE;
        break;
      case "NOTIFY":
        result = MessageOperation.NOTIFY_VALUE;
        break;
      case "ERROR":
        result = MessageOperation.ERROR_VALUE;
        break;
      case "DEL":
        result = MessageOperation.DEL_VALUE;
        break;
      case"DELETE":
        result = MessageOperation.DELETE_VALUE;
        break;
      case "CREATE":
        result = MessageOperation.CREATE_VALUE;
        break;
      case "UPDATE":
        result = MessageOperation.UPDATE_VALUE;
        break;
      default:
        result = MessageOperation.UNKNOWN_VALUE;
        break;
    }

    return result;
  }

  @Override
  public int compareTo(MessageOperation o) {
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

  @Override
  public String toString() {
    return MessageOperation.typeForValue(this.value());
  }
}