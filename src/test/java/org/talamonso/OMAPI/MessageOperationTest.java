package org.talamonso.OMAPI;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertTrue;

public class MessageOperationTest {
  private MessageOperation messageOperation;

  @Before
  public void setup() throws Exception {
    this.messageOperation = MessageOperation.CREATE;
  }

  @Test
  public void testGetType() throws Exception {
    String type = this.messageOperation.getType();
    assertTrue(type.equals(MessageOperation.CREATE.getType()));
  }

  @Test
  public void testCompareTo() throws Exception {
    MessageOperation localMessage = MessageOperation.CREATE;
    assertTrue(this.messageOperation.compareTo(localMessage) == 0);

    localMessage = MessageOperation.OPEN;
    assertTrue(this.messageOperation.compareTo(localMessage) < 0);

    localMessage = MessageOperation.DELETE;
    assertTrue(this.messageOperation.compareTo(localMessage) > 0);
  }

  @Test
  public void testValue() throws Exception {
    assertTrue(this.messageOperation.value() == MessageOperation.CREATE_VALUE);
  }

  @Test
  public void testTypeOf() throws Exception {
    assertTrue(MessageOperation.typeOf(-1).getType().equals("UNKNOWN"));
  }

  @Test
  public void testTypeForValue() throws Exception {
    assertTrue(MessageOperation.typeForValue(-1).equals("UNKNOWN"));
  }

  @Test
  public void testValueForType() throws Exception {
    assertTrue(MessageOperation.valueForType("UNKNOWN") == -1);
  }
}