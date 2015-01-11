package org.talamonso.OMAPI.models;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import com.google.common.io.BaseEncoding;
import com.google.common.primitives.Bytes;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Values {
  private static final Logger log = LoggerFactory.getLogger(Values.class);

  protected Map<String, ValuePair> values;

  public Values() {
    this.values = new HashMap<>();
  }

  public void add(String name, String value) {
    if (this.values.containsKey(name)) {
      this.values.remove(name);
    }

    this.values.put(name, new ValuePair(name, value));
  }

  public Optional<String> get(String name) {
    Optional<String> result = Optional.empty();

    if (this.values.containsKey(name)) {
      result = Optional.of(this.values.get(name).getValue());
    }

    return result;
  }

  public void remove(String name) {
    if (this.values.containsKey(name)) {
      this.values.remove(name);
    }
  }

  public void set(String name, String value) {
    this.add(name, value);
  }

  public int size() {
    return this.values.size();
  }

  public byte[] toBytes() {
    byte[] result = null;

    try {
      ByteArrayOutputStream stream = new ByteArrayOutputStream();

      ByteBuffer bb32 = ByteBuffer.allocate(4);
      for (String k : this.values.keySet()) {
        ValuePair v = this.values.get(k);
        bb32.clear();
        String value = v.getValue();

        byte[] integer = bb32.putInt(k.length()).array();
        byte[] nameLength = new byte[2];
        nameLength[0] = integer[2];
        nameLength[1] = integer[3];
        byte[] nameValue = k.getBytes();

        bb32.clear();
        byte[] valueLength = bb32.putInt(value.length()).array();
        byte[] valueValue =
          BaseEncoding.base16().encode(value.getBytes()).getBytes();

        stream.write(
          Bytes.concat(nameLength, nameValue, valueLength, valueValue)
        );
      }

      // Terminate the list of name+value pairs
      byte[] term = {0x00, 0x00};
      stream.write(term);

      result = stream.toByteArray();
    } catch (IOException e) {
      log.error("Could not write the byte array stream: `{}`", e.getMessage());
      log.debug(e.toString());
      result = null;
    }

    return result;
  }

  @Override
  public String toString() {
    StringBuilder result = new StringBuilder();

    this.values.forEach(
      (k, v) -> result.append(v.toString())
    );

    return result.toString();
  }
}