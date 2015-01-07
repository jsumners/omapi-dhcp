package org.talamonso.OMAPI;

import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.junit.Assert.assertTrue;

public class ConvertTest {
  private static final Logger log = LoggerFactory.getLogger(ConvertTest.class);

  @Test
  public void testHex2date() throws Exception {
    // 2015/01/07 17:35:15 +0
    byte[] bytesDate = new byte[4];
    bytesDate[0] = (byte) 0x54;
    bytesDate[1] = (byte) 0xAD;
    bytesDate[2] = (byte) 0x6E;
    bytesDate[3] = (byte) 0X53;
    
    String dateTimeString = Convert.hex2date(bytesDate);

    Instant referenceInstant = Instant.ofEpochSecond(1420652115);
    DateTimeFormatter formatter = DateTimeFormatter.RFC_1123_DATE_TIME;
    ZonedDateTime zonedDateTime = ZonedDateTime.ofInstant(referenceInstant, ZoneId.of("UTC"));
    String referenceDateString = formatter.format(zonedDateTime);

    assertTrue(dateTimeString.equals(referenceDateString));
  }
}