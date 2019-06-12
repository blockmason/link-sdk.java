package io.blockmason.link.oauth2;

import io.blockmason.link.oauth2.grant.ClientCredentialsGrant;
import io.blockmason.link.oauth2.grant.RefreshTokenGrant;
import io.blockmason.link.oauth2.Credential;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URL;
import javax.net.ssl.HttpsURLConnection;

public class Session {
  public static final String DEFAULT_BASE_URL = "https://api.block.mason.link";

  public static Session create(final String clientId, final String clientSecret, final String baseURL) throws IOException {
    return create(new ClientCredentialsGrant(clientId, clientSecret), baseURL);
  }

  public static Session create(final String clientId, final String clientSecret) throws IOException {
    return create(new ClientCredentialsGrant(clientId, clientSecret));
  }

  public static Session create(final ClientCredentialsGrant grant) throws IOException {
    return create(grant, DEFAULT_BASE_URL);
  }

  public static Session create(final ClientCredentialsGrant grant, final String baseURL) throws IOException {
    return grant(grant.toJSON(), baseURL);
  }

  public static Session create(final RefreshTokenGrant grant) throws IOException {
    return create(grant, DEFAULT_BASE_URL);
  }

  public static Session create(final RefreshTokenGrant grant, final String baseURL) throws IOException {
    return grant(grant.toJSON(), baseURL);
  }

  private static Session grant(final String requestJSON, final String baseURL) throws IOException {
    final URL url = new URL(String.format("%s/oauth2/token", baseURL));
    final HttpsURLConnection connection = (HttpsURLConnection) url.openConnection();

    connection.setDoInput(true);
    connection.setDoOutput(true);

    connection.setRequestMethod("POST");
    connection.setRequestProperty("Content-Type", "application/json");
    connection.setRequestProperty("Accept", "application/json");

    final OutputStreamWriter writer = new OutputStreamWriter(connection.getOutputStream(), "UTF-8");
    writer.write(requestJSON, 0, requestJSON.length());
    writer.flush();
    writer.close();

    final Credential credential = Credential.fromJSON(new BufferedReader(new InputStreamReader(connection.getInputStream(), "UTF-8")).readLine());
    return new Session(credential, baseURL);
  }

  private final String baseURL;
  private final Credential credential;

  public Credential getCredential() {
    return credential;
  }

  public Session(final Credential credential) {
    this(credential, DEFAULT_BASE_URL);
  }

  public Session(final Credential credential, final String baseURL) {
    this.baseURL = baseURL;
    this.credential = credential;
  }

  public Session refresh() throws IOException {
    return create(new RefreshTokenGrant(credential.getRefreshToken()), baseURL);
  }

  public String send(final String method, final String path) throws IOException {
    return send(method, path, null);
  }

  public String send(final String method, final String path, final String requestJSON) throws IOException {
    final URL url = new URL(String.format("%s%s", baseURL, path));
    final HttpsURLConnection connection = (HttpsURLConnection) url.openConnection();

    connection.setRequestMethod(method);

    connection.setDoInput(true);
    connection.setRequestProperty("Accept", "application/json, application/vnd.api+json");
    connection.setRequestProperty("Authorization", String.format("Bearer %s", credential.getAccessToken()));

    if (requestJSON != null) {
      connection.setDoOutput(true);
      connection.setRequestProperty("Content-Type", "application/json");

      final OutputStreamWriter writer = new OutputStreamWriter(connection.getOutputStream(), "UTF-8");
      writer.write(requestJSON, 0, requestJSON.length());
      writer.flush();
      writer.close();
    }

    final String responseJSON = new BufferedReader(new InputStreamReader(connection.getInputStream(), "UTF-8")).readLine();

    return responseJSON;
  }
}
