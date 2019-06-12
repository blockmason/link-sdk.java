package io.blockmason.link.oauth2;

import org.json.JSONObject;
import org.json.JSONTokener;

public class Credential {
  public static Credential fromJSON(final String json) {
    JSONObject object = (JSONObject) new JSONTokener(json).nextValue();
    Credential credential = new Credential();

    credential.accessToken = object.getString("access_token");
    credential.expiresIn = object.getInt("expires_in");
    credential.refreshToken = object.getString("refresh_token");
    credential.scope = object.getString("scope");
    credential.tokenType = object.getString("token_type");

    return credential;
  }

  private String accessToken;
  private int expiresIn;
  private String refreshToken;
  private String scope;
  private String tokenType;

  public String getAccessToken() {
    return accessToken;
  }

  public int getExpiresIn() {
    return expiresIn;
  }

  public String getRefreshToken() {
    return refreshToken;
  }

  public String getScope() {
    return scope;
  }

  public String getTokenType() {
    return tokenType;
  }
}
