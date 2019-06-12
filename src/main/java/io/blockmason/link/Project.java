package io.blockmason.link;

import io.blockmason.link.oauth2.Session;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Iterator;
import java.net.URLEncoder;
import org.json.JSONObject;
import org.json.JSONTokener;

public class Project {
  public static Project at(final String clientId, final String clientSecret) throws IOException {
    return new Project(Session.create(clientId, clientSecret));
  }

  public static Project at(final String clientId, final String clientSecret, final String baseURL) throws IOException {
    return new Project(Session.create(clientId, clientSecret, baseURL));
  }

  private static String encodeURIComponent(final String component) {
    try {
      return URLEncoder.encode(component, "UTF-8").replaceAll("\\+", "%20").replaceAll("\\%21", "!").replaceAll("\\%27", "'").replaceAll("\\%28", "(").replaceAll("\\%29", ")").replaceAll("\\%7E", "~");
    } catch (UnsupportedEncodingException ignore) {
      return component;
    }
  }

  private Session session;

  public Project(final Session session) {
    this.session = session;
  }

  public String get(final String path) throws IOException {
    return session.send("GET", String.format("/v1%s", path));
  }

  public JSONObject get(final String path, final JSONObject inputs) throws IOException {
    return parseJSON(session.send("GET", String.format("/v1%s%s", path, toQueryString(inputs))));
  }

  public JSONObject post(final String path, final JSONObject inputs) throws IOException {
    return parseJSON(post(path, toJSON(inputs)));
  }

  public String post(final String path, final String inputs) throws IOException {
    return session.send("POST", String.format("/v1%s", path), inputs);
  }

  private static JSONObject parseJSON(final String outputs) {
    return (JSONObject) new JSONTokener(outputs).nextValue();
  }

  private static String toJSON(final JSONObject inputs) {
    return inputs.toString();
  }

  private static String toQueryString(final JSONObject inputs) {
    final StringBuilder query = new StringBuilder();

    final Iterator<String> it = inputs.keys();

    while (it.hasNext()) {
      final String key = it.next();
      if (query.length() <= 0) {
        query.append("?");
      } else {
        query.append("&");
      }

      query.append(encodeURIComponent(key)).append("=").append(encodeURIComponent(inputs.get(key).toString()));
    }

    return query.toString();
  }
}
