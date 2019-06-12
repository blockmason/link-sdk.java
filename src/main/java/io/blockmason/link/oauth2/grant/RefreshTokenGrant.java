package io.blockmason.link.oauth2.grant;

import org.json.JSONObject;

public class RefreshTokenGrant {
  private final String refreshToken;

  public RefreshTokenGrant(final String refreshToken) {
    this.refreshToken = refreshToken;
  }

  public String toJSON() {
    JSONObject json = new JSONObject();

    json.put("grant_type", "refresh_token");
    json.put("refresh_token", refreshToken);

    return json.toString();
  }
}
