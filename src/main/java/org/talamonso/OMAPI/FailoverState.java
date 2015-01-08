package org.talamonso.OMAPI;

import java.io.Serializable;

public class FailoverState implements Comparable<FailoverState>, Serializable {
  private static final long serialVersionUID = 1L;

  public static final int STARTUP = 1;
  public static final int NORMAL = 2;
  public static final int COMM_INTERRUPTED = 3;
  public static final int DOWN = 4;
  public static final int CONFLICT = 5;
  public static final int RECOVER = 6;
  public static final int PAUSED = 7;
  public static final int SHUTDOWN = 8;
  public static final int RECOVER_DONE = 9;
  public static final int RESOLUTION_INTERRUPTED = 10;
  public static final int CONFLICT_DONE = 11;
  public static final int RECOVER_WAIT = 12;

  private final int state;

  public FailoverState(int state) {
    this.state = state;
  }

  public int getState() {
    return this.state;
  }

  public static String nameForValue(FailoverState leaseState) {
    return FailoverState.nameForValue(leaseState.getState());
  }

  public static String nameForValue(int value) {
    String result = "unknown";

    switch (value) {
      case FailoverState.STARTUP:
        result = "STARTUP";
        break;
      case FailoverState.NORMAL:
        result = "NORMAL";
        break;
      case FailoverState.COMM_INTERRUPTED:
        result = "COMM_INTERRUPTED";
        break;
      case FailoverState.DOWN:
        result = "DOWN";
        break;
      case FailoverState.CONFLICT:
        result = "CONFLICT";
        break;
      case FailoverState.RECOVER:
        result = "RECOVER";
        break;
      case FailoverState.PAUSED:
        result = "PAUSED";
        break;
      case FailoverState.SHUTDOWN:
        result = "SHUTDOWN";
        break;
      case FailoverState.RECOVER_DONE:
        result = "RECOVER_DONE";
        break;
      case FailoverState.RESOLUTION_INTERRUPTED:
        result = "RESOLUTION_INTERRUPTED";
        break;
      case FailoverState.CONFLICT_DONE:
        result = "CONFLICT_DONE";
        break;
      case FailoverState.RECOVER_WAIT:
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
    int oState = o.getState();

    if (oState < this.state) {
      result = -1;
    } else if (oState > this.state) {
      result = 1;
    }

    return result;
  }
}