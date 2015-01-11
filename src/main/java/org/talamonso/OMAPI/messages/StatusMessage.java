package org.talamonso.OMAPI.messages;

import java.nio.ByteBuffer;

import com.google.common.primitives.Bytes;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.talamonso.OMAPI.Exceptions.OmapiException;

public class StatusMessage extends Message {
  private static final Logger log = LoggerFactory.getLogger(StatusMessage.class);

  // The api+protocol doc says they should be 100 and 56, respectively.
  // But it seems 24 is the real length number.
  private byte[] protocolVersion = {0x00, 0x00, 0x00, 0x64}; // 100
  private byte[] headerLength = {0x00, 0x00, 0x00, 0x18}; // 24

  public StatusMessage(byte[] message) throws OmapiException {
    super();

    if (message.length != 8) {
      throw new OmapiException("Status message length must be 8 bytes");
    }

    System.arraycopy(message, 0, this.protocolVersion, 0, 4);
    System.arraycopy(message, 4, this.headerLength, 0, 4);
  }

  /**
   * If you need to specify a different header length than the default, 24,
   * you can use this method to do so.
   *
   * @param length
   */
  public void setHeaderLength(int length) {
    this.headerLength = ByteBuffer.allocate(4).putInt(length).array();
  }

  /**
   * If you need to specify a different protocol version than the default 100,
   * i.e. 1.00, you can use this method to do.
   *
   * @param version
   */
  public void setProtocolVersion(int version) {
    this.protocolVersion = ByteBuffer.allocate(4).putInt(version).array();
  }

  @Override
  public byte[] toBytes() {
    return Bytes.concat(this.protocolVersion, this.headerLength);
  }
}