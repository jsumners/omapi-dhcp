package org.talamonso.OMAPI;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertTrue;

public class MessageTypeTest {
  private MessageType messageType;

  @Before
  public void setup() throws Exception {
    this.messageType = new MessageType(MessageType.CREATE);
  }

  @Test
  public void testGetType() throws Exception {
    int type = this.messageType.getType();
    assertTrue(type == MessageType.CREATE);
  }

  @Test
  public void testNameForType() throws Exception {
    String name = MessageType.nameForType(this.messageType);
    assertTrue(name.equals("CREATE"));
  }

  @Test
  public void testNameForValue() throws Exception {
    String name = MessageType.nameForValue(MessageType.CREATE);
    assertTrue(name.equals("CREATE"));
  }

  @Test
  public void testCompareTo() throws Exception {
    MessageType localMessage = new MessageType(MessageType.CREATE);
    assertTrue(this.messageType.compareTo(localMessage) == 0);

    localMessage = new MessageType(MessageType.OPEN);
    assertTrue(this.messageType.compareTo(localMessage) < 0);

    localMessage = new MessageType(MessageType.DELETE);
    assertTrue(this.messageType.compareTo(localMessage) > 0);
  }
}