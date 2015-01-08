package org.talamonso.OMAPI;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertTrue;

public class LeaseStateTest {

  private LeaseState leaseState;

  @Before
  public void setup() throws Exception {
    this.leaseState = LeaseState.FREE;
  }

  @Test
  public void testGetState() throws Exception {
    String state = this.leaseState.getState();
    assertTrue(state.equals("FREE"));
  }

  @Test
  public void testNameForValue() throws Exception {
    String name = LeaseState.stateForValue(this.leaseState.value());
    assertTrue(name.equals("FREE"));
  }

  @Test
  public void testCompareTo() throws Exception {
    LeaseState localState = LeaseState.FREE;
    assertTrue(this.leaseState.compareTo(localState) == 0);

    localState = new LeaseState(-1);
    assertTrue(this.leaseState.compareTo(localState) < 0);

    localState = LeaseState.ABANDONDED;
    assertTrue(this.leaseState.compareTo(localState) > 0);
  }
}