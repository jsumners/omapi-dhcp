package org.talamonso.OMAPI.models;

public class DhcpKey {
  private String name;
  private String value;

  public DhcpKey() {}

  public DhcpKey(String name, String value) {
    this.name = name;
    this.value = value;
  }

  public String getName() {
    return this.name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getValue() {
    return this.value;
  }

  public void setValue(String value) {
    this.value = value;
  }

  public byte[] toSignature() {
    return null;
  }
}