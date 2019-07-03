package io.blockmason.link.oauth2;

import io.blockmason.link.oauth2.grant.ClientCredentialsGrant;
import io.blockmason.link.oauth2.grant.RefreshTokenGrant;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URL;
import javax.net.ssl.HttpsURLConnection;

public class Session {
  public static final String DEFAULT_BASE_URL = "https://api.block.mason.link";

  private static final String CONTENT_TYPE_JSON = "application/json";
  private static final String ENCODING_UTF8 = "UTF-8";
  private static final String HTTP_HEADER_ACCEPT = "Accept";
  private static final String HTTP_HEADER_AUTHORIZATION = "Authorization";
  private static final String HTTP_HEADER_CONTENT_TYPE = "Content-Type";
  private static final String HTTP_METHOD_POST = "POST";
  private static final String PATH_OAUTH2_TOKEN = "/oauth2/token";
  private static final String TOKEN_TYPE_BEARER = "Bearer";

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
    final URL url = new URL(String.format("%s%s", baseURL, PATH_OAUTH2_TOKEN));
    final HttpsURLConnection connection = (HttpsURLConnection) url.openConnection();

    connection.setDoInput(true);
    connection.setDoOutput(true);

    connection.setRequestMethod(HTTP_METHOD_POST);
    connection.setRequestProperty(HTTP_HEADER_CONTENT_TYPE, CONTENT_TYPE_JSON);
    connection.setRequestProperty(HTTP_HEADER_ACCEPT, CONTENT_TYPE_JSON);

    final OutputStreamWriter writer = new OutputStreamWriter(connection.getOutputStream(), ENCODING_UTF8);
    writer.write(requestJSON, 0, requestJSON.length());
    writer.flush();
    writer.close();

    try {
      return new Session(Credential.fromJSON(new BufferedReader(new InputStreamReader(connection.getInputStream(), ENCODING_UTF8)).readLine()), baseURL);
    } catch (final IOException exception) {
      if (AuthenticationFailure.is(exception)) {
        throw new AuthenticationFailure(exception);
      }

      throw exception;
    }
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
    connection.setRequestProperty(HTTP_HEADER_ACCEPT, CONTENT_TYPE_JSON);
    connection.setRequestProperty(HTTP_HEADER_AUTHORIZATION, String.format("%s %s", TOKEN_TYPE_BEARER, credential.getAccessToken()));

    if (requestJSON != null) {
      connection.setDoOutput(true);
      connection.setRequestProperty(HTTP_HEADER_CONTENT_TYPE, CONTENT_TYPE_JSON);

      final OutputStreamWriter writer = new OutputStreamWriter(connection.getOutputStream(), ENCODING_UTF8);
      writer.write(requestJSON, 0, requestJSON.length());
      writer.flush();
      writer.close();
    }

    try {
      return new BufferedReader(new InputStreamReader(connection.getInputStream(), ENCODING_UTF8)).readLine();
    } catch (final IOException exception) {
      if (AuthenticationFailure.is(exception)) {
        throw new AuthenticationFailure(exception);
      }

      throw exception;
    }
  }
}
