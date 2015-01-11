package org.talamonso.OMAPI.models;

public class ValuePair {
  private final String name;
  private final String value;

  public ValuePair(String name, String value) {
    this.name = name;
    this.value = value;
  }

  public String getName() {
    return this.name;
  }

  public String getValue() {
    return this.value;
  }

  @Override
  public String toString() {
    return String.format("{`%s` : `%s`}", this.name, this.value);
  }
}