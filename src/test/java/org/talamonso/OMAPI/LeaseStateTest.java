package org.talamonso.OMAPI;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertTrue;

public class LeaseStateTest {

  private LeaseState leaseState;

  @Before
  public void setup() throws Exception {
    this.leaseState = new LeaseState(LeaseState.FREE);
  }

  @Test
  public void testGetState() throws Exception {
    int state = this.leaseState.getState();
    assertTrue(state == LeaseState.FREE);
  }

  @Test
  public void testNameForValue() throws Exception {
    String name = LeaseState.nameForValue(this.leaseState);
    assertTrue(name.equals("FREE"));
  }

  @Test
  public void testCompareTo() throws Exception {
    LeaseState localState = new LeaseState(LeaseState.FREE);
    assertTrue(this.leaseState.compareTo(localState) == 0);

    localState = new LeaseState(-1);
    assertTrue(this.leaseState.compareTo(localState) < 0);

    localState = new LeaseState(LeaseState.ABANDONDED);
    assertTrue(this.leaseState.compareTo(localState) > 0);
  }
}