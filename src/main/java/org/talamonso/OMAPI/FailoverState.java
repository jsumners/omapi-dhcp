package org.talamonso.OMAPI;

import java.io.Serializable;

public class FailoverState implements Comparable<FailoverState>, Serializable {
  private static final long serialVersionUID = 2L;

  public static final int STARTUP_VALUE = 1;
  public static final int NORMAL_VALUE = 2;
  public static final int COMM_INTERRUPTED_VALUE = 3;
  public static final int DOWN_VALUE = 4;
  public static final int CONFLICT_VALUE = 5;
  public static final int RECOVER_VALUE = 6;
  public static final int PAUSED_VALUE = 7;
  public static final int SHUTDOWN_VALUE = 8;
  public static final int RECOVER_DONE_VALUE = 9;
  public static final int RESOLUTION_INTERRUPTED_VALUE = 10;
  public static final int CONFLICT_DONE_VALUE = 11;
  public static final int RECOVER_WAIT_VALUE = 12;

  public static final FailoverState STARTUP;
  public static final FailoverState NORMAL;
  public static final FailoverState COMM_INTERRUPTED;
  public static final FailoverState DOWN;
  public static final FailoverState CONFLICT;
  public static final FailoverState RECOVER;
  public static final FailoverState PAUSED;
  public static final FailoverState SHUTDOWN;
  public static final FailoverState RECOVER_DONE;
  public static final FailoverState RESOLUTION_INTERRUPTED;
  public static final FailoverState CONFLICT_DONE;
  public static final FailoverState RECOVER_WAIT;

  static {
    STARTUP                 = stateOf(STARTUP_VALUE);
    NORMAL                  = stateOf(NORMAL_VALUE);
    COMM_INTERRUPTED        = stateOf(COMM_INTERRUPTED_VALUE);
    DOWN                    = stateOf(DOWN_VALUE);
    CONFLICT                = stateOf(CONFLICT_VALUE);
    RECOVER                 = stateOf(RECOVER_VALUE);
    PAUSED                  = stateOf(PAUSED_VALUE);
    SHUTDOWN                = stateOf(SHUTDOWN_VALUE);
    RECOVER_DONE            = stateOf(RECOVER_DONE_VALUE);
    RESOLUTION_INTERRUPTED  = stateOf(RESOLUTION_INTERRUPTED_VALUE);
    CONFLICT_DONE           = stateOf(CONFLICT_DONE_VALUE);
    RECOVER_WAIT            = stateOf(RECOVER_WAIT_VALUE);
  }

  private final String state;

  public FailoverState(String state) {
    this.state = state.toUpperCase();
  }

  public FailoverState(int value) {
    this.state = FailoverState.stateForValue(value);
  }

  public String getState() {
    return this.state;
  }

  public int value() {
    return FailoverState.valueForState(this.state);
  }

  public static FailoverState stateOf(int value) {
    return new FailoverState(value);
  }

  public static int valueForState(String state) {
    int result = -1;

    switch (state) {
      case "STARTUP":
        result = FailoverState.STARTUP_VALUE;
        break;
      case "NORMAL":
        result = FailoverState.NORMAL_VALUE;
        break;
      case "COMM_INTERRUPTED":
        result = FailoverState.COMM_INTERRUPTED_VALUE;
        break;
      case "DOWN":
        result = FailoverState.DOWN_VALUE;
        break;
      case "CONFLICT":
        result = FailoverState.CONFLICT_VALUE;
        break;
      case "RECOVER":
        result = FailoverState.RECOVER_VALUE;
        break;
      case "PAUSED":
        result = FailoverState.PAUSED_VALUE;
        break;
      case "SHUTDOWN":
        result = FailoverState.SHUTDOWN_VALUE;
        break;
      case "RECOVER_DONE":
        result = FailoverState.RECOVER_DONE_VALUE;
        break;
      case "RESOLUTION_INTERRUPTED":
        result = FailoverState.RESOLUTION_INTERRUPTED_VALUE;
        break;
      case "CONFLICT_DONE":
        result = FailoverState.CONFLICT_DONE_VALUE;
        break;
      case "RECOVER_WAIT":
        result = FailoverState.RECOVER_WAIT_VALUE;
        break;
      default:
        break;
    }

    return result;
  }

  public static String stateForValue(int value) {
    String result = "UNKNOWN";

    switch (value) {
      case FailoverState.STARTUP_VALUE:
        result = "STARTUP";
        break;
      case FailoverState.NORMAL_VALUE:
        result = "NORMAL";
        break;
      case FailoverState.COMM_INTERRUPTED_VALUE:
        result = "COMM_INTERRUPTED";
        break;
      case FailoverState.DOWN_VALUE:
        result = "DOWN";
        break;
      case FailoverState.CONFLICT_VALUE:
        result = "CONFLICT";
        break;
      case FailoverState.RECOVER_VALUE:
        result = "RECOVER";
        break;
      case FailoverState.PAUSED_VALUE:
        result = "PAUSED";
        break;
      case FailoverState.SHUTDOWN_VALUE:
        result = "SHUTDOWN";
        break;
      case FailoverState.RECOVER_DONE_VALUE:
        result = "RECOVER_DONE";
        break;
      case FailoverState.RESOLUTION_INTERRUPTED_VALUE:
        result = "RESOLUTION_INTERRUPTED";
        break;
      case FailoverState.CONFLICT_DONE_VALUE:
        result = "CONFLICT_DONE";
        break;
      case FailoverState.RECOVER_WAIT_VALUE:
        result = "RECOVER_WAIT";
        break;
      default:
        break;
    }

    return result;
  }

  @Override
  public int compareTo(FailoverState o) {
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