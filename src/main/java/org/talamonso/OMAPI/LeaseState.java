package org.talamonso.OMAPI;

import java.io.Serializable;

public class LeaseState implements Comparable<LeaseState>, Serializable {
  private static final long serialVersionUID = 2L;

  public static final int FREE_VALUE = 1;
  public static final int ACTIVE_VALUE = 2;
  public static final int EXPIRED_VALUE = 3;
  public static final int RELEASED_VALUE = 4;
  public static final int ABANDONDED_VALUE = 5;
  public static final int RESET_VALUE = 6;
  public static final int BACKUP_VALUE = 7;
  public static final int RESERVED_VALUE = 8;
  public static final int BOOTP_VALUE = 9;

  public static final LeaseState FREE;
  public static final LeaseState ACTIVE;
  public static final LeaseState EXPIRED;
  public static final LeaseState RELEASED;
  public static final LeaseState ABANDONDED;
  public static final LeaseState RESET;
  public static final LeaseState BACKUP;
  public static final LeaseState RESERVED;
  public static final LeaseState BOOTP;

  static {
    FREE        = stateOf(FREE_VALUE);
    ACTIVE      = stateOf(ACTIVE_VALUE);
    EXPIRED     = stateOf(EXPIRED_VALUE);
    RELEASED    = stateOf(RELEASED_VALUE);
    ABANDONDED  = stateOf(ABANDONDED_VALUE);
    RESET       = stateOf(RESET_VALUE);
    BACKUP      = stateOf(BACKUP_VALUE);
    RESERVED    = stateOf(RESERVED_VALUE);
    BOOTP       = stateOf(BOOTP_VALUE);
  }

  private final String state;

  public LeaseState(String state) {
    this.state = state.toUpperCase();
  }

  public LeaseState(int value) {
    this.state = LeaseState.stateForValue(value);
  }

  public String getState() {
    return this.state;
  }

  public int value() {
    return LeaseState.valueForState(this.state);
  }

  public static LeaseState stateOf(int value) {
    return new LeaseState(value);
  }

  public static int valueForState(String state) {
    int result = -1;

    switch (state) {
      case "FREE":
        result = LeaseState.FREE_VALUE;
        break;
      case "ACTIVE":
        result = LeaseState.ACTIVE_VALUE;
        break;
      case "EXPIRED":
        result = LeaseState.EXPIRED_VALUE;
        break;
      case "RELEASED":
        result = LeaseState.RELEASED_VALUE;
        break;
      case "ABANDONDED":
        result = LeaseState.ABANDONDED_VALUE;
        break;
      case "RESET":
        result = LeaseState.RESET_VALUE;
        break;
      case "BACKUP":
        result = LeaseState.BACKUP_VALUE;
        break;
      case "RESERVED":
        result = LeaseState.RESERVED_VALUE;
        break;
      case "BOOTP":
        result = LeaseState.BOOTP_VALUE;
        break;
      default:
        break;
    }

    return result;
  }

  public static String stateForValue(int value) {
    String result = "UNKNOWN";

    switch (value) {
      case LeaseState.FREE_VALUE:
        result = "FREE";
        break;
      case LeaseState.ACTIVE_VALUE:
        result = "ACTIVE";
        break;
      case LeaseState.EXPIRED_VALUE:
        result = "EXPIRED";
        break;
      case LeaseState.RELEASED_VALUE:
        result = "RELEASED";
        break;
      case LeaseState.ABANDONDED_VALUE:
        result = "ABANDONDED";
        break;
      case LeaseState.RESET_VALUE:
        result = "RESET";
        break;
      case LeaseState.BACKUP_VALUE:
        result = "BACKUP";
        break;
      case LeaseState.RESERVED_VALUE:
        result = "RESERVED";
        break;
      case LeaseState.BOOTP_VALUE:
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