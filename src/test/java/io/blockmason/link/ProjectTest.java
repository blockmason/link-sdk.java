package io.blockmason.link;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.IOException;
import org.json.JSONObject;
import org.junit.jupiter.api.Test;

public class ProjectTest {
  private static String getClientID() {
    return System.getenv().get("LINK_CLIENT_ID");
  }

  private static String getClientSecret() {
    return System.getenv().get("LINK_CLIENT_SECRET");
  }

  @Test
  public void happyPath() throws IOException {
    Project market = Project.at(getClientID(), getClientSecret());

    JSONObject inputs = new JSONObject();

    inputs.put("item", "cheese");

    JSONObject price = market.get("/getPrice", inputs);

    assertEquals(0, price.getInt("latest"));
    assertEquals(0, price.getInt("mean"));
    assertEquals(0, price.getInt("median"));
    assertEquals(0, price.getInt("minimum"));
    assertEquals(0, price.getInt("maximum"));
  }
}
