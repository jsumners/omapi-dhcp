package com.jrfom.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.google.common.io.BaseEncoding;

public class HexFormatter {
  /**
   * The same as {@link #withSpaces(String)} but takes a raw {@code byte[]}
   * input instead. The byte array is first converted to a hexadecimal string
   * representation, e.g. "666F6F626172", before the formatting is performed.
   *
   * @param input An array of raw bytes to convert and format
   * @return A string of space separated hexadecimal characters
   */
  public static String withSpaces(byte[] input) {
    String string = BaseEncoding.base16().encode(input);
    return HexFormatter.withSpaces(string);
  }

  /**
   * Formats a string of hexadecimal characters with spaces. For example, if
   * you have the string "666F6F626172" then the result will
   * be "66 6F 6F 62 61 72". Be sure to feed it an actual hex string. If you
   * feed it something like "foobar" then you could get weird results
   * (e.g. "fooba r").
   *
   * @param input A string of hexadecimal characters to format
   * @return A string of space separated hexadecimal characters
   */
  public static String withSpaces(String input){
    String _input = input.replaceAll("[\\p{Punct}\\p{Blank}]", "");
    Pattern pattern = Pattern.compile("([0-9a-fA-F]{2}+)");
    Matcher matcher = pattern.matcher(_input);
    return matcher.replaceAll("$1 ").trim();
  }
 }