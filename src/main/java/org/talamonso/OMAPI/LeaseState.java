package org.talamonso.OMAPI;

import java.io.Serializable;

public class LeaseState implements Comparable<LeaseState>, Serializable {
  private static final long serialVersionUID = 1L;

  public static final int FREE = 1;
  public static final int ACTIVE = 2;
  public static final int EXPIRED = 3;
  public static final int RELEASED = 4;
  public static final int ABANDONDED = 5;
  public static final int RESET = 6;
  public static final int BACKUP = 7;
  public static final int RESERVED = 8;
  public static final int BOOTP = 9;

  private final int state;

  public LeaseState(int state) {
    this.state = state;
  }

  public int getState() {
    return this.state;
  }

  public static String nameForValue(LeaseState leaseState) {
    return LeaseState.nameForValue(leaseState.getState());
  }

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

  @Override
  public int compareTo(LeaseState o) {
    int result = 0;
    int oState = o.getState();

    if (oState < this.state) {
      result = -1;
    } else if (oState > this.state) {
      result = 1;
    }

    return result;
  }
}