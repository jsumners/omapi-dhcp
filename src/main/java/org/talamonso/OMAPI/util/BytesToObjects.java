package org.talamonso.OMAPI.util;

import com.google.common.base.Verify;

public class BytesToObjects {
  public static Long bytesToLong(byte[] bytes) {
    long value = 0;

    for (int i = 0, j = bytes.length; i < j; i += 1) {
      value |= bytes[i] & 0xFF;
      if (i != (j-1)) {
        value <<= 8;
      }
    }

    return Long.valueOf(value);
  }

  public static Integer bytesToInteger(byte[] bytes) {
    Verify.verify(bytes.length == 2);
    int value = 0;

    value |= bytes[0] & 0xFF;
    value <<= 8;
    value |= bytes[1] & 0xFF;

    return Integer.valueOf(value);
  }

  public static int bytesToInt(byte[] bytes) {
    return BytesToObjects.bytesToInteger(bytes).intValue();
  }
}