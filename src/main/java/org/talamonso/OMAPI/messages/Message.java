package org.talamonso.OMAPI.messages;

import java.security.SecureRandom;
import java.util.Arrays;

import com.google.common.io.BaseEncoding;
import com.google.common.primitives.Bytes;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.talamonso.OMAPI.MessageOperation;
import org.talamonso.OMAPI.models.Values;
import org.talamonso.OMAPI.util.BytesToObjects;

public class Message {
  private static final Logger log = LoggerFactory.getLogger(Message.class);

  private byte[] authId;
  private byte[] authLength;

  protected MessageOperation operation;
  protected Long id;
  protected Long responseId;
  protected Long handle;

  protected final Values messageValues;
  protected final Values objectValues;

  public Message() {
    log.debug("Creating instance of: `{}`", this.getClass().getSimpleName());

    this.authId = new byte[]{0x00, 0x00, 0x00, 0x00};
    this.authLength = new byte[]{0x00, 0x00, 0x00, 0x00};

    this.operation = MessageOperation.OPEN;

    this.id = Long.valueOf( Math.abs((new SecureRandom()).nextInt()) );
    this.responseId = 0L;
    this.handle = 0L;

    this.messageValues = new Values();
    this.objectValues = new Values();
  }

  public Message(byte[] messageBytes) {
    log.debug("Creating Message instance from bytes");
    log.debug(BaseEncoding.base16().encode(messageBytes));

    this.authId = new byte[4];
    this.authLength = new byte[4];
    System.arraycopy(messageBytes, 0, this.authId, 0, 4);
    System.arraycopy(messageBytes, 4, this.authLength, 0, 4);

    byte[] opCodeBytes = new byte[4];
    System.arraycopy(messageBytes, 8, opCodeBytes, 0, 4);
    Long opCode = BytesToObjects.bytesToLong(opCodeBytes);
    this.operation = MessageOperation.typeOf(opCode.intValue());

    byte[] handleBytes = new byte[4];
    System.arraycopy(messageBytes, 12, handleBytes, 0, 4);
    this.handle = BytesToObjects.bytesToLong(handleBytes);

    byte[] idBytes = new byte[4];
    System.arraycopy(messageBytes, 16, idBytes, 0, 4);
    this.id = BytesToObjects.bytesToLong(idBytes);

    byte[] ridBytes = new byte[4];
    System.arraycopy(messageBytes, 20, ridBytes, 0, 4);
    this.responseId = BytesToObjects.bytesToLong(ridBytes);

    // According to the api+protocol doc there should be an
    // "authlen" 32-bit integer here, but that never seems
    // to be the case.

    // Parse the message values
    ParseValuesBytes parser = new ParseValuesBytes();
    ParsedResult parsed = parser.parse(
      Arrays.copyOfRange(messageBytes, 24, messageBytes.length)
    );
    int valuesLength = parsed.getParsedBytesLength();
    this.messageValues = parsed.getValues();


    // 24 is the number of bytes parsed prior to the message values.
    // Thus, we need to set our new position to 24 + the number of bytes
    // that was parsed when processing the message values.
    int offset = valuesLength + 24;
    parsed = parser.parse(
      Arrays.copyOfRange(messageBytes, offset, messageBytes.length)
    );
    offset += parsed.getParsedBytesLength();
    this.objectValues = parsed.getValues();

    if (offset < messageBytes.length) {
      // Process the message signature
    }
  }

  /**
   *
   * @return
   */
  public byte[] authenticatorHeader() {
    return Bytes.concat(this.authId, this.authLength);
  }

  public Long getId() {
    return this.id;
  }

  public MessageOperation getOperation() {
    return this.operation;
  }

  public Values getMessageValues() {
    return this.messageValues;
  }

  public Values getObjectValues() {
    return this.objectValues;
  }

  public Long getResponseId() {
    return this.responseId;
  }

  public Long getHandle() {
    return this.handle;
  }

  public void setOperation(MessageOperation operation) {
    this.operation = operation;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public void setResponseId(Long responseId) {
    this.responseId = responseId;
  }

  /**
   * Sets whether or not the message will include an authentication
   * signature.
   *
   * @param shouldSign {@code True} if the message should include an auth
   *                   signature, {@code false} otherwise (the default)
   */
  public void shouldSign(boolean shouldSign) {
    // ID = 1
    this.authId = (shouldSign) ?
      new byte[]{0x00, 0x00, 0x00, 0x01} : new byte[]{0x00, 0x00, 0x00, 0x00};
    // Length = 16
    this.authLength = (shouldSign) ?
      new byte[]{0x00, 0x00, 0x00, 0x10} : new byte[]{0x00, 0x00, 0x00, 0x00};
  }

  public byte[] toBytes() {
    byte[] result = null;

    return result;
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();

    sb.append(
      this.line("authid", BaseEncoding.base16().encode(this.authId))
    );
    sb.append(
      this.line("authlen", BaseEncoding.base16().encode(this.authLength))
    );
    sb.append(
      this.line("op", this.operation.toString())
    );
    sb.append(
      this.line("id", this.id)
    );
    sb.append(
      this.line("rid", this.responseId)
    );
    sb.append(
      this.line("handle", this.handle)
    );
    sb.append(
      this.line("message", this.messageValues.toString())
    );
    sb.append(
      this.line("object", this.objectValues.toString())
    );


    return sb.toString();
  }

  private String line(String name, Object value) {
    return String.format("\n%-15s: %s", name, value);
  }

  private class ParsedResult {
    private int parsedBytesLength;
    private Values values;

    public int getParsedBytesLength() {
      return this.parsedBytesLength;
    }

    public void setParsedBytesLength(int parsedBytesLength) {
      this.parsedBytesLength = parsedBytesLength;
    }

    public Values getValues() {
      return this.values;
    }

    public void setValues(Values values) {
      this.values = values;
    }
  }

  private class ParseValuesBytes {
    public ParsedResult parse(byte[] valuesBytes) {
      Values values = new Values();
      BaseEncoding b16 = BaseEncoding.base16();
      byte[] nameLen = new byte[2];
      byte[] nameVal = null;
      byte[] valLen = new byte[4];
      byte[] valVal = null;

      System.arraycopy(valuesBytes, 0, nameLen, 0, 2);
      int count = 2;
      int len = BytesToObjects.bytesToInt(nameLen);

      int pos = 2;
      while (len > 0) {
        nameVal = new byte[len];
        System.arraycopy(valuesBytes, pos, nameVal, 0, len);
        String name = new String(nameVal);
        pos += len;
        count += len;

        System.arraycopy(valuesBytes, pos, valLen, 0, 4);
        len = BytesToObjects.bytesToLong(valLen).intValue();
        pos += 4;
        count += 4;
        valVal = new byte[len];
        System.arraycopy(valuesBytes, pos, valVal, 0, len);
        String value = new String(valVal);
        pos += len;
        count += len;

        values.set(name, value);

        nameLen = new byte[2];
        System.arraycopy(valuesBytes, pos, nameLen, 0, 2);
        len = BytesToObjects.bytesToInt(nameLen);
        pos += 2;
        count += 2;
      }

      ParsedResult result = new ParsedResult();
      result.setParsedBytesLength(count);
      result.setValues(values);

      return result;
    }
  }
}