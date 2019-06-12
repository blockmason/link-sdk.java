package io.blockmason.link.oauth2.grant;

import org.json.JSONObject;

public class ClientCredentialsGrant {
  private final String clientId;
  private final String clientSecret;

  public ClientCredentialsGrant(final String clientId, final String clientSecret) {
    this.clientId = clientId;
    this.clientSecret = clientSecret;
  }

  public String toJSON() {
    JSONObject json = new JSONObject();

    json.put("grant_type", "client_credentials");
    json.put("client_id", clientId);
    json.put("client_secret", clientSecret);

    return json.toString();
  }
}
