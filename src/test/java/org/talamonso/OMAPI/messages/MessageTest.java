package org.talamonso.OMAPI.messages;

import com.google.common.io.BaseEncoding;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.talamonso.OMAPI.MessageOperation;

import static org.junit.Assert.assertEquals;

public class MessageTest {
  private static final Logger log = LoggerFactory.getLogger(MessageTest.class);

  private Message message;

  private String authenticatorResponse =
    "0000000000000000000000030000000173679759dd3ab862000000046e616d650000000" +
    "96f6d6170695f6b65790009616c676f726974686d00000019484d41432d4d44352e5349" +
    "472d414c472e5245472e494e542e0000";

  @Before
  public void setup() throws Exception {
    this.message = new Message();
  }

  @Test
  public void testMessageFromBytes() throws Exception {
    Message message1 = new Message(
      BaseEncoding.base16().decode(this.authenticatorResponse.toUpperCase())
    );

    assertEquals(
      "0000000000000000",
      BaseEncoding.base16().encode(message1.authenticatorHeader())
    );
    assertEquals(MessageOperation.UPD_VALUE, message1.getOperation().value());
    assertEquals(1, message1.getHandle().intValue());
    assertEquals(1936168793L, message1.getId().longValue());
    assertEquals(3711613026L, message1.getResponseId().longValue());

    assertEquals(0, message1.getMessageValues().size());
    assertEquals(2, message1.getObjectValues().size());
  }

  @Test
  public void testToString() throws Exception {
    log.info(this.message.toString());
  }
}