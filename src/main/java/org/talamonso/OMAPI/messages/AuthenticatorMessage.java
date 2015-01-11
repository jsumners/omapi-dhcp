package org.talamonso.OMAPI.messages;

public class AuthenticatorMessage extends Message {
  public AuthenticatorMessage(String keyName) {
    super();

    this.messageValues.set("type", "authenticator");
    this.objectValues.set("name", keyName);
    this.objectValues.set("algorithm", "hmac-md5.SIG-ALG.REG.INT.");
  }
}