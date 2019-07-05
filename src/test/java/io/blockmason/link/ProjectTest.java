package io.blockmason.link;

import static org.junit.jupiter.api.Assertions.assertEquals;

import io.blockmason.link.oauth2.Credential;
import io.blockmason.link.oauth2.Session;
import java.io.IOException;
import org.json.JSONObject;
import org.junit.jupiter.api.Test;

public class ProjectTest {
  static class MockSession extends Session {
    public MockSession() {
      super(new Credential());
    }

    @Override
    public Session refresh() {
      return this;
    }

    @Override
    public String send(final String method, final String path, final String requestJSON) throws IOException {
      return "{\"message\":\"Hello, world!\"}";
    }
  }

  @Test
  public void happyPath() throws IOException {
    Session session = new MockSession();
    Project project = new Project(session);
    String message = "Hello, world!";

    JSONObject inputs = new JSONObject();

    inputs.put("message", message);

    JSONObject outputs = project.get("/echo", inputs);

    assertEquals(message, outputs.getString("message"));
  }
}
