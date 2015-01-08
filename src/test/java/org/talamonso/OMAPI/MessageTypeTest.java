package org.talamonso.OMAPI;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertTrue;

public class MessageTypeTest {
  private MessageType messageType;

  @Before
  public void setup() throws Exception {
    this.messageType = MessageType.CREATE;
  }

  @Test
  public void testGetType() throws Exception {
    String type = this.messageType.getType();
    assertTrue(type.equals(MessageType.CREATE.getType()));
  }

  @Test
  public void testCompareTo() throws Exception {
    MessageType localMessage = MessageType.CREATE;
    assertTrue(this.messageType.compareTo(localMessage) == 0);

    localMessage = MessageType.OPEN;
    assertTrue(this.messageType.compareTo(localMessage) < 0);

    localMessage = MessageType.DELETE;
    assertTrue(this.messageType.compareTo(localMessage) > 0);
  }

  @Test
  public void testValue() throws Exception {
    assertTrue(this.messageType.value() == MessageType.CREATE_VALUE);
  }

  @Test
  public void testTypeOf() throws Exception {
    assertTrue(MessageType.typeOf(-1).getType().equals("UNKNOWN"));
  }

  @Test
  public void testTypeForValue() throws Exception {
    assertTrue(MessageType.typeForValue(-1).equals("UNKNOWN"));
  }

  @Test
  public void testValueForType() throws Exception {
    assertTrue(MessageType.valueForType("UNKNOWN") == -1);
  }
}