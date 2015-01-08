package org.talamonso.OMAPI;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertTrue;

public class FailoverStateTest {

  private FailoverState failoverState;

  @Before
  public void setup() throws Exception {
    this.failoverState = FailoverState.NORMAL;
  }

  @Test
  public void testGetState() throws Exception {
    String state = this.failoverState.getState();
    assertTrue(state.equals("NORMAL"));
  }

  @Test
  public void testCompareTo() throws Exception {
    FailoverState localState = FailoverState.NORMAL;
    assertTrue(this.failoverState.compareTo(localState) == 0);

    localState = new FailoverState(-1);
    assertTrue(this.failoverState.compareTo(localState) < 0);

    localState = FailoverState.SHUTDOWN;
    assertTrue(this.failoverState.compareTo(localState) > 0);
  }
}