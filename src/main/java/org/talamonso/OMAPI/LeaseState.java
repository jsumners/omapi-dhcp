package org.talamonso.OMAPI;

public class LeaseState {
  public static final int FREE = 1;
  public static final int ACTIVE = 2;
  public static final int EXPIRED = 3;
  public static final int RELEASED = 4;
  public static final int ABANDONDED = 5;
  public static final int RESET = 6;
  public static final int BACKUP = 7;
  public static final int RESERVED = 8;
  public static final int BOOTP = 9;


  public static String nameForValue(int value) {
    String result = "unknown";

    switch (value) {
      case LeaseState.FREE:
        result = "FREE";
        break;
      case LeaseState.ACTIVE:
        result = "ACTIVE";
        break;
      case LeaseState.EXPIRED:
        result = "EXPIRED";
        break;
      case LeaseState.RELEASED:
        result = "RELEASED";
        break;
      case LeaseState.ABANDONDED:
        result = "ABANDONDED";
        break;
      case LeaseState.RESET:
        result = "RESET";
        break;
      case LeaseState.BACKUP:
        result = "BACKUP";
        break;
      case LeaseState.RESERVED:
        result = "RESERVED";
        break;
      case LeaseState.BOOTP:
        result = "BOOTP";
        break;
      default:
        break;
    }

    return result;
  }
}