package io.blockmason.link.oauth2;

import java.io.IOException;

public class AuthenticationFailure extends IOException {
  private static final String PATTERN = "HTTP response code: 401";

  public static boolean is(IOException exception) {
    return exception.getMessage().contains(PATTERN);
  }

  public AuthenticationFailure(Throwable cause) {
    super(cause);
  }
}
