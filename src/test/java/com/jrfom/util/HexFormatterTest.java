package com.jrfom.util;

import java.security.SecureRandom;

import com.google.common.io.BaseEncoding;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class HexFormatterTest {
  private final String testString = "666F6F626172";

  @Test
  public void testWithSpaces() throws Exception {
    String spacesResult = "66 6F 6F 62 61 72";

    String formatted = HexFormatter.withSpaces(this.testString);
    assertEquals(spacesResult, formatted);

    SecureRandom random = new SecureRandom();
    byte[] randomBytes = new byte[6];
    random.nextBytes(randomBytes);
    String randomString = BaseEncoding.base16().encode(randomBytes);
    String randomStringFormatted = HexFormatter.withSpaces(randomString);
    String randomBytesFormatted = HexFormatter.withSpaces(randomBytes);
    assertEquals(randomStringFormatted, randomBytesFormatted);

    String notHex = "foobar";
    String notHexFormatted = HexFormatter.withSpaces(notHex);
    assertEquals("fooba r", notHexFormatted);

    String wacko = "6 66 F6:F62-6172";
    String wackoFormatted = HexFormatter.withSpaces(wacko);
    assertEquals(spacesResult, wackoFormatted);
  }
}