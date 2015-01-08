package org.talamonso.OMAPI;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertTrue;

public class FailoverStateTest {

  private FailoverState failoverState;

  @Before
  public void setup() throws Exception {
    this.failoverState = new FailoverState(FailoverState.NORMAL);
  }

  @Test
  public void testGetState() throws Exception {
    int state = this.failoverState.getState();
    assertTrue(state == FailoverState.NORMAL);
  }

  @Test
  public void testNameForValue() throws Exception {
    String name = FailoverState.nameForValue(this.failoverState);
    assertTrue(name.equals("NORMAL"));
  }

  @Test
  public void testCompareTo() throws Exception {
    FailoverState localState = new FailoverState(FailoverState.NORMAL);
    assertTrue(this.failoverState.compareTo(localState) == 0);

    localState = new FailoverState(-1);
    assertTrue(this.failoverState.compareTo(localState) < 0);

    localState = new FailoverState(FailoverState.SHUTDOWN);
    assertTrue(this.failoverState.compareTo(localState) > 0);
  }
}