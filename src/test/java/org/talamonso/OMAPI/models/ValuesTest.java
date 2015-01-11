package org.talamonso.OMAPI.models;

import java.util.Optional;

import com.google.common.io.BaseEncoding;
import org.junit.Before;
import org.junit.Test;

import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

public class ValuesTest {

  private Values values;

  @Before
  public void setup() throws Exception {
    this.values = new Values();
    this.values.add("foo", "bar");
  }

  @Test
  public void testAdd() throws Exception {
    this.values.add("bar", "baz");
    assertEquals(2, this.values.size());

    this.values.add("bar", "foobar");
    assertEquals(2, this.values.size());
    assertEquals("foobar", this.values.get("bar").get());
  }

  @Test
  public void testGet() throws Exception {
    Optional<String> stringOptional = this.values.get("foo");
    assertTrue(stringOptional.isPresent());
    assertEquals("bar", stringOptional.get());

    stringOptional = this.values.get("none");
    assertFalse(stringOptional.isPresent());
  }


  @Test
  public void testToBytes() throws Exception {
    String valid = "0003666F6F000000033632363137320000";
    byte[] bytes = this.values.toBytes();

    assertEquals(valid, BaseEncoding.base16().encode(bytes));

    Values emptyValues = new Values();
    assertEquals("0000", BaseEncoding.base16().encode(emptyValues.toBytes()));
  }

  @Test
  public void testRemove() throws Exception {
    this.values.remove("foo");
    assertEquals(0, this.values.size());
  }
}